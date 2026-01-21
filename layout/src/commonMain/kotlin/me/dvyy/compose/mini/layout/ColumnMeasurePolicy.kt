package me.dvyy.compose.mini.layout

import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.layout.jetpack.Arrangement
import me.dvyy.compose.mini.layout.jetpack.LayoutDirection

data class ColumnMeasurePolicy(
    private val verticalArrangement: Arrangement.Vertical,
    private val horizontalAlignment: Alignment.Horizontal,
) : RowColumnMeasurePolicy(
    sumHeight = true,
    arrangementSpacing = verticalArrangement.spacing
) {
    override fun placeChildren(placeables: List<Placeable>, width: Int, height: Int): MeasureResult {
        val positions = IntArray(placeables.size)
        verticalArrangement.arrange(
            totalSize = height,
            sizes = placeables.map { it.height }.toIntArray(),
            outPositions = positions
        )
        return MeasureResult(width, height) {
            var childY = 0
            placeables.forEachIndexed { index, child ->
                child.placeAt(horizontalAlignment.align(child.height, height, LayoutDirection.Ltr), positions[index])
                childY += child.height
            }
        }
    }
}
