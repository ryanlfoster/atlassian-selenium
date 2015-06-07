package com.atlassian.webtest.ui.keys;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Key sequence consisting out of alphanumeric characters only, capable of being represented as a pure string.
 *
 */
public final class CharacterKeySequence extends AbstractKeySequence
{

    private final String characters;
    private final Iterable<Key> charKeys;

    public CharacterKeySequence(String characters, TypeMode mode, Set<ModifierKey> pressed, Collection<KeyEventType> keyEvents)
    {
        super(mode, pressed, keyEvents);
        this.characters = checkNotNull(characters);
        this.charKeys = initKeys();
    }

    private List<Key> initKeys()
    {
        List<Key> answer = Lists.newArrayList();
        for (char character : characters.toCharArray())
        {
            answer.add(KeySequenceBuilder.keyFor(character));
        }
        return answer;
    }

    public CharacterKeySequence(String characters, TypeMode mode, Set<ModifierKey> pressed)
    {
        this(characters, mode, pressed, KeyEventType.ALL);
    }

    public CharacterKeySequence(String characters, Set<ModifierKey> pressed)
    {
        this(characters, TypeMode.DEFAULT, pressed);
    }

    public CharacterKeySequence(String characters)
    {
        this(characters, EnumSet.noneOf(ModifierKey.class));
    }

    public List<Key> keys()
    {
        return ImmutableList.copyOf(charKeys);
    }

    /**
     * Characters of this sequence as pure string
     *
     * @return characters string
     */
    public String string()
    {
        return characters;
    }

    @Override
    public String toString()
    {
        return string();
    }
}
