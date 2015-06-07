package com.atlassian.pageobjects.elements.test.query;

import com.atlassian.pageobjects.elements.query.Conditions;
import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import org.junit.Test;

import static com.atlassian.pageobjects.elements.mock.MockCondition.FALSE;
import static com.atlassian.pageobjects.elements.mock.MockCondition.TRUE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test case for {@link com.atlassian.pageobjects.elements.query.Conditions}.
 */
public class TestConditions
{
    @Test
    public void testNot()
    {
        TimedQuery<Boolean> notFalse = Conditions.not(FALSE);
        assertTrue(notFalse.now());
        TimedQuery<Boolean> notTrue = Conditions.not(TRUE);
        assertFalse(notTrue.now());
    }

    @Test
    public void andTrueOnlyShouldResultInTrue()
    {
        TimedCondition and = Conditions.and(TRUE, TRUE, TRUE);
        assertTrue(and.now());
    }

    @Test
    public void trueAndFalseShouldResultInFalse()
    {
        TimedCondition and = Conditions.and(TRUE, FALSE, TRUE, TRUE);
        assertFalse(and.now());
    }

    @Test
    public void trueOrFalseShouldResultInTrue()
    {
        TimedCondition or = Conditions.or(FALSE, FALSE, TRUE, FALSE);
        assertTrue(or.now());
    }

    @Test
    public void orFalseOnlyShouldResultInFalse()
    {
        TimedCondition or = Conditions.or(FALSE, FALSE, FALSE, FALSE);
        assertFalse(or.now());
    }

    @Test
    public void alwaysTrueShouldAlwaysReturnTrue()
    {
        final TimedCondition alwaysTrue = Conditions.alwaysTrue();
        assertTrue(alwaysTrue.now());
        assertTrue(alwaysTrue.by(100));
        assertTrue(alwaysTrue.now());
    }

    @Test
    public void alwaysFalseShouldAlwaysReturnFalse()
    {
        final TimedCondition alwaysFalse = Conditions.alwaysFalse();
        assertFalse(alwaysFalse.now());
        assertFalse(alwaysFalse.by(100));
        assertFalse(alwaysFalse.now());
    }
}
