package com.atlassian.webdriver.pageobjects;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.BrowserAware;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.browsers.WebDriverBrowserAutoInstall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation that uses WebDriver to drive the browser.
 */
public class DefaultWebDriverTester implements WebDriverTester, BrowserAware
{
    private final AtlassianWebDriver webDriver;
    private static final Logger log = LoggerFactory.getLogger(DefaultWebDriverTester.class);

    public DefaultWebDriverTester()
    {
        this(WebDriverBrowserAutoInstall.INSTANCE.getDriver());
    }

    public DefaultWebDriverTester(AtlassianWebDriver driver)
    {
        webDriver = driver;
    }

    public AtlassianWebDriver getDriver()
    {
        return webDriver;
    }

    public void gotoUrl(String url)
    {
        log.debug("Navigating to URL: " + url);
        webDriver.get(url);
    }

    @Override
    public Browser getBrowser()
    {
        return webDriver.getBrowser();
    }
}
