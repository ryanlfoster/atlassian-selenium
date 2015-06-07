package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import com.atlassian.pageobjects.elements.query.webdriver.WebDriverQueryFactory;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.atlassian.webdriver.Elements.ATTRIBUTE_ID;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of TimedElement based on WebDriver
 */
public class WebDriverTimedElement implements TimedElement
{
    @Inject
    PageBinder pageBinder;

    private WebDriverQueryFactory queryFactory;
    private final WebDriverLocatable locatable;
    private final TimeoutType defaultTimeout;

    /**
     * Create a WebDriverTimedElement with the given timeout.
     *
     * @param locatable locatable for the target element
     * @param defaultTimeout default timeout of this element
     */
    public WebDriverTimedElement(WebDriverLocatable locatable, TimeoutType defaultTimeout)
    {
        this.locatable = checkNotNull(locatable);
        this.defaultTimeout = checkNotNull(defaultTimeout);
    }

    @Init
    public void initialize()
    {
        queryFactory = pageBinder.bind(WebDriverQueryFactory.class, locatable);
    }
    
    public TimedCondition isPresent()
    {
        return queryFactory.isPresent(defaultTimeout);
    }

    public TimedCondition isVisible()
    {
        return queryFactory.isVisible(defaultTimeout);
    }

    public TimedCondition isEnabled()
    {
        return queryFactory.isEnabled(defaultTimeout);
    }

    public TimedCondition isSelected()
    {
        return queryFactory.isSelected(defaultTimeout);
    }

    @Nonnull
    @Override
    public TimedQuery<String> getId()
    {
        return queryFactory.getAttribute(ATTRIBUTE_ID);
    }

    @Nonnull
    @Override
    public TimedQuery<Set<String>> getCssClasses()
    {
        return queryFactory.getCssClasses(defaultTimeout);
    }

    public TimedCondition hasClass(final String className)
    {
        return queryFactory.hasClass(className, defaultTimeout);
    }

    public TimedQuery<String> getAttribute(final String name)
    {
        return queryFactory.getAttribute(name, defaultTimeout);
    }

    public TimedCondition hasAttribute(final String name, final String value)
    {
        return queryFactory.hasAttribute(name, value, defaultTimeout);
    }

    public TimedQuery<String> getText()
    {
        return queryFactory.getText(defaultTimeout);
    }

    public TimedCondition hasText(String text)
    {
        return queryFactory.hasText(text, defaultTimeout);
    }

    public TimedQuery<String> getTagName()
    {
        /* Even though the tagname can't change, the reason why we go through the same query mechanism is to
        get the polling for finding the element */
        return queryFactory.getTagName(defaultTimeout);
    }

    public TimedQuery<String> getValue()
    {
        return queryFactory.getValue(defaultTimeout);
    }

    public TimedCondition hasValue(String value)
    {
        return queryFactory.hasValue(value, defaultTimeout);
    }

    @Override
    public TimedQuery<Point> getLocation()
    {
        return queryFactory.getLocation(defaultTimeout);
    }

    @Override
    public TimedQuery<Dimension> getSize()
    {
        return queryFactory.getSize(defaultTimeout);
    }
}
