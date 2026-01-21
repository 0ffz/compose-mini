package me.dvyy.compose.mini.layout

val ChildMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val placeables = measurables.map { it.measure(constraints) }
    MeasureResult(placeables.maxOfOrNull { it.width } ?: 0, placeables.maxOfOrNull { it.height } ?: 0) {
        placeables.forEach { it.placeAt(0, 0) }
    }
}