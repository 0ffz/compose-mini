package me.dvyy.compose.mini.layout

import me.dvyy.compose.mini.layout.jetpack.Measurable
import me.dvyy.compose.mini.layout.jetpack.MeasurePolicy
import me.dvyy.compose.mini.layout.jetpack.MeasureScope
import me.dvyy.compose.mini.layout.jetpack.Placeable
import me.dvyy.compose.mini.modifier.Modifier

abstract class ComposeMiniNode : Placeable(), Measurable, MeasureScope, Placeable.PlacementScope {
    override var width: Int = 0
    override var height: Int = 0
    override var x: Int = 0
    override var y: Int = 0

    var measurePolicy: MeasurePolicy = ChildMeasurePolicy

    abstract fun setModifier(modifier: Modifier)
}