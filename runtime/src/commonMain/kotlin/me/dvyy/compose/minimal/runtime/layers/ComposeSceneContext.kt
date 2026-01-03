package me.dvyy.compose.minimal.runtime.layers

import androidx.compose.runtime.Applier
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.compositionLocalOf

/**
 * Context for the current compose scene (bound to a [de.fabmax.kool.modules.compose.surface.ComposableSurface]).
 *
 * Manages multiple layers in composition for one surface.
 * Loosely based on the androidx class of the same name.
 */
class ComposeSceneContext<T>(
    val createLayerNode: () -> T,
    val removeLayerNode: (T) -> Unit,
    val applierForNode: (T) -> Applier<T>,
) {
    private val layers = mutableListOf<ComposeSceneLayer<T>>()

    /**
     * Creates a new layer for composition, this layer is resposible for calling
     * close when it is no longer needed.
     *
     * @param parentContext parent composition context, used for passing composition locals to this layer's subcomposition.
     */
    fun createLayer(
        parentContext: CompositionContext,
    ): ComposeSceneLayer<T> = ComposeSceneLayer(
        owner = this,
        parentContext = parentContext,
        rootNode = createLayerNode()
    ).also { layers.add(it) }

    fun removeLayer(layer: ComposeSceneLayer<T>) {
        layers.remove(layer)
        removeLayerNode(layer.rootNode)
    }
}

val LocalComposeSceneContext = compositionLocalOf<ComposeSceneContext<*>> { error("No CompositionLayers provided") }
