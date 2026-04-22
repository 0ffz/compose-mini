package me.dvyy.compose.mini.modifier

/**
 * A [Modifier.Element] which manages an instance of a particular [Modifier.Node] implementation. A
 * given [Modifier.Node] implementation can only be used when a [ModifierNodeElement] which creates
 * and updates that implementation is applied to a Layout.
 *
 * A [ModifierNodeElement] should be very lightweight, and do little more than hold the information
 * necessary to create and maintain an instance of the associated [Modifier.Node] type.
 *
 * @sample androidx.compose.ui.samples.ModifierNodeElementSample
 * @sample androidx.compose.ui.samples.SemanticsModifierNodeSample
 * @see Modifier.Node
 * @see Modifier.Element
 */
abstract class ModifierNodeElement<N : Modifier.Node> : Modifier.Element {
    /**
     * This will be called the first time the modifier is applied to the Layout and it should
     * construct and return the corresponding [Modifier.Node] instance.
     */
    abstract fun create(): N

    /**
     * Called when a modifier is applied to a Layout whose inputs have changed from the previous
     * application. This function will have the current node instance passed in as a parameter, and
     * it is expected that the node will be brought up to date.
     */
    abstract fun update(node: N)

    /**
     * Require hashCode() to be implemented. Using a data class is sufficient. Singletons and
     * modifiers with no parameters may implement this function by returning an arbitrary constant.
     */
    abstract override fun hashCode(): Int

    /**
     * Require equals() to be implemented. Using a data class is sufficient. Singletons may
     * implement this function with referential equality (`this === other`). Modifiers with no
     * inputs may implement this function by checking the type of the other object.
     */
    abstract override fun equals(other: Any?): Boolean
}
