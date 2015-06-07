package com.atlassian.pageobjects.elements.query;

import com.atlassian.pageobjects.elements.query.util.Clock;
import com.atlassian.pageobjects.elements.query.util.ClockAware;
import com.atlassian.pageobjects.elements.query.util.SystemClock;

import javax.annotation.concurrent.NotThreadSafe;

import static com.atlassian.pageobjects.elements.query.util.Clocks.getClock;


/**
 * Abstract timed condition based on {@link com.atlassian.pageobjects.elements.query.AbstractTimedQuery}. Override
 * {@link #currentValue()} to complete implementation. 
 *
 */
@NotThreadSafe
public abstract class AbstractTimedCondition extends AbstractTimedQuery<Boolean> implements TimedCondition, ClockAware
{
    protected AbstractTimedCondition(Clock clock, long defTimeout, long interval)
    {
        super(clock, defTimeout, interval, ExpirationHandler.RETURN_CURRENT);
    }

    protected AbstractTimedCondition(long defTimeout, long interval)
    {
        this(SystemClock.INSTANCE, defTimeout, interval);
    }

    protected AbstractTimedCondition(PollingQuery other)
    {
        this(getClock(other), other.defaultTimeout(), other.interval());
    }

    @Override
    protected final boolean shouldReturn(Boolean currentEval)
    {
        return currentEval;
    }
}
