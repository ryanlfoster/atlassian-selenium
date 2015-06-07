package com.atlassian.webdriver.testing.rule;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.RequireBrowser;
import com.atlassian.pageobjects.util.BrowserUtil;
import com.atlassian.webdriver.WebDriverFactory;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import com.atlassian.webdriver.testing.annotation.TestBrowser;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

/**
 * <p/>
 * A rule that allows annotating test methods with the {@link com.atlassian.pageobjects.browser.IgnoreBrowser}
 * annotation and will skip the test if the current running driver
 * is in the list of browsers to ignore.
 *
 * <p/>
 * Also skips tests annotated with {@link com.atlassian.pageobjects.browser.RequireBrowser} if they require a
 * particular browser that is not currently running.
 *
 * <p/>
 * Currently still supports the deprecated annotations {@link IgnoreBrowser} and
 * {@link com.atlassian.webdriver.testing.annotation.TestBrowser} but this will be dropped in the future. Please make
 * sure to move to the equivalents from the pageobjects-api module.
 *
 * <p/>
 * Requires surefire 2.7.2 or higher to show skipped tests in test results.
 *
 * @since 2.1
 */
public class IgnoreBrowserRule implements TestRule
{
    private static final Logger log = LoggerFactory.getLogger(IgnoreBrowserRule.class);

    private final Supplier<Browser> currentBrowserSupplier;

    @Inject
    public IgnoreBrowserRule(Browser currentBrowser)
    {
        this(Suppliers.ofInstance(checkNotNull(currentBrowser, "currentBrowser")));
    }

    public IgnoreBrowserRule(Supplier<Browser> currentBrowserSupplier)
    {
        this.currentBrowserSupplier = checkNotNull(currentBrowserSupplier, "currentBrowserSupplier");
    }

    /**
     *
     * @deprecated use explicit browser/supplier, or get an instance of this rule using the page binder framework. Scheduled
     * for removal in 3.0
     * @see com.atlassian.webdriver.testing.runner.ProductContextRunner
     */
    @Deprecated
    public IgnoreBrowserRule()
    {
        this(BrowserUtil.currentBrowserSupplier());
    }

    public Statement apply(final Statement base, final Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                Class<?> clazz = description.getTestClass();
                Package pkg = clazz.getPackage();
                checkRequiredBrowsers(getRequired(description.getAnnotation(RequireBrowser.class)));
                checkRequiredBrowsers(getRequired(description.isSuite() ? null : clazz.getAnnotation(RequireBrowser.class)));
                checkRequiredBrowsers(getRequired(pkg.getAnnotation(RequireBrowser.class)));
                checkRequiredBrowsers(getRequired(description.getAnnotation(TestBrowser.class)));
                checkRequiredBrowsers(getRequired(description.isSuite() ? null : clazz.getAnnotation(TestBrowser.class)));
                checkRequiredBrowsers(getRequired(pkg.getAnnotation(TestBrowser.class)));

                IgnoredBrowsers.fromLegacyAnnotation(description.getAnnotation(IgnoreBrowser.class))
                        .checkBrowser(currentBrowser(), description);
                IgnoredBrowsers.fromLegacyAnnotation(description.isSuite() ? null : clazz.getAnnotation(IgnoreBrowser.class))
                        .checkBrowser(currentBrowser(), description);
                IgnoredBrowsers.fromAnnotation(description.getAnnotation(com.atlassian.pageobjects.browser.IgnoreBrowser.class))
                        .checkBrowser(currentBrowser(), description);
                IgnoredBrowsers.fromAnnotation(description.isSuite() ? null : clazz.getAnnotation(com.atlassian.pageobjects.browser.IgnoreBrowser.class))
                        .checkBrowser(currentBrowser(), description);
                base.evaluate();
            }

            // support for legacy annotation that will be removed: https://studio.atlassian.com/browse/SELENIUM-187
            private Iterable<Browser> getRequired(TestBrowser testBrowser)
            {
                if (testBrowser != null)
                {
                    return Collections.singleton(WebDriverFactory.getBrowser(testBrowser.value()));
                }
                else
                {
                    return Collections.emptyList();
                }
            }

            private Iterable<Browser> getRequired(RequireBrowser requireBrowser)
            {
                if (requireBrowser != null)
                {
                    return ImmutableList.copyOf(requireBrowser.value());
                }
                else
                {
                    return Collections.emptyList();
                }
            }

            private void checkRequiredBrowsers(Iterable<Browser> required)
            {
                if (Iterables.isEmpty(required) || Iterables.contains(required, Browser.ALL))
                {
                    return;
                }
                Browser latestBrowser = currentBrowser();
                if (!Iterables.contains(required, latestBrowser))
                {
                    log.info(description.getDisplayName() + " ignored, since it requires one of " + required);
                    assumeThat(required, hasItem(latestBrowser));
                }
            }

            private Browser currentBrowser()
            {
                return currentBrowserSupplier.get();
            }
        };
    }

    private static final class IgnoredBrowsers
    {
        public static IgnoredBrowsers fromLegacyAnnotation(IgnoreBrowser legacy)
        {
            if (legacy == null)
            {
                return empty();
            }
            else
            {
                return new IgnoredBrowsers(ImmutableList.copyOf(legacy.value()), legacy.reason());
            }
        }

        public static IgnoredBrowsers fromAnnotation(com.atlassian.pageobjects.browser.IgnoreBrowser ignoreBrowser)
        {
            if (ignoreBrowser == null)
            {
                return empty();
            }
            else
            {
                return new IgnoredBrowsers(ImmutableList.copyOf(ignoreBrowser.value()), ignoreBrowser.reason());
            }
        }

        public static IgnoredBrowsers empty()
        {
            return new IgnoredBrowsers(Collections.<Browser>emptyList(), "");
        }

        private final Iterable<Browser> ignored;
        private final String reason;

        private IgnoredBrowsers(Iterable<Browser> ignored, String reason)
        {
            this.ignored = ignored;
            this.reason = reason;
        }

        boolean isEmpty()
        {
            return Iterables.isEmpty(ignored);
        }

        void checkBrowser(Browser currentBrowser, Description description)
        {
            if (isEmpty())
            {
                return;
            }
            for (Browser browser : ignored)
            {

                if (browser == currentBrowser || browser == Browser.ALL)
                {
                    log.info(description.getDisplayName() + " ignored, reason: " + reason);
                    assumeThat(browser, not(currentBrowser));
                    assumeThat(browser, not(Browser.ALL));
                }
            }
        }
    }
}
