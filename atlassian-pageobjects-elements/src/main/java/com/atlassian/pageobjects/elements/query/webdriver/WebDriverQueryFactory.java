package com.atlassian.pageobjects.elements.query.webdriver;

import com.atlassian.annotations.Internal;
import com.atlassian.pageobjects.elements.WebDriverLocatable;
import com.atlassian.pageobjects.elements.query.*;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.atlassian.pageobjects.elements.timeout.Timeouts;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.google.common.base.Supplier;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import java.util.Set;

import static com.atlassian.pageobjects.elements.timeout.TimeoutType.DEFAULT;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Creates various WebDriver-based queries.
 */
@NotThreadSafe
@Internal
public class WebDriverQueryFactory
{
    private final WebDriverLocatable locatable;

    @Inject
    private Timeouts timeouts;

    @Inject
    private AtlassianWebDriver webDriver;

    public WebDriverQueryFactory(WebDriverLocatable locatable)
    {
        this.locatable = checkNotNull(locatable);
    }

    public WebDriverQueryFactory(WebDriverLocatable locatable, Timeouts timeouts, AtlassianWebDriver webDriver)
    {
        this.locatable = checkNotNull(locatable);
        this.timeouts = timeouts;
        this.webDriver = webDriver;
    }

    private long interval = -1L;

    private long interval()
    {
        if (interval == -1)
        {
            interval = timeouts.timeoutFor(TimeoutType.EVALUATION_INTERVAL);
            checkState(interval > 0);
        }
        return interval;
    }

    public TimedCondition isPresent(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedCondition(locatable, webDriver, WebDriverQueryFunctions.isPresent(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition isPresent()
    {
        return isPresent(DEFAULT);
    }

    public TimedCondition isVisible(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedCondition(locatable, webDriver, WebDriverQueryFunctions.isVisible(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition isVisible()
    {
        return isVisible(DEFAULT);
    }

    public TimedCondition isEnabled(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedCondition(locatable, webDriver, WebDriverQueryFunctions.isEnabled(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition isEnabled()
    {
        return isEnabled(DEFAULT);
    }

    public TimedCondition isSelected(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedCondition(locatable, webDriver, WebDriverQueryFunctions.isSelected(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition isSelected()
    {
        return isSelected(DEFAULT);
    }

    public TimedQuery<String> getText(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedQuery<String>(locatable, webDriver, WebDriverQueryFunctions.getText(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedQuery<String> getText()
    {
        return getText(DEFAULT);
    }

    public TimedQuery<String> getValue(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedQuery<String>(locatable, webDriver, WebDriverQueryFunctions.getValue(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedQuery<String> getValue()
    {
        return getValue(DEFAULT);
    }

    public TimedCondition hasAttribute(String attributeName, String expectedValue, TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedCondition(locatable, webDriver,
                WebDriverQueryFunctions.hasAttribute(attributeName, expectedValue),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition hasAttribute(String attributeName, String expectedValue)
    {
        return hasAttribute(attributeName, expectedValue, DEFAULT);
    }

    public TimedQuery<String> getAttribute(String attributeName, TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedQuery<String>(locatable, webDriver,
                WebDriverQueryFunctions.getAttribute(attributeName),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedQuery<String> getAttribute(String attributeName)
    {
        return getAttribute(attributeName, DEFAULT);
    }

    public TimedQuery<Set<String>> getCssClasses(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedQuery<Set<String>>(locatable, webDriver,
                WebDriverQueryFunctions.getCssClasses(), timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition hasClass(String className, TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedCondition(locatable, webDriver, WebDriverQueryFunctions.hasClass(className),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition hasClass(String className)
    {
        return hasClass(className, DEFAULT);
    }

    public TimedQuery<String> getTagName(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedQuery<String>(locatable, webDriver, WebDriverQueryFunctions.getTagName(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedQuery<String> getTagName()
    {
        return getTagName(DEFAULT);
    }

    public TimedCondition hasText(String text, TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedCondition(locatable, webDriver, WebDriverQueryFunctions.hasText(text),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition hasText(String text)
    {
        return hasText(text, DEFAULT);
    }

    public TimedCondition hasValue(String value, TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedCondition(locatable, webDriver, WebDriverQueryFunctions.hasValue(value),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedCondition hasValue(String value)
    {
        return hasValue(value, DEFAULT);
    }

    public TimedQuery<Point> getLocation(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedQuery<Point>(locatable, webDriver, WebDriverQueryFunctions.getLocation(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public TimedQuery<Dimension> getSize(TimeoutType timeoutType)
    {
        return new WebDriverLocatableBasedTimedQuery<Dimension>(locatable, webDriver, WebDriverQueryFunctions.getSize(),
                timeouts.timeoutFor(timeoutType), interval());
    }

    public <T> TimedQuery<T> forSupplier(final Supplier<T> supplier, TimeoutType timeoutType)
    {
        return new AbstractTimedQuery<T>(timeouts.timeoutFor(timeoutType), PollingQuery.DEFAULT_INTERVAL,
                ExpirationHandler.RETURN_CURRENT)
        {

            @Override
            protected boolean shouldReturn(T currentEval)
            {
                return true;
            }

            @Override
            protected T currentValue()
            {
                return supplier.get();
            }
        };
    }

    public <T> TimedQuery<T> forSupplier(final Supplier<T> supplier)
    {
        return forSupplier(supplier, DEFAULT);
    }
}
