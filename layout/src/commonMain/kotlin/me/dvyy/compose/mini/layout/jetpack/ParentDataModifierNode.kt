package me.dvyy.compose.mini.layout.jetpack

import androidx.compose.ui.unit.Density
import me.dvyy.compose.mini.modifier.Modifier

/**
 * A [Modifier] that provides data to the parent [Layout]. This can be read from within the
 * the [Layout] during measurement and positioning, via [IntrinsicMeasurable.parentData].
 * The parent data is commonly used to inform the parent how the child [Layout] should be measured
 * and positioned.
 */
public interface ParentDataModifierNode : Modifier.Element {
    /**
     * Provides a parentData, given the [parentData] already provided through the modifier's chain.
     */
    public fun Density.modifyParentData(parentData: Any?): Any?
}
