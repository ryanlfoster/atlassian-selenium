package com.atlassian.webdriver;

import com.atlassian.pageobjects.browser.BrowserAware;
import com.atlassian.webdriver.debug.WebDriverDebug;
import com.atlassian.webdriver.utils.Check;
import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.internal.WrapsDriver;

import java.io.File;

/**
 * Represents the web browser, adds common helper methods on top of <tt>WebDriver</tt>.
 *
 * @deprecated scheduled for removal in 3.0. See particular methods for references to replacing functionality.
 */
@Deprecated
public interface AtlassianWebDriver extends WebDriver, JavascriptExecutor, HasInputDevices, BrowserAware, WrapsDriver
{
    /**
     * Gets the underlying <tt>WebDriver</tt>.
     * @return WebDriver
     */
    WebDriver getDriver();

    /**
     * Quits this driver, closing every associated window.
     */
    void quit();

    /**
     * Waits for condition to evaluate to true until default timeout.
     *
     * <p>
     *     If the condition does not become true within default timeout, this method will throw a TimeoutException.
     * </p>
     * @param isTrue Function that evaluates true if waiting is complete.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntil(Function<WebDriver, Boolean> isTrue);

    /**
     * Waits for condition to evaluate to true until given timeout.
     *
     * <p>
     *     If the condition does not become true within given timeout, this method will throw a TimeoutException.
     * </p>
     * @param isTrue Function that evaluates true if waiting is complete.
     * @param timeoutInSeconds Timeout in seconds to wait for condition to return true.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntil(Function<WebDriver, Boolean> isTrue, int timeoutInSeconds);

    /**
     * Writes the source of the last loaded page to the specified file.
     * @param dumpFile File to write the source to.
     * @deprecated use {@link WebDriverDebug#dumpSourceTo(File)}
     */
    void dumpSourceTo(File dumpFile);

    /**
     * Saves screen shot of the browser to the specified file.
     * @param destFile File to save screen shot.
     * @deprecated use {@link WebDriverDebug#takeScreenshotTo(File)}
     */
    void takeScreenshotTo(File destFile);

    /**
     * Wait until element is visible within search context.
     *
     * @param elementLocator Locator strategy for the element.
     * @param context SearchContext to use when locating.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntilElementIsVisibleAt(By elementLocator, SearchContext context);

    /**
     * Wait until element is visible on the page.
     *
     * @param elementLocator Locator strategy for the element.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntilElementIsVisible(By elementLocator);

    /**
     * Wait until element is not visible within a search context.
     *
     * @param elementLocator Locator strategy for the element.
     * @param context SearchContext to use when locating.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntilElementIsNotVisibleAt(By elementLocator, SearchContext context);

    /**
     * Wait until element is not visible on the page.
     *
     * @param elementLocator Locator strategy for the element.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntilElementIsNotVisible(By elementLocator);

    /**
     * Wait until element is present within a search context.
     *
     * @param elementLocator Locator strategy for the element.
     * @param context SearchContext to use when locating.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntilElementIsLocatedAt(By elementLocator, SearchContext context);

    /**
     * Wait until element is present on the page.
     *
     * @param elementLocator Locator strategy for the element.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntilElementIsLocated(By elementLocator);

    /**
     * Wait until element is not present within a search context.
     *
     * @param elementLocator Locator strategy for the element.
     * @param context Parent element to use when locating.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntilElementIsNotLocatedAt(By elementLocator, SearchContext context);

    /**
     * Wait until element is not present on the page.
     *
     * @param elementLocator Locator strategy for the element.
     * @deprecated use {@link com.atlassian.webdriver.utils.element.WebDriverPoller} instead. For more sophisticated
     * polling/waiting toolkit, check the {@code PageElement} API in the atlassian-pageobjects-elements module.
     */
    void waitUntilElementIsNotLocated(By elementLocator);

    /**
     * Whether an element is present on the page.
     *
     * @param locator Locator strategy for the element.
     * @return True if the element is present, false otherwise.
     * @deprecated use {@link Check#elementExists(By, SearchContext)} instead
     */
    boolean elementExists(By locator);

    /**
     * Whether an element is present within a search context.
     *
     * @param locator Locator strategy for the element.
     * @param context SearchContext to use when locating.
     * @return True if element is present, false otherwise.
     * @deprecated use {@link Check#elementExists(By, SearchContext)} instead
     */
    boolean elementExistsAt(By locator, SearchContext context);

    /**
     * Whether an element is visible on the page.
     *
     * @param locator Locator strategy for the element.
     * @return True if element is visible, false otherwise
     * @deprecated use {@link Check#elementIsVisible(By, SearchContext)} instead
     */
    boolean elementIsVisible(By locator);

    /**
     * Whether an element is visible within a given search context.
     *
     * @param locator Locator strategy for the element.
     * @param context SearchContext to use when locating.
     * @return True if element is visible, false otherwise
     * @deprecated use {@link Check#elementIsVisible(By, SearchContext)} instead
     */
    boolean elementIsVisibleAt(By locator, SearchContext context);
}
