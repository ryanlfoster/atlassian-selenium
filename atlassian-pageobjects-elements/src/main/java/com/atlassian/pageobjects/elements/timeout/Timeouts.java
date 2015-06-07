package com.atlassian.pageobjects.elements.timeout;

/**
 * A configuration components responsible for providing customizable timeouts for various timeout types.
 *
 */
public interface Timeouts
{
    public static final long DEFAULT_INTERVAL = 100L;

    /**
     * Provide timeout (in milliseconds) for a given <tt>timeoutType</tt>.
     *
     * @param timeoutType type of the timeout
     * @return timeout value in milliseconds
     */
    long timeoutFor(TimeoutType timeoutType);
}
