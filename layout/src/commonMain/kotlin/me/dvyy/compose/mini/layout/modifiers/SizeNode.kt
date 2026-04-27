package me.dvyy.compose.mini.layout.modifiers

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.*
import me.dvyy.compose.mini.layout.jetpack.LayoutModifierNode
import me.dvyy.compose.mini.layout.jetpack.Measurable
import me.dvyy.compose.mini.layout.jetpack.MeasureResult
import me.dvyy.compose.mini.layout.jetpack.MeasureScope
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

private data class SizeElement(
    private val minWidth: Dp = Dp.Unspecified,
    private val minHeight: Dp = Dp.Unspecified,
    private val maxWidth: Dp = Dp.Unspecified,
    private val maxHeight: Dp = Dp.Unspecified,
) : ModifierNodeElement<SizeNode>() {
    override fun create(): SizeNode = SizeNode(
        minWidth,
        minHeight,
        maxWidth,
        maxHeight,
    )

    override fun update(node: SizeNode) {
        node.minWidth = minWidth
        node.minHeight = minHeight
        node.maxWidth = maxWidth
        node.maxHeight = maxHeight
    }

}

private class SizeNode(
    var minWidth: Dp = Dp.Unspecified,
    var minHeight: Dp = Dp.Unspecified,
    var maxWidth: Dp = Dp.Unspecified,
    var maxHeight: Dp = Dp.Unspecified,
) : LayoutModifierNode, Modifier.Node() {
    private val Density.targetConstraints: Constraints
        get() {
            val maxWidth =
                if (maxWidth.isSpecified) {
                    maxWidth.roundToPx().coerceAtLeast(0)
                } else {
                    Constraints.Infinity
                }
            val maxHeight =
                if (maxHeight.isSpecified) {
                    maxHeight.roundToPx().coerceAtLeast(0)
                } else {
                    Constraints.Infinity
                }
            val minWidth =
                if (minWidth.isSpecified) {
                    minWidth.roundToPx().coerceIn(0, maxWidth).let {
                        if (it != Constraints.Infinity) it else 0
                    }
                } else {
                    0
                }
            val minHeight =
                if (minHeight.isSpecified) {
                    minHeight.roundToPx().coerceIn(0, maxHeight).let {
                        if (it != Constraints.Infinity) it else 0
                    }
                } else {
                    0
                }
            return Constraints(
                minWidth = minWidth,
                minHeight = minHeight,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
            )
        }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val result = measurable.measure(
            constraints.constrain(targetConstraints)
        )
        return layout(result.width, result.height) {
            result.place(0, 0)
        }
    }

}

/**
 * Sets min and max, width and height constraints for this element.
 */
@Stable
fun Modifier.sizeIn(
    minWidth: Dp = 0.dp,
    maxWidth: Dp = Dp.Unspecified,
    minHeight: Dp = 0.dp,
    maxHeight: Dp = Dp.Unspecified,
) = then(SizeElement(minWidth, minHeight, maxWidth, maxHeight))

/** Sets identical min/max width and height constraints for this element. */
@Stable
fun Modifier.size(width: Dp, height: Dp) = sizeIn(width, width, height, height)

/** Sets identical min/max width and height constraints for this element. */
@Stable
fun Modifier.size(size: Dp) = size(size, size)

/** Sets identical min/max width constraints for this element. */
@Stable
fun Modifier.width(width: Dp) = sizeIn(width, width, 0.dp, Dp.Unspecified)

/** Sets identical min/max height constraints for this element. */
@Stable
fun Modifier.height(height: Dp) = sizeIn(0.dp, Dp.Unspecified, height, height)
