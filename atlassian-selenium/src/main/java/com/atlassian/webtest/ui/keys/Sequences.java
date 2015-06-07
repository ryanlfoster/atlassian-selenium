package com.atlassian.webtest.ui.keys;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory of {@link KeySequence} objects.
 *
 * @author Dariusz Kordonski
 */
public final class Sequences
{
    private Sequences() {
        throw new AssertionError("Don't instantiate me");
    }

    private static final KeySequence EMPTY = chars("");

    /**
     * Create a sequence from a string of characters.
     *
     * @param sequence character sequence
     * @return key sequence composed of characters from <tt>sequence</tt>
     */
    public static KeySequence chars(String sequence)
    {
        return new CharacterKeySequence(sequence);
    }


    /**
     * Create a sequence builder from a string of characters.
     *
     * @param sequence character sequence
     * @return key sequence builder initialized with characters from <tt>sequence</tt>
     */
    public static KeySequenceBuilder charsBuilder(String sequence)
    {
        return new KeySequenceBuilder(sequence);
    }

    /**
     * Empty key sequence (useful for clearing out form inputs).
     *
     * @return an empty key sequence
     */
    public static KeySequence empty()
    {
        return EMPTY;
    }


    /**
     * Return key sequence consisting of one or more special keys.
     *
     * @param keys list of special keys in the sequence
     * @return key sequence consisting of the <tt>keys</tt>
     */
    public static KeySequence keys(SpecialKey... keys)
    {
        return new DefaultKeySequence(copy(keys));
    }

    /**
     * Return key sequence builder initialized with <tt>keys</tt>.
     *
     * @param keys list of special keys in the sequence
     * @return new key sequence builder
     */
    public static KeySequenceBuilder keysBuilder(Key... keys)
    {
        return new KeySequenceBuilder(keys);
    }



    private static List<Key> copy(SpecialKey... keys)
    {
        List<Key> answer = new ArrayList<Key>();
        for (SpecialKey key : keys)
        {
            answer.add(key);
        }
        return answer;
    }
}