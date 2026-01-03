package me.dvyy.compose.minimal.runtime

import androidx.compose.runtime.AbstractApplier

class MinimalNodeApplier<T : MinimalNode>(root: T)  : AbstractApplier<T>(root) {
    //TODO support top down insert via a boolean
    override fun insertTopDown(index: Int, instance: T) = Unit

    override fun insertBottomUp(index: Int, instance: T) {
        current.addChild(index, instance)
        check(instance.parent == null) {
            "$instance must not have a parent when being inserted."
        }
        instance.parent = current
    }

    override fun remove(index: Int, count: Int) {
        current.removeChild(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.moveChild(from, to, count)
    }

    override fun onClear() {
        current.clearChildren()
    }
}
