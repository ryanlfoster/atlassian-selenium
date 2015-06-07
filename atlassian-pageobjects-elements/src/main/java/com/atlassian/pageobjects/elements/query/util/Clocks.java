package com.atlassian.pageobjects.elements.query.util;

/**
 * Utilities for clocks.
 *
 */
public final class Clocks
{
    private Clocks()
    {
        throw new AssertionError("Don't instantiate me");
    }

    public static Clock getClock(Object instance)
    {
        if (instance instanceof ClockAware)
        {
            return ((ClockAware) instance).clock();
        }
        else
        {
            return SystemClock.INSTANCE;
        }
    }
}
