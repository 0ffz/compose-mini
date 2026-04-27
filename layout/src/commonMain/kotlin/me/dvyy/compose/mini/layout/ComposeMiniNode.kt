package me.dvyy.compose.mini.layout

import me.dvyy.compose.mini.layout.jetpack.MeasurePolicy
import me.dvyy.compose.mini.modifier.Modifier

abstract class ComposeMiniNode {
    var measurePolicy: MeasurePolicy = ChildMeasurePolicy

    abstract fun setModifier(modifier: Modifier)
}