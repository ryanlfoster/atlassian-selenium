package com.atlassian.webdriver.browsers;

import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.LifecycleAwareWebDriverGrid;
import com.google.common.base.Supplier;
import org.slf4j.LoggerFactory;

/**
 * Client that supports automatically installing the appropriate browser for the environment
 *
 */
public enum WebDriverBrowserAutoInstall
{
    INSTANCE;

    public AtlassianWebDriver getDriver()
    {
        try {
            return LifecycleAwareWebDriverGrid.getDriver();
        } catch (RuntimeException error) {
            LoggerFactory.getLogger(WebDriverBrowserAutoInstall.class).error("Unable to setup browser", error);
            throw error;
        }
    }

    public static Supplier<AtlassianWebDriver> driverSupplier()
    {
        return new Supplier<AtlassianWebDriver>()
        {
            @Override
            public AtlassianWebDriver get()
            {
                return INSTANCE.getDriver();
            }
        };
    }
}