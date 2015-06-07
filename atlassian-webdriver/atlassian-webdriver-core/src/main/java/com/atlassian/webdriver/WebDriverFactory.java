package com.atlassian.webdriver;

import com.atlassian.browsers.BrowserConfig;
import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.util.BrowserUtil;
import com.atlassian.webdriver.browsers.chrome.ChromeBrowser;
import com.atlassian.webdriver.browsers.firefox.FirefoxBrowser;
import com.atlassian.webdriver.browsers.ie.IeBrowser;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks the System property webdriver.browser to see what browser driver to return. Defaults to
 * firefox.
 */
public class WebDriverFactory
{
    private static final Logger log = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final Pattern browserPathPattern = Pattern.compile("^([A-Za-z0-9_.-]+):path=(.*)$");

    private WebDriverFactory() {}

    public static AtlassianWebDriver getDriver()
    {
        return getDriver(null);
    }

    public static String getBrowserProperty()
    {
        return System.getProperty("webdriver.browser", "firefox");
    }

    public static Browser getBrowser()
    {
        String browserProperty = getBrowserProperty();

        return getBrowser(browserProperty);
    }

    public static Browser getBrowser(String browserProperty)
    {
        if (RemoteWebDriverFactory.matches(browserProperty))
        {
            return RemoteWebDriverFactory.getBrowser(browserProperty);
        }

        Matcher matcher = browserPathPattern.matcher(browserProperty);

        if (matcher.matches())
        {
            browserProperty = matcher.group(1);
        }

        return Browser.typeOf(browserProperty);
    }

    public static AtlassianWebDriver getDriver(BrowserConfig browserConfig)
    {
        WebDriver driver;
        String browserPath = null;

        String BROWSER = getBrowserProperty();

        if (RemoteWebDriverFactory.matches(BROWSER))
        {
            log.info("Loading RemoteWebDriverFactory driver " + BROWSER);
            return RemoteWebDriverFactory.getDriver(BROWSER);
        }

        Matcher matcher = browserPathPattern.matcher(BROWSER);

        if (matcher.matches())
        {
            BROWSER = matcher.group(1);
            browserPath = matcher.group(2);
        }

        Browser browserType = Browser.typeOf(BROWSER);

        switch (browserType)
        {
            case FIREFOX:

                if (browserPath == null && browserConfig != null)
                {
                    driver = FirefoxBrowser.getFirefoxDriver(browserConfig);
                }
                else if (browserPath != null)
                {
                    driver = FirefoxBrowser.getFirefoxDriver(browserPath);
                }
                else
                {
                    driver = FirefoxBrowser.getFirefoxDriver();
                }
                break;

            case CHROME:

                if (browserPath == null && browserConfig != null)
                {
                    driver = ChromeBrowser.getChromeDriver(browserConfig);
                }

                else if (browserPath != null)
                {
                    driver = ChromeBrowser.getChromeDriver(browserPath);
                }
                else
                {
                    driver = ChromeBrowser.getChromeDriver();
                }
                break;
            
            case IE:
                driver = IeBrowser.createIeDriver(browserPath, browserConfig);
                break;

            case HTMLUNIT_NOJS:
                driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
                ((HtmlUnitDriver) driver).setJavascriptEnabled(false);
                break;

            case HTMLUNIT:
                driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
                ((HtmlUnitDriver) driver).setJavascriptEnabled(true);
                break;

            case IPHONE_SIMULATOR:
                throw new UnsupportedOperationException("iPhone simulator is no longer a supported Browser Type. " +
                        "Use remote iPhone driver instead");

            case IPHONE:
                // iPhones can only be driven via a RemoteWebDriver
                throw new RuntimeException("iPhone driver must be configured with a url parameter");

            case IPAD:
                // iPads can only be driven via a RemoteWebDriver
                throw new RuntimeException("iPad driver must be configured with a url parameter");

            case ANDROID_EMULATOR:
                throw new UnsupportedOperationException("Android emulator is no longer a supported Browser Type. " +
                        "Use remote Android driver instead");

            case ANDROID:
                // Android can only be driven via a RemoteWebDriver
                throw new RuntimeException("Android driver must be configured with a url parameter");

            case SAFARI:
                throw new UnsupportedOperationException("Safari is not a supported Browser Type");

            case OPERA:
                throw new UnsupportedOperationException("Opera is not a supported Browser Type");

            default:
                log.error("Unknown browser: {}, defaulting to firefox.", BROWSER);
                browserType = Browser.FIREFOX;
                driver = FirefoxBrowser.getFirefoxDriver();
        }

        BrowserUtil.setCurrentBrowser(browserType);

        return new DefaultAtlassianWebDriver(driver, browserType);
    }
}
