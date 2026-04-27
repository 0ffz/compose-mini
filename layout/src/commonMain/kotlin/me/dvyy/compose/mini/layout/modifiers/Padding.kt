/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.dvyy.compose.mini.layout.modifiers

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.*
import me.dvyy.compose.mini.layout.jetpack.LayoutModifierNode
import me.dvyy.compose.mini.layout.jetpack.Measurable
import me.dvyy.compose.mini.layout.jetpack.MeasureResult
import me.dvyy.compose.mini.layout.jetpack.MeasureScope
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

@Stable
fun Modifier.padding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
): Modifier = this.then(
    PaddingElement(
        start = start,
        top = top,
        end = end,
        bottom = bottom,
    ),
)

@Stable
fun Modifier.padding(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.then(
    PaddingElement(
        start = horizontal,
        top = vertical,
        end = horizontal,
        bottom = vertical,
    ),
)

@Stable
fun Modifier.padding(all: Dp): Modifier = this.then(
    PaddingElement(
        start = all,
        top = all,
        end = all,
        bottom = all,
    ),
)

private data class PaddingElement(
    var start: Dp = 0.dp,
    var top: Dp = 0.dp,
    var end: Dp = 0.dp,
    var bottom: Dp = 0.dp,
) : ModifierNodeElement<PaddingNode>() {
    init {
        require(
            (start.value >= 0f || start.isUnspecified) and
                    (top.value >= 0f || top.isUnspecified) and
                    (end.value >= 0f || end.isUnspecified) and
                    (bottom.value >= 0f || bottom.isUnspecified)
        ) {
            "Padding must be non-negative"
        }
    }

    override fun create(): PaddingNode {
        return PaddingNode(start, top, end, bottom)
    }

    override fun update(node: PaddingNode) {
        node.start = start
        node.top = top
        node.end = end
        node.bottom = bottom
    }
}

private data class PaddingNode(
    var start: Dp = 0.dp,
    var top: Dp = 0.dp,
    var end: Dp = 0.dp,
    var bottom: Dp = 0.dp,
) : LayoutModifierNode, Modifier.Node() {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {

        val horizontal = start.roundToPx() + end.roundToPx()
        val vertical = top.roundToPx() + bottom.roundToPx()

        val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

        val width = constraints.constrainWidth(placeable.width + horizontal)
        val height = constraints.constrainHeight(placeable.height + vertical)
        return layout(width, height) {
            placeable.place(start.roundToPx(), top.roundToPx())
        }
    }
}
