package me.dvyy.compose.minimal.runtime

import kotlin.math.roundToLong

actual fun nanoTime(): Long {
    return (js("performance.now()") as Double * 1_000_000).roundToLong()
}
