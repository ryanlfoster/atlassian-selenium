package com.atlassian.webtest.ui.keys;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * For easy and efficient instantiation of {@link KeySequence}s.
 *
 */
public final class KeySequenceBuilder implements KeyEventAware
{
    private static final int ASCII_CACHE_START = 40;
    private static final int ASCII_CACHE_END = 125;

    private static final Map<Character, CharacterKey> KEY_CACHE = createKeyCache();

    private static Map<Character, CharacterKey> createKeyCache()
    {
        Map<Character, CharacterKey> cache = Maps.newHashMap();
        for (char ch= ASCII_CACHE_START; ch<=ASCII_CACHE_END; ch++)
        {
            cache.put(ch, new DefaultCharacterKey(ch));
        }
        return ImmutableMap.copyOf(cache);
    }

    private final List<Key> keys = new ArrayList<Key>();
    private final List<Character> charKeys = new ArrayList<Character>();
    private final EnumSet<ModifierKey> pressed = EnumSet.noneOf(ModifierKey.class);
    private EnumSet<KeyEventType> events = EnumSet.allOf(KeyEventType.class);
    private TypeMode typeMode = TypeMode.DEFAULT;

    public KeySequenceBuilder()
    {
    }
    
    public KeySequenceBuilder(List<Key> keys)
    {
        this.keys.addAll(keys);
    }
    
    public KeySequenceBuilder(Key... keys)
    {
        this(Arrays.asList(keys));
    }

    public KeySequenceBuilder(String characterKeys)
    {
        append(characterKeys);
    }

    public KeySequenceBuilder append(String characterKeys)
    {
        for (char ch : characterKeys.toCharArray())
        {
            keys.add(keyFor(ch));
            charKeys.add(ch);
        }
        return this;
    }

    public KeySequenceBuilder append(Key... keys)
    {
        this.keys.addAll(Arrays.asList(keys));
        return this;
    }

    public KeySequenceBuilder append(Collection<Key> keys)
    {
        this.keys.addAll(keys);
        return this;
    }

    public KeySequenceBuilder withPressed(ModifierKey key)
    {
        pressed.add(key);
        return this;
    }

    public KeySequenceBuilder keyEvents(KeyEventType... events)
    {
        this.events = toEnumSet(Arrays.asList(events));
        return this;
    }

    public KeySequenceBuilder keyEvents(Collection<KeyEventType> events)
    {
        this.events = toEnumSet(events);
        return this;
    }

    public Set<KeyEventType> keyEvents()
    {
        return events.clone();
    }

    private EnumSet<KeyEventType> toEnumSet(Collection<KeyEventType> events)
    {
        return events.isEmpty() ? EnumSet.noneOf(KeyEventType.class) : EnumSet.copyOf(events);
    }

    public KeySequenceBuilder typeMode(TypeMode typeMode)
    {
        this.typeMode = checkNotNull(typeMode);
        return this;
    }

    public int size()
    {
        return keys.size();
    }

    public KeySequence build()
    {
        if (keys.size() == charKeys.size())
        {
            return new CharacterKeySequence(buildString(), typeMode, pressed, events);
        }
        if (specialKeysOnly(keys))
        {
            return new SpecialKeysSequence(toSpecialKeys(), typeMode, pressed, events);
        }
        return new DefaultKeySequence(keys, typeMode, pressed, events);
    }

    private boolean specialKeysOnly(final List<Key> keys)
    {
        for (Key key : keys)
        {
            if (!SpecialKeys.class.isInstance(key))
            {
                return false;
            }
        }
        return true;
    }

    private List<SpecialKeys> toSpecialKeys()
    {
        return Lists.transform(keys, new Function<Key,SpecialKeys>()
        {
            public SpecialKeys apply(Key input)
            {
                return (SpecialKeys) input;
            }
        });
    }

    private String buildString()
    {
        StringBuilder answer = new StringBuilder(charKeys.size());
        for (Character character : charKeys)
        {
            answer.append(character);
        }
        return answer.toString();
    }


    static Key keyFor(char ch)
    {
        Key fromCache = KEY_CACHE.get(ch);
        if (fromCache != null)
        {
            return fromCache;
        }
        else
        {
            return new DefaultCharacterKey(ch);
        }
    }

    

}
