package com.atlassian.webtest.ui.keys;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Enumeration of {@link SpecialKey}s used within tests.
 *
 * <p>
 * This enumeration also implements the {@link KeySequence} interface for convenient usage.
 *
 * <p>
 * NOTE: Some of the keys may be actually represented as Unicode/ASCII characters, nevertheless they should be
 * supported by implementing frameworks for the convenience of API clients.
 *
 *
 * @see Key
 * @see KeySequence
 */
public enum SpecialKeys implements SpecialKey, KeySequence
{
    ENTER,
    ESC,
    BACKSPACE,
    DELETE,
    SPACE,
    ARROW_LEFT,
    ARROW_RIGHT,
    ARROW_UP,
    ARROW_DOWN;

    // TODO add other if necessary

    private final List<Key> keys;

    private SpecialKeys()
    {
        this.keys = Collections.singletonList((Key)this);
    }


//    @Override
    public List<Key> keys()
    {
        return keys;
    }

//    @Override
    public Set<ModifierKey> withPressed()
    {
        return Collections.emptySet();
    }

    /**
     * Construct key sequence consisting of this special key with given key events to invoke
     *
     * @param events key events to invoke for the created sequence
     * @return new key sequence representing this key
     */
    public KeySequence withEvents(KeyEventType... events)
    {
        return Sequences.keysBuilder(this).keyEvents(events).build();
    }

}
