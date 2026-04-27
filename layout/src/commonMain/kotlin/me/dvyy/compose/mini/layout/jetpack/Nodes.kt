package me.dvyy.compose.mini.layout.jetpack

import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.NodeKind
import kotlin.jvm.JvmStatic

object Nodes {
    @JvmStatic
    inline val Any
        get() = NodeKind<Modifier.Node>(0b1 shl 0)

    @JvmStatic
    inline val Layout
        get() = NodeKind<LayoutModifierNode>(0b1 shl 1)

}