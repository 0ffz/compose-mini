package me.dvyy.compose.minimal.me.dvyy.compose.minimal.runtime

import androidx.compose.runtime.Applier
import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.snapshots.Snapshot
import kotlinx.coroutines.*
import me.dvyy.compose.minimal.me.dvyy.compose.minimal.runtime.layers.ComposeSceneContext
import me.dvyy.compose.minimal.me.dvyy.compose.minimal.runtime.layers.LocalComposeSceneContext
import kotlin.coroutines.CoroutineContext

class MinimalComposition<T : MinimalNode>(
    coroutineContext: CoroutineContext,
    private val onFrameAwaiters: () -> Unit,
    private val wrapContent: @Composable (content: @Composable () -> Unit) -> Unit,
    private val createLayerNode: () -> T,
    private val removeLayerNode: (T) -> Unit,
    private val applierForNode: (T) -> Applier<T> = { MinimalNodeApplier(it) },
) : AutoCloseable {
    private var hasFrameWaiters = false
    private var running = false
    private var applyScheduled = false
    private var exitScheduled = false

    private val clock = BroadcastFrameClock { hasFrameWaiters = true }
    private val composeScope = (CoroutineScope(coroutineContext) + clock)
    private val snapshotHandle = Snapshot.registerGlobalWriteObserver {
        if (!applyScheduled) {
            applyScheduled = true
            composeScope.launch {
                applyScheduled = false
                Snapshot.sendApplyNotifications()
            }
        }
    }
    private val recomposer = Recomposer(coroutineContext)

    /** Manages creating multiple layers in composition as box nodes under surface viewport. */
    private val layers = ComposeSceneContext(
        createLayerNode = createLayerNode,
        removeLayerNode = removeLayerNode,
        applierForNode =  applierForNode,
    )

    val mainLayer = layers.createLayer(recomposer)

    fun start(content: @Composable () -> Unit) {
        !running || return
        running = true

        composeScope.launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        composeScope.launch {
            setContent(content)
            while (!exitScheduled) {
                if (hasFrameWaiters) {
                    hasFrameWaiters = false
                    onFrameAwaiters()
                }
                clock.sendFrame(nanoTime())
                yield()
            }
            running = false
            recomposer.close()
            snapshotHandle.dispose()
            mainLayer.close()
            composeScope.cancel()
        }
    }

    private fun setContent(content: @Composable () -> Unit) {
        hasFrameWaiters = true
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

    override fun close() {
        exitScheduled = true
    }
}
