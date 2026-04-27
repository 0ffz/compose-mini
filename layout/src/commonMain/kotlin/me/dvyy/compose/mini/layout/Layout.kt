package me.dvyy.compose.mini.layout

import androidx.compose.runtime.*
import me.dvyy.compose.mini.layout.jetpack.MeasurePolicy
import me.dvyy.compose.mini.modifier.Modifier

@Composable
inline fun Layout(
    measurePolicy: MeasurePolicy,
    modifier: Modifier,
    content: @Composable () -> Unit = {},
) {
    val factory = LocalLayoutFactory.current
//    val materializedModifier = currentComposer.materialize(modifier)
    ReusableComposeNode<ComposeMiniNode, Applier<ComposeMiniNode>>(
        factory = factory.create,
        update = {
            set(modifier) { setModifier(it) }
            set(measurePolicy) { this.measurePolicy = it }
            factory.update(this)
        },
        content = content,
    )
}

val LocalLayoutFactory = staticCompositionLocalOf<LayoutFactory> { error("No LayoutFactory provided") }

interface LayoutFactory {
    val create: () -> ComposeMiniNode
    val update: Updater<ComposeMiniNode>.() -> Unit
}