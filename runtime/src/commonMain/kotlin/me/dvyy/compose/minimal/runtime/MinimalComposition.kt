package me.dvyy.compose.minimal.runtime

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.dvyy.compose.minimal.runtime.layers.ComposeSceneContext
import me.dvyy.compose.minimal.runtime.layers.LocalComposeSceneContext
import kotlin.coroutines.CoroutineContext

/**
 * A helper for creating compositions with support for multiple layers created via [ComposeSceneContext].
 * Users are responsible for a few more things detailed below.
 *
 * ## User responsibilities
 *
 * ### Provide a [MonotonicFrameClock] and send frames
 *
 * - Per composition, create a [BroadcastFrameClock] and append it to your [coroutineContext] (ex. `myContext + clock`.)
 * - Send frames using [BroadcastFrameClock.sendFrame], after applying snapshots (see below) and before rendering UI.
 * - A multiplatform [nanoTime] implementation is provided by this project for sending frames.
 *
 * ### Apply snapshot writes
 *
 * Compose requires you to set up your global environment for composition updates to trigger correctly,
 * below is an implementation assuming an atomic boolean (any implementation should be fine):
 *
 * ```kotlin
 * // Once in your application
 * private val applyScheduled = atomic(false)
 * private val snapshotHandle = Snapshot.registerGlobalWriteObserver {
 *     applyScheduled.compareAndSet(expect = false, update = true)
 * }
 *
 * // Right after user input, before sending a frame to composition
 * if (applyScheduled.compareAndSet(expect = true, update = false)) {
 *     Snapshot.sendApplyNotifications()
 * }
 *
 * // On shutdown
 * snapshotHandle.dispose()
 * ```
 *
 * - Note: if your [coroutineContext] is not immediate, ensure a dispatch occurs right after sending apply notifications,
 *   and before sending your frame, otherwise recompositions will be delayed by one frame.
 */
class MinimalComposition<T>(
    coroutineContext: CoroutineContext,
    /**
     * Called when snapshot sate changes, and the [Recomposer] has potentially updated a node in the tree.
     * You may use your own logic to determine whether layout calculations or a redraw is needed
     * (see [Mosaic](https://github.com/JakeWharton/mosaic)'s runtime for an example.)
     */
    private val onNodesChanged: () -> Unit,
    /** A composable wrapping the entire composition, useful for global [CompositionLocal]s. */
    private val wrapContent: @Composable (content: @Composable () -> Unit) -> Unit,
    /**
     * Creates a node representing a UI layer. This may be under a single root node or kept in a separate structure.
     *
     * @see ComposeSceneContext
     */
    private val createLayerNode: () -> T,
    /** Called when a layer is removed from composition. */
    private val removeLayerNode: (T) -> Unit,
    /**
     * Provides an [Applier] for a layer node in composition.
     * A different applier will be used per layer, these can be completely independent if needed.
     */
    private val applierForNode: (T) -> Applier<T>,
) : AutoCloseable {
    private val externalClock = checkNotNull(coroutineContext[MonotonicFrameClock]) {
        "Composition requires an external MonotonicFrameClock in its coroutine context"
    }
    private var hasFrameWaiters = false // set to true when Recomposer runs, signalling potential changes
    private val internalClock = BroadcastFrameClock { hasFrameWaiters = true }
    private val job = Job(coroutineContext[Job])
    private val composeContext = coroutineContext + job + internalClock

    private var running = false

    private val scope = CoroutineScope(composeContext)
    private val recomposer = Recomposer(composeContext)

    /** Manages creating multiple layers in composition as box nodes under surface viewport. */
    private val layers = ComposeSceneContext(
        createLayerNode = createLayerNode,
        removeLayerNode = removeLayerNode,
        applierForNode = applierForNode,
    )

    val mainLayer = layers.createLayer(recomposer)

    fun start(content: @Composable () -> Unit) {
        !running || return
        running = true
        startRecomposer()
        startFrameListener()
        setContent(content)
    }

    private fun setContent(content: @Composable () -> Unit) {
        mainLayer.setContent {
            CompositionLocalProvider(
                LocalComposeSceneContext provides layers,
            ) {
                wrapContent {
                    content()
                }
            }
        }
    }

    private fun startRecomposer() {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            try {
                recomposer.runRecomposeAndApplyChanges()
            } finally {
                mainLayer.close()
            }
        }
    }

    private fun startFrameListener() {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            do {
                externalClock.withFrameNanos { nanos ->
                    // Let recomposer update layout
                    internalClock.sendFrame(nanos)

                    if (hasFrameWaiters) {
                        onNodesChanged()
                        hasFrameWaiters = false
                    }
                }
            } while (job.isActive)
        }
    }

    override fun close() {
        recomposer.close()
        job.cancel()
    }
}
