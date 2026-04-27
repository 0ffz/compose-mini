package me.dvyy.compose.mini.layout.modifiers

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import me.dvyy.compose.mini.modifier.DelegatableNode

/**
 * A [androidx.compose.ui.Modifier.Node] which receives various callbacks in response to local
 * changes in layout.
 *
 * This is the [androidx.compose.ui.Modifier.Node] equivalent of
 * [androidx.compose.ui.layout.OnRemeasuredModifier] and
 * [androidx.compose.ui.layout.OnPlacedModifier]
 *
 * Example usage:
 *
 * @sample androidx.compose.ui.samples.OnSizeChangedSample
 * @sample androidx.compose.ui.samples.OnPlaced
 * @sample androidx.compose.ui.samples.LayoutAwareModifierNodeSample
 */
interface LayoutAwareModifierNode : DelegatableNode {
    /**
     * [onPlaced] is called after the parent [LayoutModifier] and parent layout has been placed and
     * before child [LayoutModifier] is placed. This allows child [LayoutModifier] to adjust its own
     * placement based on where the parent is.
     *
     * If you only need to access the current [LayoutCoordinates] at a single point in time from
     * outside this method, use [requireLayoutCoordinates].
     *
     * @see UnplacedAwareModifierNode if you need to also be notified when the node is not placed
     *   anymore.
     */
    fun onPlaced(coordinates: IntOffset) {}

    /**
     * This method is called when the layout content is remeasured. The most common usage is
     * [onSizeChanged].
     */
    fun onRemeasured(size: IntSize) {}
}
