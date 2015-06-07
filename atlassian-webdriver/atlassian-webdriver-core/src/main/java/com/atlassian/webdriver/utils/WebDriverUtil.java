package com.atlassian.webdriver.utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @since 2.1
 */
public final class WebDriverUtil
{
    private WebDriverUtil()
    {
        throw new AssertionError(WebDriverUtil.class.getName() + " is not supposed to be instantiated");
    }

    public static boolean isFirefox(@Nonnull WebDriver driver)
    {
        return getUnderlyingDriver(driver) instanceof FirefoxDriver;
    }

    public static boolean isChrome(@Nonnull WebDriver driver)
    {
        return getUnderlyingDriver(driver) instanceof ChromeDriver;
    }

    public static boolean isHtmlUnit(@Nonnull WebDriver driver)
    {
        return getUnderlyingDriver(driver) instanceof HtmlUnitDriver;
    }

    public static boolean isIE(@Nonnull WebDriver driver)
    {
        return getUnderlyingDriver(driver) instanceof InternetExplorerDriver;
    }

    /**
     * Retrieve the underlying {@link WebDriver} instance.
     *
     * @param driver the driver to examine
     * @return the underlying web driver instance that is actually driving the test. This can be the same instance
     * as {@literal driver}, or some other instance that is wrapped by {@literal driver}.
     */
    @Nonnull
    public static WebDriver getUnderlyingDriver(@Nonnull WebDriver driver)
    {
        checkNotNull(driver, "driver");
        while (driver instanceof WrapsDriver)
        {
            driver = WrapsDriver.class.cast(driver).getWrappedDriver();
        }
        return driver;
    }

    /**
     * Check whether the underlying driver is instance of {@literal type}. This method attempts to retrieve the underlying
     * web driver before performing the instance check.
     *
     * @param driver driver to check
     * @param type type to check
     * @return {@literal true}, if the underlying driver is an instance of {@literal type}, in which case
     * {@link #as(WebDriver, Class)} may be safely used on it
     * @see #getUnderlyingDriver(WebDriver)
     */
    public static boolean isInstance(@Nonnull WebDriver driver, @Nonnull Class<?> type)
    {
        return type.isInstance(getUnderlyingDriver(driver));
    }

    /**
     * Get the underlying web driver instance from {@literal driver} as instance of {@literal type}.
     *
     * @param driver driver to examine
     * @param type type to cast to
     * @param <T> type parameter
     * @return the underlying driver instance as T
     * @throws ClassCastException if {@link #isInstance(WebDriver, Class)} for given arguments returns {@literal false}
     * @see #isInstance(org.openqa.selenium.WebDriver, Class)
     */
    public static <T> T as(@Nonnull WebDriver driver, @Nonnull Class<T> type)
    {
        return type.cast(getUnderlyingDriver(driver));
    }

    /**
     * Parses capabilities from input string
     * @since 2.3
     */
    @Nonnull
    public static DesiredCapabilities createCapabilitiesFromString(@Nullable String capabilitiesList) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (!StringUtils.isEmpty(capabilitiesList)){
            for (String cap : capabilitiesList.split(";"))
            {
                String[] nameVal = cap.split("=");
                capabilities.setCapability(nameVal[0], nameVal[1]);
            }
        }
        return capabilities;
    }
}
