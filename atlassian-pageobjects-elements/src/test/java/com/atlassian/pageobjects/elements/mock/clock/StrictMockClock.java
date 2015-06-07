package com.atlassian.pageobjects.elements.mock.clock;

import com.atlassian.pageobjects.elements.query.util.Clock;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * A mock {@link com.atlassian.pageobjects.elements.query.util.Clock} that will return predefined dates and will
 * throw exception, if called more times than the number of predefined values.
 *
 */
public class StrictMockClock implements Clock
{
    private static final Logger log = LoggerFactory.getLogger(StrictMockClock.class);

    private final List<Long> times;
    private int current = 0;

    public StrictMockClock(List<Long> times)
    {
        this.times = ImmutableList.copyOf(times);
    }

    public StrictMockClock(Long... times)
    {
        this.times = Arrays.asList(times);
    }

    public List<Long> times()
    {
        return ImmutableList.copyOf(times);
    }

    public long first()
    {
        if (times.isEmpty())
        {
            return -1L;
        }
        return times.get(0);
    }

    public long last()
    {
        if (times.isEmpty())
        {
            return -1L;
        }
        return times.get(times.size()-1);
    }

    public long currentTime()
    {
        log.debug("#getCurrentDate: times=" + times + ",current=" + current);        
        if (current >= times.size())
        {
            throw new IllegalStateException("Called too many times, only supports " + times.size() + " invocations");
        }
        return times.get(current++);
    }
}
