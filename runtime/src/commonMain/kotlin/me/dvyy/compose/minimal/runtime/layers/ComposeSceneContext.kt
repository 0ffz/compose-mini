package me.dvyy.compose.minimal.runtime.layers

import androidx.compose.runtime.Applier
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.compositionLocalOf

/**
 * Manages multiple layers in composition. Loosely based on the androidx class of the same name.
 *
 * Layers are subcompositions managed via [createLayerNode] and [removeLayerNode].
 * These can interact with the outside world as needed
 * (ex. you may create a node directly under a root ui node, or create entirely new UI windows.)
 *
 * Useful for popup-style composables which should append to a different place in the UI tree, but access
 * composition locals from the current context.
 *
 * @see ComposeSceneLayer
 * @see rememberComposeSceneLayer
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
