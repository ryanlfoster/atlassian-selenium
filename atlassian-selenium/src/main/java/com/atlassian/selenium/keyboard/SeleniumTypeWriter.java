package com.atlassian.selenium.keyboard;

import com.atlassian.selenium.AbstractSeleniumDriver;
import com.atlassian.selenium.SeleniumClient;
import com.atlassian.selenium.SeleniumKeyHandler;
import com.atlassian.webtest.ui.keys.CharacterKey;
import com.atlassian.webtest.ui.keys.CharacterKeySequence;
import com.atlassian.webtest.ui.keys.Key;
import com.atlassian.webtest.ui.keys.KeyEventAware;
import com.atlassian.webtest.ui.keys.KeyEventType;
import com.atlassian.webtest.ui.keys.KeySequence;
import com.atlassian.webtest.ui.keys.KeySequenceBuilder;
import com.atlassian.webtest.ui.keys.Sequences;
import com.atlassian.webtest.ui.keys.SpecialKey;
import com.atlassian.webtest.ui.keys.SpecialKeys;
import com.atlassian.webtest.ui.keys.SpecialKeysSequence;
import com.atlassian.webtest.ui.keys.TypeMode;
import com.atlassian.webtest.ui.keys.TypeModeAware;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility to type into Selenium components.
 *
 * @author Dariusz Kordonski
 */
public final class SeleniumTypeWriter extends AbstractSeleniumDriver
{
    private final String target;
    private final TypeMode defaultMode;
    private final Map<TypeMode, Typer> typers = Maps.newHashMap();

    public SeleniumTypeWriter(SeleniumClient client, String target, TypeMode defaultMode)
    {
        super(client);
        this.target = checkNotNull(target, "target");
        validateMode(checkNotNull(defaultMode, "defaultMode"));
        this.defaultMode = defaultMode;
        initTypers();
    }

    private void validateMode(TypeMode defaultMode)
    {
        if (defaultMode == TypeMode.DEFAULT)
        {
            throw new IllegalArgumentException("DEFAULT TypeMode not allowed");
        }
    }

    private void initTypers()
    {
        typers.put(TypeMode.INSERT, new InsertingTyper());
        typers.put(TypeMode.INSERT_WITH_EVENT, new LastCharEventTyper());
        typers.put(TypeMode.TYPE, new FullEventTyper());
    }

    public SeleniumTypeWriter type(KeySequence sequence)
    {
        typers.get(resolveMode(sequence)).type(sequence);
        return this;
    }

    public SeleniumTypeWriter clear()
    {
        client.type(target, "");
        return this;
    }

    private TypeMode resolveMode(KeySequence sequence)
    {
        if (isTypeModeAware(sequence))
        {
            if (asTypeModeAware(sequence).typeMode() == TypeMode.DEFAULT)
            {
                return defaultMode;
            }
            return asTypeModeAware(sequence).typeMode();
        }
        return defaultMode;
    }

    private boolean isTypeModeAware(KeySequence sequence)
    {
        return sequence instanceof TypeModeAware;
    }

    private TypeModeAware asTypeModeAware(KeySequence seq)
    {
        return (TypeModeAware) seq;
    }

    private void setUpModifiers(KeySequence sequence)
    {
        for (SeleniumModifierKey key : seleniumModifiersFor(sequence))
        {
            key.keyDown(client);
        }
    }

    private void tearDownModifiers(KeySequence sequence)
    {
        for (SeleniumModifierKey key : seleniumReversedModifiersFor(sequence))
        {
            key.keyUp(client);
        }
    }

    private Collection<SeleniumModifierKey> seleniumModifiersFor(KeySequence sequence)
    {
        return SeleniumModifierKey.forKeys(sequence.withPressed());
    }

    private Collection<SeleniumModifierKey> seleniumReversedModifiersFor(KeySequence sequence)
    {
        List<SeleniumModifierKey> answer = new ArrayList<SeleniumModifierKey>(seleniumModifiersFor(sequence));
        Collections.reverse(answer);
        return answer;
    }

    private void insert(CharacterKeySequence sequence)
    {
        setUpModifiers(sequence);
        try
        {
            bareInsert(sequence);
        }
        finally
        {
            tearDownModifiers(sequence);
        }
    }

    private void bareInsert(CharacterKeySequence sequence)
    {
        client.type(target, existingValue() + sequence.string());
    }

    private void fullEventType(CharacterKeySequence sequence)
    {
        setUpModifiers(sequence);
        try
        {
            bareFullEventType(sequence);
        }
        finally
        {
            tearDownModifiers(sequence);
        }
    }

    private void bareFullEventType(CharacterKeySequence sequence)
    {
        new SeleniumKeyHandler(client, target, extractKeyEvents(sequence), false).typeWithFullKeyEvents(sequence.string());
    }

    private void lastCharType(CharacterKeySequence sequence)
    {
        if (sequence.string().length() == 0)
        {
            bareInsert(sequence);
            return;
        }
        setUpModifiers(sequence);
        try
        {
            bareLastCharType(sequence);
        }
        finally
        {
            tearDownModifiers(sequence);
        }
    }

    private void bareLastCharType(CharacterKeySequence sequence)
    {
        final String toType = sequence.string();
        bareInsert(withoutLastChar(toType));
        client.simulateKeyPressForCharacter(target, lastChar(toType).string().charAt(0), extractKeyEvents(sequence));
    }

    private CharacterKeySequence withoutLastChar(String toType)
    {
        return asCharacterSequence(Sequences.chars(toType.substring(0, toType.length()-1)));
    }

    private CharacterKeySequence lastChar(String toType)
    {
        return asCharacterSequence(Sequences.chars(toType.substring(toType.length()-1)));
    }

    private void typeSpecialKeys(KeySequence sequence)
    {
        if (sequence instanceof SpecialKeys)
        {
            typeSpecialKey((SpecialKey) sequence, extractKeyEvents(sequence));
        }
        else if (sequence instanceof SpecialKeysSequence)
        {
            final SpecialKeysSequence specialKeysSequence = asSpecialKeysSequence(sequence);
            Collection<KeyEventType> events = specialKeysSequence.keyEvents();
            for (SpecialKeys specialKey : specialKeysSequence.specialKeys())
            {
                typeSpecialKey(specialKey, events);
            }
        }
        else
        {
            throw new AssertionError("Should not invoke .typeSpecialKeys for: " + sequence);
        }
    }

    private void typeSpecialKey(SpecialKey key, Collection<KeyEventType> events)
    {
        client.simulateKeyPressForSpecialKey(target, SeleniumSpecialKeys.forKey(key).keyCode(), events);
    }

    private void insertMixedSequence(KeySequence sequence)
    {
        setUpModifiers(sequence);
        for (KeySequence subSequence : divide(sequence))
        {
            if (subSequence instanceof CharacterKeySequence)
            {
                bareInsert(asCharacterSequence(subSequence));
            }
            else if (isSpecialKeys(subSequence))
            {
                typeSpecialKeys(subSequence);
            }
            else
            {
                throw new AssertionError("WTF is that? " + sequence);
            }
        }
        tearDownModifiers(sequence);
    }

    private void lastCharTypeMixedSequence(KeySequence sequence)
    {
        setUpModifiers(sequence);
        List<KeySequence> subSequences = divide(sequence);
        for (KeySequence subSequence : subSequences.subList(0, subSequences.size()-1))
        {
            if (subSequence instanceof CharacterKeySequence)
            {
                bareInsert(asCharacterSequence(subSequence));
            }
            else if (isSpecialKeys(subSequence))
            {
                typeSpecialKeys(subSequence);
            }
            else
            {
                throw new AssertionError("WTF is that? " + sequence);
            }
        }
        KeySequence last = subSequences.get(subSequences.size()-1);
        if (last instanceof CharacterKeySequence)
        {
            bareLastCharType(asCharacterSequence(last));
        }
        else if (isSpecialKeys(last))
        {
            typeSpecialKeys(last);
        }
        else
        {
            throw new AssertionError("WTF is that? " + sequence);
        }
        tearDownModifiers(sequence);
    }

    private void fullEventTypeMixedSequence(KeySequence sequence)
    {
        setUpModifiers(sequence);
        for (KeySequence subSequence : divide(sequence))
        {
            if (subSequence instanceof CharacterKeySequence)
            {
                bareFullEventType(asCharacterSequence(subSequence));
            }
            else if (isSpecialKeys(subSequence))
            {
                typeSpecialKeys(subSequence);
            }
            else
            {
                throw new AssertionError("WTF is that? " + subSequence);
            }
        }
        tearDownModifiers(sequence);
    }

    private String existingValue()
    {
        final String existingValue = client.getValue(target);
        return existingValue != null ? existingValue : "";
    }

    private List<KeySequence> divide(KeySequence sequence)
    {
        List<KeySequence> answer = new ArrayList<KeySequence>();
        KeySequenceBuilder charBuilder = newBuilderFrom(sequence);
        KeySequenceBuilder keysBuilder = newBuilderFrom(sequence);
        for (Key key : sequence.keys())
        {
            if (key instanceof CharacterKey)
            {
                keysBuilder = addCollected(answer, keysBuilder);
                charBuilder.append(((CharacterKey)key).string());
            }
            else if (key instanceof SpecialKey)
            {
                charBuilder = addCollected(answer, charBuilder);
                keysBuilder.append(key);
            }
            else
            {
                throw new IllegalArgumentException("Not suported: " + key);
            }
        }
        addCollected(answer, charBuilder);
        addCollected(answer, keysBuilder);
        return answer;
    }

    private CharacterKeySequence asCharacterSequence(KeySequence seq)
    {
        return (CharacterKeySequence) seq;
    }

    private SpecialKeys asSppecialKeys(KeySequence seq)
    {
        return (SpecialKeys) seq;
    }

    private KeySequenceBuilder addCollected(List<KeySequence> answer, KeySequenceBuilder charBuilder)
    {
        if (charBuilder.size() > 0)
        {
            answer.add(charBuilder.build());
            return new KeySequenceBuilder().keyEvents(charBuilder.keyEvents());
        }
        return charBuilder;
    }

    private KeySequenceBuilder newBuilderFrom(KeySequence sequence)
    {
        return new KeySequenceBuilder().keyEvents(extractKeyEvents(sequence));
    }

    private Set<KeyEventType> extractKeyEvents(KeySequence sequence)
    {
        if (sequence instanceof KeyEventAware)
        {
            return ((KeyEventAware)sequence).keyEvents();
        }
        return KeyEventType.ALL;
    }

    private boolean isSpecialKeys(KeySequence sequence)
    {
        return sequence instanceof SpecialKeys || sequence instanceof SpecialKeysSequence;
    }

    private SpecialKeysSequence asSpecialKeysSequence(final KeySequence sequence)
    {
        return (SpecialKeysSequence) sequence;
    }

    private interface Typer
    {
        void type(KeySequence sequence);
    }

    private class InsertingTyper implements Typer
    {
        public void type(KeySequence sequence)
        {
            if (sequence instanceof CharacterKeySequence)
            {
                insert((CharacterKeySequence)sequence);
            }
            else if (isSpecialKeys(sequence))
            {
                typeSpecialKeys(sequence);
            }
            else
            {
                insertMixedSequence(sequence);
            }
        }
    }

    private class LastCharEventTyper implements Typer
    {
        public void type(KeySequence sequence)
        {
            if (sequence instanceof CharacterKeySequence)
            {
                lastCharType((CharacterKeySequence)sequence);
            }
            else if (isSpecialKeys(sequence))
            {
                typeSpecialKeys(sequence);
            }
            else
            {
                lastCharTypeMixedSequence(sequence);
            }
        }
    }

    private class FullEventTyper implements Typer
    {
        public void type(KeySequence sequence)
        {
            if (sequence instanceof CharacterKeySequence)
            {
                fullEventType((CharacterKeySequence)sequence);
            }
            else if (isSpecialKeys(sequence))
            {
                typeSpecialKeys(sequence);
            }
            else
            {
                fullEventTypeMixedSequence(sequence);
            }
        }
    }


}
