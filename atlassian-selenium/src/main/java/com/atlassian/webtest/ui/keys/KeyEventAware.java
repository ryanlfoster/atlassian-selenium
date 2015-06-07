package com.atlassian.webtest.ui.keys;

import java.util.Set;

/**
 * An object that is aware of key event types.
 *
 * @since v1.21
 */
public interface KeyEventAware
{
    /**
     * Set of key events associated with this object.
     *
     * @return set of key event types
     */
    Set<KeyEventType> keyEvents();
}
