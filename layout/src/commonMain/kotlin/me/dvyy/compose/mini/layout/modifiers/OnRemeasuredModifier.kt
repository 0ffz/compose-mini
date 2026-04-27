package me.dvyy.compose.mini.layout.modifiers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntSize
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

/**
 * Invoked with the size of the modified Compose UI element when the element is first measured or
 * when the size of the element changes.
 *
 * There are no guarantees `onSizeChanged` will not be re-invoked with the same size.
 *
 * Using the `onSizeChanged` size value in a [MutableState] to update layout causes the new size
 * value to be read and the layout to be recomposed in the succeeding frame, resulting in a one
 * frame lag.
 *
 * You can use `onSizeChanged` to affect drawing operations. Use [Layout] or [SubcomposeLayout] to
 * enable the size of one component to affect the size of another.
 *
 * Example usage:
 *
 * @sample androidx.compose.ui.samples.OnSizeChangedSample
 */
@Stable
fun Modifier.onSizeChanged(onSizeChanged: (IntSize) -> Unit) =
    this.then(OnSizeChangedModifier(onSizeChanged = onSizeChanged))

private class OnSizeChangedModifier(private val onSizeChanged: (IntSize) -> Unit) :
    ModifierNodeElement<OnSizeChangedNode>() {
    override fun create(): OnSizeChangedNode = OnSizeChangedNode(onSizeChanged)

    override fun update(node: OnSizeChangedNode) {
        node.update(onSizeChanged)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OnSizeChangedModifier) return false

        return onSizeChanged === other.onSizeChanged
    }

    override fun hashCode(): Int {
        return onSizeChanged.hashCode()
    }
}

internal class OnSizeChangedNode(private var onSizeChanged: (IntSize) -> Unit) :
    Modifier.Node(), LayoutAwareModifierNode {
    // TODO When onSizeChanged changes, we want to invalidate so onRemeasured is called again
//    override val shouldAutoInvalidate: Boolean = true
    private var previousSize = IntSize(Int.MIN_VALUE, Int.MIN_VALUE)

    fun update(onSizeChanged: (IntSize) -> Unit) {
        this.onSizeChanged = onSizeChanged
        // Reset the previous size, so when onSizeChanged changes the new lambda gets invoked,
        // matching previous behavior
        previousSize = IntSize(Int.MIN_VALUE, Int.MIN_VALUE)
    }

    override fun onRemeasured(size: IntSize) {
        if (previousSize != size) {
            onSizeChanged(size)
            previousSize = size
        }
    }
}
