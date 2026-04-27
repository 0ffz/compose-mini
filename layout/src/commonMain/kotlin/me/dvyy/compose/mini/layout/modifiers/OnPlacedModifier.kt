package me.dvyy.compose.mini.layout.modifiers

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

/**
 * Invoke [onPlaced] after the parent [LayoutModifier] and parent layout has been placed and before
 * child [LayoutModifier] is placed. This allows child [LayoutModifier] to adjust its own placement
 * based on where the parent is.
 *
 * @sample androidx.compose.ui.samples.OnPlaced
 */
@Stable
fun Modifier.onPlaced(onPlaced: (IntOffset) -> Unit) = this then OnPlacedElement(onPlaced)

private class OnPlacedElement(val onPlaced: (IntOffset) -> Unit) :
    ModifierNodeElement<OnPlacedNode>() {
    override fun create() = OnPlacedNode(callback = onPlaced)

    override fun update(node: OnPlacedNode) {
        node.callback = onPlaced
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OnPlacedElement) return false

        if (onPlaced !== other.onPlaced) return false

        return true
    }

    override fun hashCode(): Int {
        return onPlaced.hashCode()
    }
}

internal class OnPlacedNode(var callback: (IntOffset) -> Unit) :
    LayoutAwareModifierNode, Modifier.Node() {

    override fun onPlaced(coordinates: IntOffset) {
        callback(coordinates)
    }
}