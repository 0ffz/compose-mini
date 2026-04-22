package me.dvyy.compose.mini.modifier

interface ModifierWrapper<T : ModifierWrapper<T>> {
    var next: T?
}

class NodeChain<T : ModifierWrapper<T>>(
    val wrap: (Modifier.Node) -> T,
) {
    val empty = wrap(object : Modifier.Node() {})
    var topNode: T = empty
        private set

    //TODO node caching
    fun updateFrom(modifier: Modifier) {
        var newNode: T = empty
        modifier.foldOut(Unit) { modifier, _ ->
            if (modifier is ModifierNodeElement<*>) {
                val node = wrap(modifier.create())
                node.next = newNode
                newNode = node
            }
        }
        topNode = newNode
    }
}
