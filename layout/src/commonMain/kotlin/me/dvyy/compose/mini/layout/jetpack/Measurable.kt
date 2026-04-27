package me.dvyy.compose.mini.layout.jetpack

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import me.dvyy.compose.mini.layout.jetpack.Placeable.PlacementScope

/**
 * A part of the composition that can be measured. This represents a layout. The instance should
 * never be stored.
 */
interface Measurable : IntrinsicMeasurable {
    /**
     * Measures the layout with [constraints], returning a [Placeable] layout that has its new size.
     * A [Measurable] can only be measured once inside a layout pass.
     */
    fun measure(constraints: Constraints): Placeable
}

/**
 * Interface holding the size and alignment lines of the measured layout, as well as the children
 * positioning logic. [placeChildren] is the function used for positioning children.
 * [Placeable.placeAt] should be called on children inside [placeChildren]. The alignment lines can
 * be used by the parent layouts to decide layout, and can be queried using the [Placeable.get]
 * operator. Note that alignment lines will be inherited by parent layouts, such that indirect
 * parents will be able to query them as well.
 */
interface MeasureResult {
    /** The measured width of the layout, in pixels. */
    val width: Int

    /** The measured height of the layout, in pixels. */
    val height: Int

    /**
     * A method used to place children of this layout. It may also be used to measure children that
     * were not needed for determining the size of this layout.
     */
    fun placeChildren()
}

object NotMeasured : MeasureResult {
    override val width get() = 0
    override val height get() = 0
    override fun placeChildren() = throw UnsupportedOperationException("Not measured")
}

@Stable
fun interface MeasurePolicy {
    fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult
}

@DslMarker
annotation class MeasureScopeMarker

@MeasureScopeMarker
interface MeasureScope : Density {
    fun layout(
        width: Int,
        height: Int,
        placementBlock: PlacementScope.() -> Unit,
    ): MeasureResult {
        return LayoutResult(this as PlacementScope, width, height, placementBlock)
    }

    private class LayoutResult(
        private val placementScope: PlacementScope,
        override val width: Int,
        override val height: Int,
        private val placementBlock: PlacementScope.() -> Unit,
    ) : MeasureResult {
        override fun placeChildren() = placementScope.placementBlock()
    }
}
