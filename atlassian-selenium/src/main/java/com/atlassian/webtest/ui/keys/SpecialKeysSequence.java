package com.atlassian.webtest.ui.keys;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A {@link KeySequence} that contains instances of {@link SpecialKeys}.
 *
 * @since v1.21
 */
public class SpecialKeysSequence extends AbstractKeySequence implements KeySequence
{
    List<SpecialKeys> specialKeys;

    public SpecialKeysSequence(SpecialKeys specialKey, TypeMode typeMode, Collection<ModifierKey> toPress,
            Collection<KeyEventType> events)
    {
        this(Arrays.asList(specialKey), typeMode, toPress, events);
    }

    public SpecialKeysSequence(List<SpecialKeys> specialKeys, TypeMode typeMode, Collection<ModifierKey> toPress,
            Collection<KeyEventType> events)
    {
        super(typeMode, toPress, events);
        this.specialKeys = new ArrayList<SpecialKeys>(specialKeys);
    }

    private List<Key> copy(List<SpecialKeys> specialKeys)
    {
        List<Key> answer = Lists.newArrayList();
        for (SpecialKeys spKey : specialKeys)
        {
            answer.add(spKey);
        }
        return answer;
    }

    public List<Key> keys()
    {
        return Collections.unmodifiableList(copy(specialKeys));
    }

    public List<SpecialKeys> specialKeys()
    {
        return Collections.unmodifiableList(new ArrayList<SpecialKeys>(specialKeys));
    }
}
