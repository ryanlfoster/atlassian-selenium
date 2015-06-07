package com.atlassian.webdriver;

import com.atlassian.browsers.BrowserConfig;
import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.util.BrowserUtil;
import com.atlassian.webdriver.browsers.firefox.FirefoxBrowser;
import com.atlassian.webdriver.utils.WebDriverUtil;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles connecting to a web driver server remotely.
 *
 * @since 2.1.0
 */
class RemoteWebDriverFactory
{
    private static final Logger log = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final Pattern remoteBrowserPathPattern = Pattern.compile("^([A-Za-z0-9_.-]+):url=(.*)$");

    public static boolean matches(String browserProperty)
    {
        return remoteBrowserPathPattern.matcher(browserProperty).matches();
    }

    public static Browser getBrowser(String browserProperty)
    {
        Matcher matcher = remoteBrowserPathPattern.matcher(browserProperty);

        if (!matcher.matches())
        {
            // this shouldn't happen anyway.
            log.warn("Cannot find a url to connect to with a RemoteWebDriver. Falling back to using a local FirefoxDriver instead");
            return Browser.FIREFOX;
        }
        Browser browserType = Browser.typeOf(matcher.group(1));
        return browserType;
    }

    public static AtlassianWebDriver getDriver(String browserProperty)
    {
        return getDriver(browserProperty, null);
    }

    public static AtlassianWebDriver getDriver(String browserProperty, BrowserConfig browserConfig)
    {
        Matcher matcher = remoteBrowserPathPattern.matcher(browserProperty);

        if (!matcher.matches())
        {
            // this shouldn't happen anyway.
            log.warn("Cannot find a url to connect to with a RemoteWebDriver. Falling back to using a local FirefoxDriver instead");
            return new DefaultAtlassianWebDriver(FirefoxBrowser.getFirefoxDriver(), Browser.FIREFOX);
        }

        Browser browserType = Browser.typeOf(matcher.group(1));
        final String serverUrlString = matcher.group(2);
        URL serverUrl = null;
        try
        {
            StringBuilder sb = new StringBuilder(serverUrlString);
            if (!serverUrlString.contains("wd/hub"))
            {
                if (!serverUrlString.endsWith("/")) {
                    sb.append("/");
                }

                sb.append("wd/hub");
            }

            serverUrl = new URL(sb.toString());
        }
        catch (MalformedURLException e)
        {
            log.error("Invalid url provided: '{}', defaulting to http://localhost:4444.", serverUrlString);
            try
            {
                serverUrl = new URL("http://localhost:4444/wd/hub");
            }
            catch (MalformedURLException e1)
            {
                // this shouldn't happen ignore
            }
        }

        final DesiredCapabilities capabilities;
        switch (browserType)
        {
            case FIREFOX:
                capabilities = DesiredCapabilities.firefox();
                break;
            case CHROME:
                capabilities = DesiredCapabilities.chrome();
                break;
            case IE:
                capabilities = DesiredCapabilities.internetExplorer();
                break;
            case HTMLUNIT:
                capabilities = DesiredCapabilities.htmlUnit();
                break;
            case IPHONE:
                // iPhone must have iWebDriver app installed
                capabilities = DesiredCapabilities.iphone();
                break;
            case IPAD:
                // iPad must have iWebDriver app installed
                capabilities = DesiredCapabilities.ipad();
                break;
            case ANDROID:
                // android must have android-server.apk installed and running
                capabilities = DesiredCapabilities.android();
                break;
            case SAFARI:
                throw new UnsupportedOperationException("Safari is not a supported Browser Type");
            case OPERA:
                throw new UnsupportedOperationException("Opera is not a supported Browser Type");
            default:
                log.error("Unknown browser: {}, defaulting to firefox.", browserType);
                capabilities = DesiredCapabilities.firefox();
        }

        final String capabilitiesStr = System.getProperty("webdriver.capabilities");
        log.info("Loading custom capabilities " + capabilitiesStr);
        DesiredCapabilities customCapabilities = WebDriverUtil.createCapabilitiesFromString(capabilitiesStr);
        capabilities.merge(customCapabilities);

        BrowserUtil.setCurrentBrowser(browserType);

        RemoteWebDriver driver = new RemoteWebDriver(serverUrl, capabilities);

        return new DefaultAtlassianWebDriver(driver, browserType);
    }
}
