package com.atlassian.webtest.ui.keys;

/**
 * <p>
 * A {@link Key} instance that has a corresponding 16-bit
 * char representation in Java (e.g. 'a', 'b', 'c', '1', '2','3' etc).
 *
 * <p>
 * NOTE: as this interface is based on a single 16-bit Java char, it only supports characters from
 * the Basic Multilingual Plane (BMP) and <b>NOT</b> the <i>supplementary characters<i> as explained
 * in the {@link Character} class documentation.
 *
 */
public interface CharacterKey extends Key
{
    /**
     * String representation of the character entered by this key
     *
     * @return string character representation
     */
    String string();

    /**
     * Char (a code unit) representation of the character entered by this key 
     *
     * @return char character representation
     */
    char codeUnit();
}
