package com.atlassian.webtest.ui.keys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Default implementation of {@link KeySequence}.
 *
 */
public class DefaultKeySequence extends AbstractKeySequence
{

    private final List<Key> keys;


    public DefaultKeySequence(List<Key> keys, ModifierKey... toPress)
    {
        this(keys, Arrays.asList(toPress));
    }

    public DefaultKeySequence(List<Key> keys, Collection<ModifierKey> specialKeys)
    {
        this(keys, TypeMode.DEFAULT, specialKeys);
    }

    public DefaultKeySequence(List<Key> keys, TypeMode mode, Collection<ModifierKey> specialKeys)
    {
        this(keys, mode, specialKeys, KeyEventType.ALL);
    }


    public DefaultKeySequence(List<Key> keys, TypeMode typeMode, Collection<ModifierKey> toPress, Collection<KeyEventType> events)
    {
        super(typeMode, toPress, events);
        this.keys = new ArrayList<Key>(keys);
    }

    public List<Key> keys()
    {
        return Collections.unmodifiableList(keys);
    }

}
