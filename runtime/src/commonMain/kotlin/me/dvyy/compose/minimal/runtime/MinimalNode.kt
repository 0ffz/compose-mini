package me.dvyy.compose.minimal.me.dvyy.compose.minimal.runtime

interface MinimalNode {
    var parent: MinimalNode?
    fun addChild(index: Int, child: MinimalNode)
    fun removeChild(index: Int, count: Int)
    fun moveChild(from: Int, to: Int)
    fun clearChildren()
}