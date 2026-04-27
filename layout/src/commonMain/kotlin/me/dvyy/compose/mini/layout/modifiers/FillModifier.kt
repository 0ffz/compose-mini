package me.dvyy.compose.mini.layout.modifiers

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Constraints
import me.dvyy.compose.mini.layout.jetpack.LayoutModifierNode
import me.dvyy.compose.mini.layout.jetpack.Measurable
import me.dvyy.compose.mini.layout.jetpack.MeasureResult
import me.dvyy.compose.mini.layout.jetpack.MeasureScope
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement
import kotlin.math.roundToInt

internal enum class Direction {
    Vertical,
    Horizontal,
    Both,
}

private class FillElement(
    private val direction: Direction,
    private val fraction: Float,
) : ModifierNodeElement<FillNode>() {
    override fun create(): FillNode = FillNode(direction = direction, fraction = fraction)

    override fun update(node: FillNode) {
        node.direction = direction
        node.fraction = fraction
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FillElement) return false

        if (direction != other.direction) return false
        if (fraction != other.fraction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = direction.hashCode()
        result = 31 * result + fraction.hashCode()
        return result
    }

    @Suppress("ModifierFactoryExtensionFunction", "ModifierFactoryReturnType")
    companion object {
        @Stable
        fun width(fraction: Float) =
            FillElement(
                direction = Direction.Horizontal,
                fraction = fraction,
            )

        @Stable
        fun height(fraction: Float) =
            FillElement(
                direction = Direction.Vertical,
                fraction = fraction,
            )

        @Stable
        fun size(fraction: Float) =
            FillElement(
                direction = Direction.Both,
                fraction = fraction,
            )
    }
}

private class FillNode(var direction: Direction, var fraction: Float) :
    LayoutModifierNode, Modifier.Node() {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val minWidth: Int
        val maxWidth: Int
        if (constraints.hasBoundedWidth && direction != Direction.Vertical) {
            val width =
                (constraints.maxWidth * fraction)
                    .roundToInt()
                    .coerceIn(constraints.minWidth, constraints.maxWidth)
            minWidth = width
            maxWidth = width
        } else {
            minWidth = constraints.minWidth
            maxWidth = constraints.maxWidth
        }
        val minHeight: Int
        val maxHeight: Int
        if (constraints.hasBoundedHeight && direction != Direction.Horizontal) {
            val height =
                (constraints.maxHeight * fraction)
                    .roundToInt()
                    .coerceIn(constraints.minHeight, constraints.maxHeight)
            minHeight = height
            maxHeight = height
        } else {
            minHeight = constraints.minHeight
            maxHeight = constraints.maxHeight
        }
        val placeable = measurable.measure(Constraints(minWidth, maxWidth, minHeight, maxHeight))

        return layout(placeable.width, placeable.height) { placeable.place(0, 0) }
    }
}

@Stable
public fun Modifier.fillMaxWidth(fraction: Float = 1f): Modifier {
    require(fraction in 0.0f..1.0f) { "Fraction must be >= 0 and <= 1" }
    return this.then(if (fraction == 1f) FillWholeMaxWidth else FillElement.width(fraction))
}

@Stable
public fun Modifier.fillMaxHeight(fraction: Float = 1f): Modifier {
    require(fraction in 0.0f..1.0f) { "Fraction must be >= 0 and <= 1" }
    return this.then(if (fraction == 1f) FillWholeMaxHeight else FillElement.height(fraction))
}

@Stable
public fun Modifier.fillMaxSize(fraction: Float = 1f): Modifier {
    require(fraction in 0.0f..1.0f) { "Fraction must be >= 0 and <= 1" }
    return this.then(if (fraction == 1f) FillWholeMaxSize else FillElement.size(fraction))
}

private val FillWholeMaxWidth = FillElement.width(1f)
private val FillWholeMaxHeight = FillElement.height(1f)
private val FillWholeMaxSize = FillElement.size(1f)
