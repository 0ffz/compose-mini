package me.dvyy.compose.mini.runtime.layers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext

/**
 * A layer in a composition, created as a subcomposition.
 */
class ComposeSceneLayer<T>(
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
