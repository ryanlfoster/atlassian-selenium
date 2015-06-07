package com.atlassian.pageobjects.elements.test.query;

import com.atlassian.pageobjects.elements.mock.MockTimedQuery;
import com.atlassian.pageobjects.elements.mock.clock.StrictMockClock;
import com.atlassian.pageobjects.elements.query.ExpirationHandler;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.query.StaticQuery;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import org.junit.Test;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.ArrayList;
import java.util.List;

import static com.atlassian.pageobjects.elements.query.Poller.by;
import static com.atlassian.pageobjects.elements.query.Poller.byDefaultTimeout;
import static com.atlassian.pageobjects.elements.query.Poller.now;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.atlassian.pageobjects.elements.query.Poller} with various timed queries.
 *
 */
public class TestPollerQueryWaits
{
    private static final int DEFAULT_INTERVAL = 100;

    @Test
    public void queryEqualsAssertionShouldPassForPassingQuery()
    {
        Poller.waitUntil(queryFor("test"), equalTo("test"), now());
        Poller.waitUntil(queryFor("test"), equalTo("test"), byDefaultTimeout());
        Poller.waitUntil(queryFor("test"), equalTo("test"), by(500));
    }

    @Test
    public void queryNotNullAssertionShouldPassForNotNullQuery()
    {
        Poller.waitUntil(queryFor("test"), notNullValue(String.class), now());
        Poller.waitUntil(queryFor("test"), notNullValue(String.class), byDefaultTimeout());
        Poller.waitUntil(queryFor("test"), notNullValue(String.class), by(500));
    }

    @Test
    public void queryIsNullAssertionShouldPassForNullQuery()
    {
        Poller.waitUntil(nullQuery(), nullValue(String.class), now());
        Poller.waitUntil(nullQuery(), nullValue(String.class), byDefaultTimeout());
        Poller.waitUntil(nullQuery(), nullValue(String.class), by(500));
    }


    @Test
    public void queryEqualsAssertionShouldFailForDifferentQueryResultNow()
    {
        try
        {
            Poller.waitUntil(queryFor("something"), equalTo("somethingelse"), now());
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.query.StaticQuery[interval=100,defaultTimeout=500][value=something]>\n"
                    + "Expected: \"somethingelse\" immediately\n"
                    + "Got (last value): \"something\"",
                    e.getMessage());
        }
    }

    @Test
    public void queryEqualsAssertionShouldFailForDifferentQueryResultByDefaultTimeout()
    {
        try
        {
            Poller.waitUntil(forMultipleReturns("something", "somethingdifferent", "somethingmore").returnAll(),
                    equalTo("somethingelse"), byDefaultTimeout());
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.mock.MockTimedQuery[interval=100,defaultTimeout=200]>\n"
                    + "Expected: \"somethingelse\" by 200ms (default timeout)\n"
                    + "Got (last value): \"somethingmore\"",
                    e.getMessage());
        }
    }

    @Test
    public void queryEqualsAssertionShouldFailForDifferentQueryResultByCustomTimeout()
    {
        try
        {
            Poller.waitUntil(forMultipleReturns("something", "somethingdifferent", "somethingmore",
                    "andnowforsomethingcompletelydifferent").returnAll(), equalTo("somethingelse"), by(100));
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.mock.MockTimedQuery[interval=100,defaultTimeout=300]>\n"
                    + "Expected: \"somethingelse\" by 100ms\n"
                    + "Got (last value): \"somethingdifferent\"",
                    e.getMessage());
        }
    }

    @Test
    public void isNotNullAssertionShouldFailForNullQueryNow()
    {
        try
        {
            Poller.waitUntil(nullQuery(), notNullValue(String.class), now());
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.mock.MockTimedQuery[interval=100,defaultTimeout=500]>\n"
                    + "Expected: not null immediately\n"
                    + "Got (last value): null",
                    e.getMessage());
        }
    }

    @Test
    public void isNotNullAssertionShouldFailForNullQueryByDefaultTimeout()
    {
        try
        {
            Poller.waitUntil(forMultipleReturns("one", "two", "three", "four", "five").returnNull(), notNullValue(String.class),
                    byDefaultTimeout());
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.mock.MockTimedQuery[interval=100,defaultTimeout=400]>\n"
                    + "Expected: not null by 400ms (default timeout)\n"
                    + "Got (last value): null",
                    e.getMessage());
        }
    }

    @Test
    public void isNotNullAssertionShouldFailForNullQueryByCustomTimeout()
    {
        try
        {
            Poller.waitUntil(forMultipleReturns("five", "six", "seven", "eight").returnLast(), notNullValue(String.class), by(200));
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.mock.MockTimedQuery[interval=100,"
                    + "defaultTimeout=300]>\n"
                    + "Expected: not null by 200ms\n"
                    + "Got (last value): null",
                    e.getMessage());
        }
    }

    @Test
    public void isNullAssertionShouldFailForNonNullQueryNow()
    {
        try
        {
            Poller.waitUntil(forMultipleReturns("one", "two", "three", "four").returnAll(), nullValue(String.class), now());
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.mock.MockTimedQuery[interval=100,"
                    + "defaultTimeout=300]>\n"
                    + "Expected: null immediately\n"
                    + "Got (last value): \"one\"",
                    e.getMessage());
        }
    }

    @Test
    public void isNullAssertionShouldFailForNonNullQueryByDefaultTimeout()
    {
        try
        {
            Poller.waitUntil(forMultipleReturns("one", "two", "three", "four", "five").returnAll(), nullValue(String.class),
                    byDefaultTimeout());
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.mock.MockTimedQuery[interval=100,"
                    + "defaultTimeout=400]>\n"
                    + "Expected: null by 400ms (default timeout)\n"
                    + "Got (last value): \"five\"",
                    e.getMessage());
        }
    }

    @Test
    public void isNullAssertionShouldFailForNonNullQueryByCustomTimeout()
    {
        try
        {
            Poller.waitUntil(forMultipleReturns("five", "six", "seven", "eight").returnAll(), nullValue(String.class), by(200));
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <com.atlassian.pageobjects.elements.mock.MockTimedQuery[interval=100,"
                    + "defaultTimeout=300]>\n"
                    + "Expected: null by 200ms\n"
                    + "Got (last value): \"seven\"",
                    e.getMessage());
        }
    }

    @Test
    @SuppressWarnings ("unchecked")
    public void exceptionsWithinTimeoutShouldBeIgnored() throws Exception
    {
        String expectedQueryReturn = "all good now";

        // this mock first throws an exception, then returns the expected value
        TimedQuery<String> timedQuery = mock(TimedQuery.class);
        when(timedQuery.defaultTimeout()).thenReturn(5000L);
        when(timedQuery.interval()).thenReturn(10L);
        when(timedQuery.now())
                .thenThrow(new StaleElementReferenceException("big problem! try again?!"))
                .thenReturn(expectedQueryReturn);

        Poller.waitUntil(timedQuery, equalTo(expectedQueryReturn), by(200));
    }

    private TimedQuery<String> queryFor(String value)
    {
        return new StaticQuery<String>(value, 500, DEFAULT_INTERVAL);
    }

    private TimedQuery<String> nullQuery()
    {
        return new MockTimedQuery<String>(500, DEFAULT_INTERVAL, ExpirationHandler.RETURN_NULL).returnNull();
    }

    private MockTimedQuery<String> forMultipleReturns(String... returns)
    {
        StrictMockClock clock = clockFor(returns.length);
        return new MockTimedQuery<String>(clock, clock.last(), DEFAULT_INTERVAL, ExpirationHandler.RETURN_NULL)
                .returnValues(returns);
    }

    private StrictMockClock clockFor(int returnSize)
    {
        final List<Long> times = new ArrayList<Long>(returnSize);
        long time = 0;
        times.add(time); // initial call before loop
        for (int i=0; i<returnSize; i++)
        {
            // each loop in AbstractTimedQuery/Condition calls currentTime() 2 times
            times.add(time);
            times.add(time);
            time += DEFAULT_INTERVAL;
        }
        return new StrictMockClock(times);
    }


}
