package me.dvyy.compose.mini.layout

import me.dvyy.compose.mini.layout.jetpack.MeasurePolicy

val ChildMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val placeables = measurables.map { it.measure(constraints) }
    layout(placeables.maxOfOrNull { it.width } ?: 0, placeables.maxOfOrNull { it.height } ?: 0) {
        placeables.forEach { it.place(0, 0) }
    }
}