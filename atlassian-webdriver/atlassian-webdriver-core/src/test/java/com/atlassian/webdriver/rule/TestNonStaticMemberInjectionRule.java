package com.atlassian.webdriver.rule;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.webdriver.it.pageobjects.SimpleTestedProduct;
import com.atlassian.webdriver.testing.rule.InjectionRules;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * Test case for instance injection rule without static context.
 *
 * @since 2.1
 * @see com.atlassian.webdriver.testing.rule.InjectionRules
 */
public class TestNonStaticMemberInjectionRule
{

    @Rule public TestRule memberInjectionRule = InjectionRules.forTest(this, SimpleTestedProduct.class);

    @Inject
    private SimpleTestedProduct memberSimpleProduct;

    @Inject
    private PageBinder memberBinder;

    @Test
    public void checkMembersInjected()
    {
        assertNotNull("Member product should get injected", memberSimpleProduct);
        assertNotNull("Member binder should get injected", memberBinder);
    }

}
