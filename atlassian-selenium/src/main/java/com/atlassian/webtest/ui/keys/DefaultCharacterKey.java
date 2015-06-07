package com.atlassian.webtest.ui.keys;

import com.google.common.base.Preconditions;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * <p>
 * Character implementation of the {@link Key} interface.
 * This class represents all keys that have corresponding 16-bit char representation in Java.
 *
 * <p>
 * NOTE: as this class is based on a single 16-bit Java char, it only supports characters from
 * the Basic Multilingual Plane (BMP) and <b>NOT</b> the <i>supplementary characters<i> as explained
 * in the {@link Character} class documentation.
 *
 */
public class DefaultCharacterKey implements CharacterKey
{
    private final String string;
    private final char codeUnit;

    public DefaultCharacterKey(String string)
    {
        this.string = checkNotNull(string, "string");
        Preconditions.checkArgument(string.length() == 1, "string should be of length 1");
        this.codeUnit = string.charAt(0);
    }

    public DefaultCharacterKey(char codeUnit)
    {
        this.codeUnit = codeUnit;
        this.string = Character.toString(codeUnit);
    }

    public String string()
    {
        return string;
    }

    public char codeUnit()
    {
        return codeUnit;
    }

    @Override
    public String toString()
    {
        return string;
    }
}
