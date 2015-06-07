package com.atlassian.pageobjects.elements.mock;

import com.atlassian.pageobjects.elements.query.AbstractTimedCondition;
import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.atlassian.pageobjects.elements.query.util.Clock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mock implementation of {@link com.atlassian.pageobjects.elements.query.TimedCondition}.
 *
 */
public class MockCondition extends AbstractTimedCondition implements TimedCondition
{
    public static TimedCondition TRUE = new MockCondition(true);
    public static TimedCondition FALSE = new MockCondition(false);

    public static MockCondition successAfter(int falseCount)
    {
        boolean[] results = new boolean[falseCount + 1];
        Arrays.fill(results, false);
        results[results.length-1] = true;
        return new MockCondition(results);
    }

    public static final int DEFAULT_INTERVAL = 50;
    public static final long DEFAULT_TIMEOUT = 500;

    private final boolean[] results;
    private final AtomicInteger count = new AtomicInteger();
    private final AtomicInteger callCount = new AtomicInteger();
    private volatile boolean limitReached = false;
    private final ConcurrentLinkedQueue<Long> times = new ConcurrentLinkedQueue<Long>();

    public MockCondition(Clock clock, long interval, boolean... results)
    {
        super(clock, DEFAULT_TIMEOUT, interval);
        this.results = results;
    }

    public MockCondition(long interval, boolean... results)
    {
        super(DEFAULT_TIMEOUT, interval);
        this.results = results;
    }

    public MockCondition(boolean... results)
    {
        this(DEFAULT_INTERVAL, results);
    }

    public MockCondition withClock(Clock clock)
    {
        return new MockCondition(clock, interval, results);
    }

    public Boolean currentValue()
    {
        callCount.incrementAndGet();
        if (limitReached)
        {
            return last();
        }
        else
        {
            return next();
        }
    }

    private boolean last()
    {
        boolean answer = results[results.length-1];
        times.add(System.currentTimeMillis());
        return answer;
    }

    private boolean next()
    {
        int current = count.getAndIncrement();
        if (current >= results.length -1)
        {
            limitReached = true;
        }
        if (current > results.length -1)
        {
            return last();
        }
        boolean answer = results[current];
        times.add(System.currentTimeMillis());
        return answer;
    }

    public int callCount()
    {
        return callCount.get();
    }

    public List<Long> times()
    {
        return new ArrayList<Long>(times);
    }

    @Override
    public String toString()
    {
        return "Mock Condition";
    }
}
