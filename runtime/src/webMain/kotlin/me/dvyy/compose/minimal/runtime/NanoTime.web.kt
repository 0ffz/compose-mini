package me.dvyy.compose.minimal.runtime

@Suppress("NOTHING_TO_INLINE")
actual inline fun nanoTime(): Long = window.performance.now().toLong() * 1_000_000