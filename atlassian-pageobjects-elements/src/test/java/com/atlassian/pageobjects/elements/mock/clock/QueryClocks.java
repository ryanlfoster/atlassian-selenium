package com.atlassian.pageobjects.elements.mock.clock;

import com.atlassian.pageobjects.elements.query.util.Clock;

/**
 * {@link Clock} implementations for testing timed queries and conditions.
 *
 */
public final class QueryClocks
{
    private QueryClocks()
    {
        throw new AssertionError("Don't instantiate me");
    }

    public static Clock forInterval(long interval)
    {
        return new CompositeClock(new ConstantClock(0L)).addClock(2, new PeriodicClock(0L, interval, 2));
    }
}
