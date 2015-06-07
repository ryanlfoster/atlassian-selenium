package com.atlassian.pageobjects.elements.mock.clock;

import com.atlassian.pageobjects.elements.query.util.Clock;

import javax.annotation.concurrent.NotThreadSafe;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A {@link com.atlassian.pageobjects.elements.query.util.Clock} implementation that periodically increments the returned value.
 *
 */
@NotThreadSafe
public class PeriodicClock implements Clock
{
    private final long increment;
    private final int periodLength;

    private long current;
    private int currentInPeriod = 0;

    public PeriodicClock(long intitialValue, long increment, int periodLength)
    {
        checkArgument(increment > 0, "increment should be > 0");
        checkArgument(periodLength > 0, "periodLength should be > 0");
        this.current = intitialValue;
        this.increment = increment;
        this.periodLength = periodLength;
    }

    public PeriodicClock(long increment, int periodLength)
    {
        this(0, increment, periodLength);
    }

    public PeriodicClock(long increment)
    {
        this(0, increment, 1);
    }


    public long currentTime()
    {
        long answer = current;
        if (periodFinished())
        {
            current += increment;
        }
        return answer;
    }

    private boolean periodFinished()
    {
        currentInPeriod++;
        if (currentInPeriod == periodLength)
        {
            currentInPeriod = 0;
            return true;
        }
        return false;
    }
}
