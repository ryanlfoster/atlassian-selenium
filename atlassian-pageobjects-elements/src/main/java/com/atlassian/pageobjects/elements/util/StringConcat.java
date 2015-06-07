package com.atlassian.pageobjects.elements.util;

import javax.annotation.Nullable;

/**
 * Utility for string concatenation.
 *
 */
public final class StringConcat
{
    public static final int EXPECTED_ELEMENT_LENGTH = 8;

    private StringConcat()
    {
        throw new AssertionError("Don't instantiate me");
    }

    /**
     * <p/>
     * Concatenate array of objects into a string in accordance with
     * JLS $15.18.1 (except that primitive values are not accepted
     * by this method other than by autoboxing).
     *
     * <p/>
     * A <code>null</code> passed as the whole <tt>elements</tt> array will
     * result in empty string being returned.
     *
     * @param elements elements to convert
     * @return string resulting from concatenating <tt>elements</tt>
     */
    public static String asString(@Nullable Object... elements)
    {
        // NOTE: don't rename to toString, its not usable for static imports
        if (elements == null) {
            return "";
        }
        int length = elements.length;
        if (length  == 0)
        {
            return "";
        }
        if (length == 1)
        {
            singleAsString(elements[0]);
        }
        StringBuilder answer = new StringBuilder(length * EXPECTED_ELEMENT_LENGTH);
        for (Object elem : elements)
        {
            answer.append(singleAsString(elem));
        }
        return answer.toString();
    }

    private static String singleAsString(Object obj)
    {
        return obj != null ? obj.toString() : "null";
    }
}
