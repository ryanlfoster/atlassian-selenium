package com.atlassian.webdriver.testing.rule;

import com.atlassian.webdriver.browsers.WebDriverBrowserAutoInstall;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Similarly to {@link com.atlassian.webdriver.testing.rule.WebDriverScreenshotRule}, this rule will
 * dump page source directly to the log upon test failure.
 *
 * @since 2.1
 */
public final class LogPageSourceRule extends TestWatcher
{
    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(LogPageSourceRule.class);

    private static final String ENABLE_LOG_KEY = "atlassian.test.logSourceOnFailure";

    private final Logger logger;
    private final Supplier<? extends WebDriver> webDriver;

    @Inject
    public LogPageSourceRule(WebDriver webDriver, Logger logger)
    {
        this(Suppliers.ofInstance(checkNotNull(webDriver, "webDriver")),logger);
    }

    public LogPageSourceRule(Supplier<? extends WebDriver> webDriver, Logger logger)
    {
        this.webDriver = checkNotNull(webDriver, "webDriver");
        this.logger = checkNotNull(logger, "logger");
    }

    public LogPageSourceRule(Logger logger)
    {
        this(WebDriverBrowserAutoInstall.driverSupplier(), logger);
    }

    public LogPageSourceRule()
    {
        this(DEFAULT_LOGGER);
    }

    /**
     * Returns a copy of this rule, specifying a different logger than the default.
     * @param logger  a {@link Logger}
     * @return  a new LogPageSourceRule based on the current instance
     * @since 2.3
     */
    public LogPageSourceRule logger(Logger logger)
    {
        return new LogPageSourceRule(this.webDriver, logger);
    }

    @Override
    public void failed(final Throwable e, final Description description)
    {
        if (!logPageSourceEnabled())
        {
            return;
        }
        logger.info("----- Test '{}' Failed. ", description.getMethodName());
        logger.info("----- START PAGE SOURCE DUMP\n\n\n{}\n\n\n", webDriver.get().getPageSource());
        logger.info("----- END PAGE SOURCE DUMP");
    }

    private boolean logPageSourceEnabled()
    {
        return Boolean.TRUE.toString().equals(System.getProperty(ENABLE_LOG_KEY));
    }
}
