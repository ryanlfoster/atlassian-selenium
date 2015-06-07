package com.atlassian.pageobjects.elements.mock.clock;

import com.atlassian.pageobjects.elements.query.util.Clock;

/**
 * Simple clock that returns a constant value given during construction. Useful for testing date related functions.
 *
 */
public final class ConstantClock implements Clock
{
    private final long currentTime;

    public ConstantClock(long currentDate)
    {
        this.currentTime = currentDate;
    }

    public long currentTime()
    {
        return currentTime;
    }

    @Override
    public String toString()
    {
        return "ConstantClock[time=" + currentTime + "]";
    }
}
