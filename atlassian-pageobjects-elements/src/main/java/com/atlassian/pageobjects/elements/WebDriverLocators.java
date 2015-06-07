package com.atlassian.pageobjects.elements;

import com.atlassian.annotations.Internal;
import com.atlassian.pageobjects.elements.query.AbstractTimedQuery;
import com.atlassian.pageobjects.elements.query.ExpirationHandler;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import com.atlassian.pageobjects.elements.timeout.DefaultTimeouts;
import com.atlassian.webdriver.utils.Check;
import org.hamcrest.StringDescription;
import org.openqa.selenium.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.atlassian.pageobjects.elements.WebDriverLocatable.LocateTimeout.zero;
import static com.atlassian.pageobjects.elements.query.Poller.*;
import static com.atlassian.pageobjects.elements.util.StringConcat.asString;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Creates WebDriveLocatables for different search strategies.
 *
 * @since 2.0
 */
@Internal
public class WebDriverLocators
{
    private WebDriverLocators()
    {
        throw new AssertionError("Do not instantiate " + getClass().getSimpleName());
    }

    /**
     * Creates the root of a WebDriverLocatable list, usually the instance of WebDriver.
     *
     * @return WebDriverLocatable
     */
    @Nonnull
    public static WebDriverLocatable root()
    {
        return new WebDriverRootLocator();
    }

    /**
     * Creates a WebDriverLocatable for a single element in global context.
     *
     * @param locator The locator strategy within the parent. It will be applied in the global search context
     * @return WebDriverLocatable
     */
    @Nonnull
    public static WebDriverLocatable single(By locator)
    {
        return new WebDriverSingleLocator(locator, root());
    }

    /**
     * Creates a WebDriverLocatable for a single element nested within another locatable.
     *
     * @param locator The locator strategy within the parent
     * @param parent The parent locatable
     * @return WebDriverLocatable for a single nested element
     */
    @Nonnull
    public static WebDriverLocatable nested(By locator, WebDriverLocatable parent)
    {
        return new WebDriverSingleLocator(locator, parent);
    }

    /**
     * Creates a WebDriverLocatable for an element included in a list initialized with given element.
     *
     * @param element WebElement
     * @param locator The locator strategy within the parent that will produce a list of matches
     * @param locatorIndex The index within the list of matches to find this element
     * @param parent The locatable for the parent
     * @return WebDriverLocatable
     */
    @Nonnull
    public static WebDriverLocatable list(WebElement element, By locator, int locatorIndex, WebDriverLocatable parent)
    {
        return new WebDriverListLocator(element, locator, locatorIndex, parent);
    }

    @Nonnull
    public static WebDriverLocatable staticElement(WebElement element)
    {
        return new WebDriverStaticLocator(element);
    }

    /**
     * Whether the given {@code WebElement} is stale and needs to be relocated.
     *
     * @param webElement web element to examine
     * @return {@code true} if element reference is stale, {@code false} otherwise
     */
    public static boolean isStale(final WebElement webElement)
    {
        try
        {
            webElement.getTagName();
            return false;
        }
        catch (StaleElementReferenceException ignored)
        {
            return true;
        }
    }

    private static Poller.WaitTimeout withinTimeout(WebDriverLocatable.LocateTimeout timeout)
    {
        return timeout.timeout() > 0 ? by(timeout.timeout()) : now();
    }

    private static long getTimeout(WebDriverLocatable.LocateTimeout timeout) {
        return timeout.timeout() > 0 ? timeout.timeout() : DefaultTimeouts.DEFAULT;
    }

    private static abstract class DeprecatedApiBridge implements WebDriverLocatable
    {
        @Override
        @Nonnull
        public final SearchContext waitUntilLocated(@Nonnull WebDriver driver, int timeoutInSeconds)
                throws NoSuchElementException
        {
            return waitUntilLocated(driver, new LocateTimeout.Builder()
                    .timeout(timeoutInSeconds, TimeUnit.SECONDS)
                    .build());
        }

        @Override
        public final boolean isPresent(@Nonnull WebDriver driver, int timeoutInSeconds)
        {
            return isPresent(driver, new LocateTimeout.Builder()
                    .timeout(timeoutInSeconds, TimeUnit.SECONDS)
                    .build());
        }
    }


    /**
     * A static locator that will blow if the associated WebElement is stale. Not recommended unless no other option.
     *
     */
    private static class WebDriverStaticLocator extends DeprecatedApiBridge implements WebDriverLocatable
    {
        private final WebElement element;

        WebDriverStaticLocator(WebElement element)
        {
            this.element = element;
        }

        @Override
        public By getLocator()
        {
            if (isStale(element))
            {
                return null;
            }
            // happy reverse engineering from ID
            final String id = element.getAttribute("id");
            if (id != null)
            {
                return By.id(id);
            }
            // can't get :(
            return null;
        }

        @Override
        @Nonnull
        public WebDriverLocatable getParent()
        {
            return root();
        }

        @Override
        @Nonnull
        public SearchContext waitUntilLocated(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
                throws NoSuchElementException
        {
            checkNotNull(driver, "driver");
            checkNotNull(timeout, "timeout");
            if (isStale(element))
            {
                throw new NoSuchElementException("WebElement got stale");
            }
            return element;
        }

        @Override
        public boolean isPresent(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
        {
            checkNotNull(driver, "driver");
            checkNotNull(timeout, "timeout");
            return !isStale(element);
        }
    }

    private static class WebDriverRootLocator extends DeprecatedApiBridge implements WebDriverLocatable
    {
        public By getLocator()
        {
            return null;
        }

        public WebDriverLocatable getParent()
        {
            return null;
        }

        @Override
        @Nonnull
        public final SearchContext waitUntilLocated(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
        {
            checkNotNull(driver, "driver");
            checkNotNull(timeout, "timeout");
            return driver;
        }

        @Override
        public final boolean isPresent(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
        {
            checkNotNull(driver, "driver");
            checkNotNull(timeout, "timeout");
            return true;
        }
    }

    private static class WebDriverSingleLocator extends DeprecatedApiBridge implements  WebDriverLocatable
    {
        private WebElement webElement = null;
        private boolean webElementLocated = false;

        private final By locator;
        private final WebDriverLocatable parent;

        public WebDriverSingleLocator(By locator, WebDriverLocatable parent)
        {
            this.locator = checkNotNull(locator, "locator");
            this.parent = checkNotNull(parent, "parent");
        }

        @Nonnull
        public By getLocator()
        {
            return locator;
        }

        @Nonnull
        public WebDriverLocatable getParent()
        {
            return parent;
        }

        @Override
        @Nonnull
        public SearchContext waitUntilLocated(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
        {
            checkNotNull(driver, "driver");
            checkNotNull(timeout, "timeout");
            if(!webElementLocated || WebDriverLocators.isStale(webElement))
            {
                try
                {
                    webElement = waitUntil(queryForSingleElement(driver, timeout), notNullValue(WebElement.class),
                            withinTimeout(timeout));
                }
                catch(AssertionError notFound)
                {
                    throw new NoSuchElementException(new StringDescription()
                            .appendText("Unable to locate element by timeout.")
                            .appendText("\nLocator: ").appendValue(locator)
                            .appendText("\nTimeout: ").appendValue(timeout.timeout()).appendText("ms.")
                            .toString());
                }
                webElementLocated = true;
            }
            return webElement;
        }

        @Override
        public boolean isPresent(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
        {
            checkNotNull(driver, "driver");
            checkNotNull(timeout, "timeout");
            try
            {
                return Check.elementExists(this.locator, parent.waitUntilLocated(driver, timeout));
            }
            catch (NoSuchElementException e)
            {
                // parent cannot be located
                return false;
            }
        }

        @Override
        @Nonnull
        public String toString()
        {
            return asString("WebDriverSingleLocator[locator=", locator, "]");
        }

        private TimedQuery<WebElement> queryForSingleElement(final WebDriver driver, final LocateTimeout timeout)
        {
            return new AbstractTimedQuery<WebElement>(getTimeout(timeout), timeout.pollInterval(), ExpirationHandler.RETURN_NULL)
            {
                @Override
                protected boolean shouldReturn(WebElement currentEval)
                {
                    return true;
                }

                @Override
                protected WebElement currentValue() {
                    // we want the parent to be located and find the element within it
                    if (parent.isPresent(driver, zero()))
                    {
                        try
                        {
                            return parent.waitUntilLocated(driver, zero()).findElement(locator);
                        }
                        catch (NoSuchElementException e)
                        {
                            // element not found within parent
                            return null;
                        }
                    }
                    else
                    {
                        // parent not found
                        return null;
                    }
                }
            };
        }

    }

    private static class WebDriverListLocator extends DeprecatedApiBridge implements  WebDriverLocatable
    {
        private WebElement webElement = null;

        private final By locator;
        private final int locatorIndex;
        private final WebDriverLocatable parent;

        public WebDriverListLocator(WebElement element, By locator, int locatorIndex, WebDriverLocatable parent)
        {
            checkArgument(locatorIndex >= 0, "locator index is negative:" + locatorIndex);
            this.webElement = checkNotNull(element);
            this.locatorIndex = locatorIndex;
            this.locator = checkNotNull(locator, "locator");
            this.parent = checkNotNull(parent, "parent");
        }

        public By getLocator()
        {
            return null;
        }

        public WebDriverLocatable getParent()
        {
            return parent;
        }

        @Override
        @Nonnull
        public SearchContext waitUntilLocated(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
        {
            checkNotNull(driver, "driver");
            checkNotNull(timeout, "timeout");
            if(WebDriverLocators.isStale(webElement))
            {
                try
                {
                    webElement = waitUntil(queryForElementInList(driver, timeout), notNullValue(WebElement.class),
                            withinTimeout(timeout));
                }
                catch (AssertionError notLocated)
                {
                    throw new NoSuchElementException(new StringDescription()
                            .appendText("Unable to locate element in collection.")
                            .appendText("\nLocator: ").appendValue(locator)
                            .appendText("\nLocator Index: ").appendValue(locatorIndex)
                            .toString());
                }
            }
            return webElement;
        }

        @Override
        public boolean isPresent(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
        {
            checkNotNull(driver, "driver");
            checkNotNull(timeout, "timeout");
            SearchContext searchContext = parent.waitUntilLocated(driver, timeout);
            List<WebElement> webElements = searchContext.findElements(this.locator);
            return locatorIndex <= webElements.size() - 1;
        }

        @Override
        public String toString()
        {
            return asString("WebDriverListLocator[locator=", locator, ",index=", locatorIndex, "]");
        }

        private TimedQuery<WebElement> queryForElementInList(final WebDriver driver, final LocateTimeout timeout)
        {
            return new AbstractTimedQuery<WebElement>(getTimeout(timeout), timeout.pollInterval(), ExpirationHandler.RETURN_NULL)
            {
                @Override
                protected boolean shouldReturn(WebElement currentEval) {
                    return true;
                }

                @Override
                protected WebElement currentValue() {
                    // we want the parent to be located and then the child list to be long enough to contain our index!
                    if (parent.isPresent(driver, zero())) {
                        List<WebElement> webElements = parent.waitUntilLocated(driver, zero()).findElements(locator);
                        return locatorIndex < webElements.size() ? webElements.get(locatorIndex) : null;
                    }
                    return null;
                }
            };
        }
    }
}
