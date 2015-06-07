package com.atlassian.webtest.ui.keys;

/**
 * <p>
 * A {@link Key} extension that represents non-character keys commonly used within tests
 * (e.g. Enter, Escape, arrows etc.). 
 *
 * <p>
 * All special keys should be defined by the {@link SpecialKeys} enumeration. Those should be supported by
 * all implementations. Implementations are also free to support any additional keys, by means of providing
 * implementations of this interface. 
 *
 * @see Key
 * @see SpecialKeys
 */
public interface SpecialKey extends Key
{
}
