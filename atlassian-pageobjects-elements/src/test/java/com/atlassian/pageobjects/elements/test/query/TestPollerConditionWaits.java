package com.atlassian.pageobjects.elements.test.query;

import com.atlassian.pageobjects.elements.mock.MockCondition;
import com.atlassian.pageobjects.elements.mock.clock.QueryClocks;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.query.TimedCondition;
import org.junit.Test;

import static com.atlassian.pageobjects.elements.query.Poller.by;
import static com.atlassian.pageobjects.elements.query.Poller.byDefaultTimeout;
import static com.atlassian.pageobjects.elements.query.Poller.now;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.is;

/**
 * Test case for {@link com.atlassian.pageobjects.elements.query.Poller} using mock timed conditions.
 *
 */
public class TestPollerConditionWaits
{
    @Test
    public void shouldPassForPassingCondition()
    {
        Poller.waitUntil(passingCondition(), is(true), byDefaultTimeout());
        Poller.waitUntil(passingCondition(), is(true), by(1000));
        Poller.waitUntil(passingCondition(), is(true), now());
    }

    @Test
    public void shouldCallTestedConditionUntilItReturnsTrue()
    {
        MockCondition tested = new MockCondition(false, false, false, true)
                .withClock(QueryClocks.forInterval(MockCondition.DEFAULT_INTERVAL));
        Poller.waitUntil(tested, is(true), by(1000));
        assertEquals(4, tested.callCount());
    }

    @Test
    public void shouldCallTestedConditionUntilItReturnsFalse()
    {
        MockCondition tested = new MockCondition(true, true, true, false)
                .withClock(QueryClocks.forInterval(MockCondition.DEFAULT_INTERVAL));
        Poller.waitUntil(tested, is(false), by(1000));
        assertEquals(4, tested.callCount());
    }


    @Test
    public void shouldProduceMeaningfulErrorMessage()
    {
        try
        {
            Poller.waitUntil(failingCondition(), is(true), byDefaultTimeout());
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <Failing Condition>\n"
                    + "Expected: is <true> by 500ms (default timeout)\n"
                    + "Got (last value): <false>", e.getMessage());
        }
    }

    @Test
    public void shouldProduceMeaningfulErrorMessageForNegatedAssertion()
    {
        try
        {
            Poller.waitUntil(passingCondition(), is(false), byDefaultTimeout());
            throw new IllegalStateException("Should fail");
        }
        catch(AssertionError e)
        {
            assertEquals("Query <Passing Condition>\n"
                    + "Expected: is <false> by 500ms (default timeout)\n"
                    + "Got (last value): <true>", e.getMessage());
        }
    }


    private MockCondition passingCondition()
    {
        return new MockCondition(true)
        {
            public String toString()
            {
                return "Passing Condition";
            }
        };
    }

    private TimedCondition failingCondition()
    {
        return new MockCondition(false)
        {
            public String toString()
            {
                return "Failing Condition";
            }
        };
    }

}
