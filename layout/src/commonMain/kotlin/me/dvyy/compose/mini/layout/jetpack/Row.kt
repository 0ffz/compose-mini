/*
 * Copyright 2020 The Android Open Source Project
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

package me.dvyy.compose.mini.layout.jetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import me.dvyy.compose.mini.modifier.Modifier

internal val DefaultRowMeasurePolicy: MeasurePolicy = RowColumnMeasurePolicy(
    orientation = LayoutOrientation.Horizontal,
    horizontalArrangement = Arrangement.Start,
    verticalArrangement = null,
    arrangementSpacing = Arrangement.Start.spacing,
    crossAxisAlignment = CrossAxisAlignment.vertical(Alignment.Top),
    crossAxisSize = SizeMode.Wrap,
)

@Composable
fun rowMeasurePolicy(
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
): MeasurePolicy = if (horizontalArrangement == Arrangement.Start && verticalAlignment == Alignment.Top) {
    DefaultRowMeasurePolicy
} else {
    remember(horizontalArrangement, verticalAlignment) {
        RowColumnMeasurePolicy(
            orientation = LayoutOrientation.Horizontal,
            horizontalArrangement = horizontalArrangement,
            verticalArrangement = null,
            arrangementSpacing = horizontalArrangement.spacing,
            crossAxisAlignment = CrossAxisAlignment.vertical(verticalAlignment),
            crossAxisSize = SizeMode.Wrap,
        )
    }
}

/**
 * Scope for the children of [Row].
 */
@LayoutScopeMarker
@Immutable
public interface RowScope {

    /**
     * Size the element's width proportional to its [weight] relative to other weighted sibling
     * elements in the [Row]. The parent will divide the horizontal space remaining after measuring
     * unweighted child elements and distribute it according to this weight.
     * When [fill] is true, the element will be forced to occupy the whole width allocated to it.
     * Otherwise, the element is allowed to be smaller - this will result in [Row] being smaller,
     * as the unused allocated width will not be redistributed to other siblings.
     *
     * @param weight The proportional width to give to this element, as related to the total of
     * all weighted siblings. Must be positive.
     * @param fill When `true`, the element will occupy the whole width allocated.
     */
    @Stable
    public fun Modifier.weight(
        weight: Float,
        fill: Boolean = true,
    ): Modifier

    /**
     * Align the element vertically within the [Row]. This alignment will have priority over
     * the [Row]'s `verticalAlignment` parameter.
     */
    @Stable
    public fun Modifier.align(alignment: Alignment.Vertical): Modifier
}

object RowScopeInstance : RowScope {

    @Stable
    override fun Modifier.weight(weight: Float, fill: Boolean): Modifier {
        require(weight > 0.0) { "invalid weight $weight; must be greater than zero" }
        return this.then(
            LayoutWeightElement(
                // Coerce Float.POSITIVE_INFINITY to Float.MAX_VALUE to avoid errors
                weight = weight.coerceAtMost(Float.MAX_VALUE),
                fill = fill,
            ),
        )
    }

    @Stable
    override fun Modifier.align(alignment: Alignment.Vertical) = this.then(
        VerticalAlignElement(alignment = alignment),
    )
}
