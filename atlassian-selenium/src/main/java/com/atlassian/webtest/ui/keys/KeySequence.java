package com.atlassian.webtest.ui.keys;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Represents a keyboard input to type into test pages. A key sequence contains one of more keys
 * represented by instances of the {@link Key} class. It may
 * also be meant to type in with some modifier keys pressed down, which is represented by
 * the {@link #withPressed()} method.
 *
 */
public interface KeySequence
{
    /**
     * An immutable list of keys to be typed.
     *
     * @return keys to type into the target test page element
     */
    List<Key> keys();

    /**
     * A set of {@link SpecialKey}s that should be pressed during entering this sequence's {@link #keys()}. If empty,
     * no special keys should be pressed during typing.
     *
     * @return special keys to be pressed. Should never be <code>null</code> 
     */
    Set<ModifierKey> withPressed();

}
