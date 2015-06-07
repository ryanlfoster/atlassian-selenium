package com.atlassian.pageobjects.elements.test.query;

import com.atlassian.pageobjects.elements.mock.MockCondition;
import com.atlassian.pageobjects.elements.mock.clock.QueryClocks;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link com.atlassian.pageobjects.elements.query.AbstractTimedCondition}.
 *
 */
public class TestAbstractTimedCondition
{
    @Test
    public void testNotSuccessful()
    {
        // interval is 50,
        MockCondition tested = MockCondition.successAfter(9).withClock(QueryClocks.forInterval(MockCondition.DEFAULT_INTERVAL));
        assertFalse(tested.by(400));
    }

    @Test
    public void testNotSuccessfulDefault()
    {
        // interval is 50,
        MockCondition tested = MockCondition.successAfter(11).withClock(QueryClocks.forInterval(MockCondition.DEFAULT_INTERVAL));
        // default is 500
        assertFalse(tested.byDefaultTimeout());
    }

    @Test
    public void testSuccessful()
    {
        // interval is 50,
        MockCondition tested = MockCondition.successAfter(8).withClock(QueryClocks.forInterval(MockCondition.DEFAULT_INTERVAL));
        assertTrue(tested.by(400));
    }

    @Test
    public void testSuccessfulDefault()
    {
        // interval is 50,
        MockCondition tested = MockCondition.successAfter(10).withClock(QueryClocks.forInterval(MockCondition.DEFAULT_INTERVAL));
        // default is 500
        assertTrue(tested.byDefaultTimeout());
    }
}
