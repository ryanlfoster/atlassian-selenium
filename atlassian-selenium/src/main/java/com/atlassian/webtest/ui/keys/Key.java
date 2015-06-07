package com.atlassian.webtest.ui.keys;

/**
 * <p>
 * Represents a single key stroke into the current page.
 *
 * <p>
 * This is a marker interface that all framework API methods  handling key input should accept. It has two main
 * implementations: character keys and special keys. Creating custom implementations is discouraged, as it will
 * most likely not be handled across different implementations. 
 *
 *
 * @see CharacterKey
 * @see SpecialKey
 * @see KeySequence
 */
public interface Key
{
}
