package me.dvyy.compose.mini.layout

import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.layout.jetpack.Arrangement
import me.dvyy.compose.mini.layout.jetpack.LayoutDirection

data class RowMeasurePolicy(
    private val horizontalArrangement: Arrangement.Horizontal,
    private val verticalAlignment: Alignment.Vertical,
) : RowColumnMeasurePolicy(
    sumWidth = true,
    arrangementSpacing = horizontalArrangement.spacing
) {
    override fun placeChildren(placeables: List<Placeable>, width: Int, height: Int): MeasureResult {
        val positions = IntArray(placeables.size)
        horizontalArrangement.arrange(
            totalSize = width,
            sizes = placeables.map { it.width }.toIntArray(),
            layoutDirection = LayoutDirection.Ltr,
            outPositions = positions
        )
        return MeasureResult(width, height) {
            placeables.forEachIndexed { index, child ->
                child.placeAt(positions[index], verticalAlignment.align(child.height, height))
            }
        }
    }
}
