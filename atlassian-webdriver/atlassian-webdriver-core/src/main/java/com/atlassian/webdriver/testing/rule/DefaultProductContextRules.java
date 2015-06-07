package com.atlassian.webdriver.testing.rule;

import com.google.inject.Inject;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * <p/>
 * A version of default, recommended rules that is injectable into the test. Use together with
 * {@link com.atlassian.webdriver.testing.runner.ProductContextRunner}.
 *
 * @since 2.1
 */
public final class DefaultProductContextRules
{
    private DefaultProductContextRules()
    {
        throw new AssertionError("Don't instantiate me");
    }

    public final static class ForClass implements TestRule
     {
         private final RuleChain chain;

         @Inject
         public ForClass(DriverCleanupRule driverCleanupRule)
         {
             this.chain = RuleChain.outerRule(driverCleanupRule);
         }

         @Override
         public Statement apply(Statement base, Description description)
         {
             return chain.apply(base, description);
         }
     }

    public final static class ForMethod implements TestRule
    {
        private final RuleChain chain;

        @Inject
        public ForMethod(IgnoreBrowserRule ignoreBrowserRule,
                         JavaScriptErrorsRule javaScriptErrorsRule,
                         LogPageSourceRule logPageSourceRule,
                         SessionCleanupRule sessionCleanupRule,
                         WebDriverScreenshotRule webDriverScreenshotRule,
                         WindowSizeRule windowSizeRule)
        {
            this.chain = RuleChain.outerRule(ignoreBrowserRule)
                    .around(sessionCleanupRule)
                    .around(windowSizeRule)
                    .around(javaScriptErrorsRule)
                    .around(webDriverScreenshotRule)
                    .around(logPageSourceRule);
        }

        @Override
        public Statement apply(Statement base, Description description)
        {
            return chain.apply(base, description);
        }
    }

}
