package me.dvyy.compose.mini.modifier

import kotlin.jvm.JvmInline

interface ModifierWrapper<T : ModifierWrapper<T>> {
    var node: Modifier.Node
    var next: T?
}

class NodeChain(
) {
    val tail = object: Modifier.Node() {}
    var head: Modifier.Node = tail
        private set

    //TODO node caching
    fun updateFrom(modifier: Modifier) {
        var newNode: Modifier.Node = tail
        modifier.foldOut(Unit) { modifier, _ ->
            if (modifier is ModifierNodeElement<*>) {
                val node = modifier.create()
                node.child = newNode
                newNode.parent = node
                newNode = node
            }
        }
        head = newNode
    }

    inline fun headToTail(block: (Modifier.Node) -> Unit) {
        var node: Modifier.Node? = head
        while (node != null) {
            block(node)
            node = node.child
        }
    }

    inline fun tailToHead(block: (Modifier.Node) -> Unit) {
        var node: Modifier.Node? = tail
        while (node != null) {
            block(node)
            node = node.parent
        }
    }

}
class WrappingChain<T: ModifierWrapper<T>>(
    val nodeChain: NodeChain,
    val create: (Modifier.Node) -> T,
) {
    var tail = create(nodeChain.tail)
    var head = tail
    fun update() {
        var newNode = create(nodeChain.tail)
        nodeChain.tailToHead { node ->
            val wrapped = create(node)
            wrapped.next = newNode
            newNode = wrapped
        }
        head = newNode
    }
}


@Suppress("NOTHING_TO_INLINE")
@JvmInline
value class NodeKind<T>(val mask: Int) {
    inline infix fun or(other: NodeKind<*>): Int = mask or other.mask

    inline infix fun or(other: Int): Int = mask or other
}
