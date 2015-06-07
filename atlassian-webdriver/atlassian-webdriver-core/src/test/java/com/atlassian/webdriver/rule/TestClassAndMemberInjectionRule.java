package com.atlassian.webdriver.rule;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.webdriver.it.pageobjects.SimpleTestedProduct;
import com.atlassian.webdriver.testing.rule.InjectionRules;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test case for a combination of class and instance injection rules.
 *
 * @since 2.1
 * @see com.atlassian.webdriver.testing.rule.InjectionRules
 */
public class TestClassAndMemberInjectionRule
{

    @ClassRule public static TestRule staticInjectionRule = InjectionRules.forTestClass(SimpleTestedProduct.class);

    @Rule public TestRule memberInjectionRule = InjectionRules.forTestInContext(this);

    @Inject
    private static SimpleTestedProduct staticSimpleProduct;

    @Inject
    private static PageBinder staticBinder;

    @Inject
    private SimpleTestedProduct memberSimpleProduct;

    @Inject
    private PageBinder memberBinder;


    @BeforeClass
    public static void checkStaticsInjected()
    {
        assertNotNull("Static product should get injected", staticSimpleProduct);
        assertNotNull("Static binder should get injected", staticBinder);
    }

    @Test
    public void checkMembersInjected()
    {
        assertNotNull("Member product should get injected", memberSimpleProduct);
        assertNotNull("Member binder should get injected", memberBinder);
    }

    @Test
    public void shouldFailIfTryingToRunTestWithMemberRuleExpectingContextThatDoesNotExist()
    {
        // yo, I heard you like JUnit, so I put JUnit in your JUnit so that you can test while you test, yaay!
        final Result result = JUnitCore.runClasses(WrongTest.class);
        assertFalse(result.wasSuccessful());
        assertEquals(1, result.getFailures().size());
        final Failure theFailure = result.getFailures().get(0);
        assertTrue(theFailure.getException() instanceof IllegalStateException);
    }


    /**
     * A test class with member rule 'in-context' by without corresponding class rule. Should fail miserably.
     *
     */
    public static class WrongTest
    {
        @Rule public TestRule memberInjectionRule = InjectionRules.forTestInContext(this);

        @Test
        public void iWillNeverBeCalledOhMy()
        {
            fail("Nooooo I was not supposed to be caaaaalllled!!!");
        }
    }

}
