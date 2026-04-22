package me.dvyy.compose.mini.layout.jetpack

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

@DslMarker
annotation class PlacementScopeMarker
abstract class Placeable {
    abstract var width: Int
    abstract var height: Int

    protected abstract fun placeAt(x: Int, y: Int)

    protected fun placeAt(offset: IntOffset) = placeAt(offset.x, offset.y)

    val size: IntSize get() = IntSize(width, height)

    @PlacementScopeMarker
    interface PlacementScope : Density {
        val x: Int
        val y: Int
        override val density: Float get() = 1f

        override val fontScale: Float get() = 1f

        fun Placeable.place(x: Int, y: Int) {
            placeAt(this@PlacementScope.x + x, this@PlacementScope.y + y)
        }

        fun Placeable.place(position: IntOffset) {
            placeAt(this@PlacementScope.x + position.x, this@PlacementScope.y + position.y)
        }
    }
}