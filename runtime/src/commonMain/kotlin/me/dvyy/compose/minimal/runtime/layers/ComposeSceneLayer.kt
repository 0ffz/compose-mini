package me.dvyy.compose.minimal.runtime.layers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext

/**
 * A layer in a [ComposableSurface]'s composition, created as a subcomposition.
 *
 * [UiSurfaceComposition] binds [layerRootNode] to a [de.fabmax.kool.modules.ui2.BoxNode]
 * right under the scene's viewport. This lets us treat this node as part of the same tree in compose
 * (ex. for composition locals, getting parent node information, etc...), while Kool sees it as a node
 * directly under the root node.
 */
class ComposeSceneLayer<T >(
    val owner: ComposeSceneContext<T>,
    val parentContext: CompositionContext,
    val rootNode: T,
) {
    val composition = Composition(owner.applierForNode(rootNode), parentContext)

    fun setContent(content: @Composable () -> Unit) {
        composition.setContent(content)
    }

    fun close() {
        owner.removeLayer(this)
        composition.dispose()
    }
}
