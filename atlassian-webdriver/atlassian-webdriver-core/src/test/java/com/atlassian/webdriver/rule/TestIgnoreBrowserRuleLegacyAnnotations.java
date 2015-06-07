package com.atlassian.webdriver.rule;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import com.atlassian.webdriver.testing.annotation.TestBrowser;
import com.atlassian.webdriver.testing.rule.IgnoreBrowserRule;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * <p/>
 * Test case for {@link com.atlassian.webdriver.testing.rule.IgnoreBrowserRule}.
 *
 * <p/>
 * This tests for the legacy annotations support and must be removed once
 * {@link com.atlassian.webdriver.testing.annotation.IgnoreBrowser} and
 * {@link com.atlassian.webdriver.testing.annotation.TestBrowser} are removed.
 *
 * @since 2.1
 */
@RunWith(MockitoJUnitRunner.class)
public class TestIgnoreBrowserRuleLegacyAnnotations
{
    @Mock private Statement statement;

    @Test
    public void shouldIgnoreForAnnotatedTestMethod()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.HTMLUNIT);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockClassWithIgnoreMethods.class, "methodAnnotatedWithIgnoreBrowserHtmlUnit")));
        invoker.invokeSafely();
        assertFailedAssumption(invoker);
    }

    @Test
    public void shouldIgnoreForAnnotatedTestMethodWithMultipleValues()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.ANDROID);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockClassWithIgnoreMethods.class, "methodAnnotatedWithIgnoreBrowserMultipleValues")));
        invoker.invokeSafely();
        assertFailedAssumption(invoker);
    }

    @Test
    public void shouldNotIgnoreForAnnotatedTestMethodIfNotMatching()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.FIREFOX);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockClassWithIgnoreMethods.class, "methodAnnotatedWithIgnoreBrowserHtmlUnit")));
        invoker.invokeSafely();
        assertThat(invoker.isSuccess(), is(true));
    }

    @Test
    public void shouldIgnoreForAnnotatedMethodWithIgnoreAll()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.CHROME);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockClassWithIgnoreMethods.class, "methodAnnotatedWithIgnoreAll")));
        invoker.invokeSafely();
        assertFailedAssumption(invoker);
    }

    @Test
    public void shouldIgnoreForIgnoreBrowserOnClass()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.FIREFOX);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockIgnoreBrowserClass.class, "someMethod")));
        invoker.invokeSafely();
        assertFailedAssumption(invoker);
    }

    @Test
    public void shouldNotIgnoreForAnnotatedClassIfBrowserNotMatching()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.CHROME);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockIgnoreBrowserClass.class, "someMethod")));
        invoker.invokeSafely();
        assertThat(invoker.isSuccess(), is(true));
    }


    @Test
    public void shouldSkipForAnnotatedTestMethod()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.CHROME);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockClassWithRequireMethods.class, "methodAnnotatedWithRequireBrowserHtmlUnit")));
        invoker.invokeSafely();
        assertFailedAssumption(invoker);
    }


    @Test
    public void shouldNotSkipForAnnotatedTestMethodIfMatching()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.HTMLUNIT);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockClassWithRequireMethods.class, "methodAnnotatedWithRequireBrowserHtmlUnit")));
        invoker.invokeSafely();
        assertThat(invoker.isSuccess(), is(true));
    }

    @Test
    public void shouldNeverSkipForAnnotatedMethodWithRequireAll()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.CHROME);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockClassWithRequireMethods.class, "methodAnnotatedWithRequireAll")));
        invoker.invokeSafely();
        assertThat(invoker.isSuccess(), is(true));
    }

    @Test
    public void shouldSkipForRequireBrowserOnClass()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.CHROME);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockRequireBrowserClass.class, "someMethod")));
        invoker.invokeSafely();
        assertFailedAssumption(invoker);
    }

    @Test
    public void shouldNotSkipForRequireBrowserOnClassIfBrowserMatching()
    {
        final IgnoreBrowserRule rule = new IgnoreBrowserRule(Browser.HTMLUNIT);
        final SafeStatementInvoker invoker = new SafeStatementInvoker(rule.apply(statement,
                Descriptions.forTest(MockRequireBrowserClass.class, "someMethod")));
        invoker.invokeSafely();
        assertThat(invoker.isSuccess(), is(true));
    }

    private void assertFailedAssumption(SafeStatementInvoker invoker)
    {
        assertThat(invoker.isSuccess(), is(false));
        assertThat(invoker.getError(), instanceOf(AssumptionViolatedException.class));
    }


    public static class MockClassWithIgnoreMethods
    {
        @Test
        @IgnoreBrowser(value = Browser.HTMLUNIT, reason = "For some reason")
        public void methodAnnotatedWithIgnoreBrowserHtmlUnit() {}

        @Test
        @IgnoreBrowser(value = { Browser.HTMLUNIT, Browser.ANDROID, Browser.FIREFOX }, reason = "For some reason")
        public void methodAnnotatedWithIgnoreBrowserMultipleValues() {}

        @Test
        @IgnoreBrowser(value = Browser.ALL, reason = "For some reason")
        public void methodAnnotatedWithIgnoreAll() {}
    }

    @IgnoreBrowser(value = {Browser.HTMLUNIT, Browser.FIREFOX}, reason = "For some reason")
    public static class MockIgnoreBrowserClass
    {
        @Test
        public void someMethod() {}
    }


    public static class MockClassWithRequireMethods
    {
        @Test
        @TestBrowser("htmlunit")
        public void methodAnnotatedWithRequireBrowserHtmlUnit() {}

        @Test
        @TestBrowser("all")
        public void methodAnnotatedWithRequireAll() {}
    }

    @TestBrowser("htmlunit")
    public static class MockRequireBrowserClass
    {
        @Test
        public void someMethod() {}
    }
}
