package me.dvyy.compose.mini.layout.modifiers

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import me.dvyy.compose.mini.layout.jetpack.Measurable
import me.dvyy.compose.mini.layout.jetpack.MeasureResult
import me.dvyy.compose.mini.layout.jetpack.MeasureScope
import me.dvyy.compose.mini.layout.jetpack.modifier.LayoutModifierNode
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

private class OffsetElement(
    val x: Dp,
    val y: Dp,
    val rtlAware: Boolean,
) : ModifierNodeElement<OffsetNode>() {
    override fun create(): OffsetNode {
        return OffsetNode(x, y, rtlAware)
    }

    override fun update(node: OffsetNode) {
        node.update(x, y, rtlAware)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherModifierElement = other as? OffsetElement ?: return false

        return x == otherModifierElement.x &&
                y == otherModifierElement.y &&
                rtlAware == otherModifierElement.rtlAware
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + rtlAware.hashCode()
        return result
    }

    override fun toString(): String = "OffsetModifierElement(x=$x, y=$y, rtlAware=$rtlAware)"
}

private class OffsetNode(var x: Dp, var y: Dp, var rtlAware: Boolean) :
    LayoutModifierNode, Modifier.Node() {


    fun update(x: Dp, y: Dp, rtlAware: Boolean) {
//        if (this.x != x || this.y != y || this.rtlAware != rtlAware) invalidatePlacement()
        this.x = x
        this.y = y
        this.rtlAware = rtlAware
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.place(this@OffsetNode.x.roundToPx(), this@OffsetNode.y.roundToPx())
        }
    }
}


@Stable
fun Modifier.offset(x: Dp, y: Dp) = then(OffsetElement(x, y, false))