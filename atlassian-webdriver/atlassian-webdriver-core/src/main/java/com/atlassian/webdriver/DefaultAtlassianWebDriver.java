package com.atlassian.webdriver;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.webdriver.utils.Check;
import com.atlassian.webdriver.utils.element.ElementIsVisible;
import com.atlassian.webdriver.utils.element.ElementLocated;
import com.atlassian.webdriver.utils.element.ElementNotLocated;
import com.atlassian.webdriver.utils.element.ElementNotVisible;
import com.google.common.base.Function;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Exposes a set of common functions to use.
 */
public class DefaultAtlassianWebDriver implements AtlassianWebDriver
{

    private static final Logger log = LoggerFactory.getLogger(DefaultAtlassianWebDriver.class);

    private static final int WAIT_TIME = 60;

    private final WebDriver driver;
    private final Browser browser;

    @Inject
    public DefaultAtlassianWebDriver(WebDriver driver, Browser browser)
    {
        this.driver = checkNotNull(driver, "driver");
        this.browser = checkNotNull(browser, "browser");
    }

    @Override
    public Browser getBrowser()
    {
        return browser;
    }

    @Override
    public WebDriver getWrappedDriver()
    {
        return driver;
    }

    @Override
    public WebDriver getDriver()
    {
        return driver;
    }

    @Override
    public void quit()
    {
        log.debug("Quitting {}", this);
        if(driver instanceof RemoteWebDriver) {
            RemoteWebDriver remoteDriver = (RemoteWebDriver) driver;
            if(remoteDriver.getSessionId() == null) {
                // already quit - avoid exceptions of trying to do it twice.
                // It's important for quit IE before getting to the shutdown hook, as IE crashes the
                // JVM (in IEDriver.dll) if quit is left until the shutdown hook.
                log.debug("No remote session {}", this);
                return;
            }
        }
        driver.quit();
        log.debug("Quit complete {}", this);
    }

    public void waitUntil(final Function<WebDriver, Boolean> isTrue)
    {
        new WebDriverWait(getDriver(), WAIT_TIME).until(isTrue);
    }

    public void waitUntil(final Function<WebDriver, Boolean> isTrue, int timeoutInSeconds)
    {
        new WebDriverWait(getDriver(), timeoutInSeconds).until(isTrue);
    }

    public void dumpSourceTo(File dumpFile) {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(dumpFile);
            IOUtils.write(getDriver().getPageSource(), fileWriter);
        }
        catch (IOException e1)
        {
            log.info("Error dumping page source to file: " + dumpFile + " : " + e1.getMessage());
        }
        finally
        {
            IOUtils.closeQuietly(fileWriter);
        }
    }

    public void takeScreenshotTo(File destFile) {
        if (getDriver() instanceof TakesScreenshot) {
            TakesScreenshot shotter = (TakesScreenshot) getDriver();
            log.info("Saving screenshot to: " + destFile.getAbsolutePath());
            try
            {
                File screenshot = shotter.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshot, destFile);
            }
            catch (IOException e)
            {
                log.warn("Could not capture screenshot to: " + destFile, e);
            }
            catch (WebDriverException e)
            {
                // This will occur in Chrome for platforms (e.g. linux) where screenshots aren't supported.
                // Webdriver already spams the logs, so only debug log it.
                log.debug("Details: ", e);
            }
        }
        else
        {
            log.info("Driver is not capable of taking screenshots.");
        }
    }

    public void waitUntilElementIsVisibleAt(By elementLocator, SearchContext at)
    {
        waitUntil(new ElementIsVisible(elementLocator, at));
    }

    public void waitUntilElementIsVisible(By elementLocator)
    {
        waitUntilElementIsVisibleAt(elementLocator, null);
    }

    public void waitUntilElementIsNotVisibleAt(By elementLocator, SearchContext at)
    {
        waitUntil(new ElementNotVisible(elementLocator, at));
    }

    public void waitUntilElementIsNotVisible(By elementLocator)
    {
        waitUntilElementIsNotVisibleAt(elementLocator, null);
    }

    public void waitUntilElementIsLocatedAt(By elementLocator, SearchContext at)
    {
        waitUntil(new ElementLocated(elementLocator, at));
    }

    public void waitUntilElementIsLocated(By elementLocator)
    {
        waitUntilElementIsLocatedAt(elementLocator, null);
    }

    public void waitUntilElementIsNotLocatedAt(By elementLocator, SearchContext at)
    {
        waitUntil(new ElementNotLocated(elementLocator, at));
    }

    public void waitUntilElementIsNotLocated(By elementLocator)
    {
        waitUntilElementIsNotLocatedAt(elementLocator, null);
    }

    public boolean elementExists(By locator)
    {
        return Check.elementExists(locator, driver);
    }

    public boolean elementExistsAt(By locator, SearchContext context)
    {
        return Check.elementExists(locator, context);
    }

    public boolean elementIsVisible(By locator)
    {
        return Check.elementIsVisible(locator, driver);
    }

    public boolean elementIsVisibleAt(By locator, SearchContext context)
    {
        return Check.elementIsVisible(locator, context);
    }

    /**
     * WebDriver implementation below
     */
    public void get(final String url)
    {
        driver.get(url);
    }

    public String getCurrentUrl()
    {
        return driver.getCurrentUrl();
    }

    public String getTitle()
    {
        return driver.getTitle();
    }

    public List<WebElement> findElements(final By by)
    {
        return driver.findElements(by);
    }

    public WebElement findElement(final By by)
    {
        return driver.findElement(by);
    }

    public String getPageSource()
    {
        return driver.getPageSource();
    }

    public void close()
    {
        driver.close();
    }

    public Set<String> getWindowHandles()
    {
        return driver.getWindowHandles();
    }

    public String getWindowHandle()
    {
        return driver.getWindowHandle();
    }

    public TargetLocator switchTo()
    {
        return driver.switchTo();
    }

    public Navigation navigate()
    {
        return driver.navigate();
    }

    public Options manage()
    {
        return driver.manage();
    }

    public Object executeScript(final String script)
    {
        return ((JavascriptExecutor)driver).executeScript(script);
    }

    public Object executeScript(final String script, final Object... args)
    {
        return ((JavascriptExecutor)driver).executeScript(script, args);
    }

    public Object executeAsyncScript(String s, Object... objects)
    {
        return ((JavascriptExecutor) driver).executeAsyncScript(s, objects);
    }

    public boolean isJavascriptEnabled()
    {
        return ((HasCapabilities)driver).getCapabilities().isJavascriptEnabled();
    }

    public Keyboard getKeyboard()
    {
        return ((HasInputDevices)driver).getKeyboard();
    }

    public Mouse getMouse()
    {
        return ((HasInputDevices)driver).getMouse();
    }
}