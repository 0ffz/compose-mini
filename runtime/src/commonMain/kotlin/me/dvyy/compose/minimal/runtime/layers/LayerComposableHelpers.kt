package me.dvyy.compose.minimal.runtime.layers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState

/**
 * Creates a [ComposeSceneLayer] bound do this context.
 *
 * Handles disposing of the layer when this composable is disposed.
 */
@Composable
fun rememberComposeSceneLayer(): ComposeSceneLayer<*> {
    val layers = LocalComposeSceneContext.current
    val parentComposition = rememberCompositionContext()
    val layer = remember { layers.createLayer(parentComposition) }
    DisposableEffect(Unit) {
        onDispose {
            layer.close()
        }
    }
    return layer
}

/**
 * Sets the content of a [ComposeSceneLayer], ensuring it updates when the outer composition state changes.
 */
@Composable
fun ComposeSceneLayer<*>.Content(content: @Composable () -> Unit) {
    val currentContent by rememberUpdatedState(content)
    DisposableEffect(this) {
        setContent {
            currentContent()
        }
        onDispose { }
    }
}
