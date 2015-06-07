package com.atlassian.pageobjects.elements.query.webdriver;

import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Timed condition based on {@link org.openqa.selenium.WebElement}.
 *
 */
public class WebElementBasedTimedCondition extends WebElementBasedTimedQuery<Boolean> implements TimedCondition
{
    public WebElementBasedTimedCondition(SearchContext searchContext, By by, final Function<WebElement, Boolean> valueProvider,
            long timeout, long interval)
    {
        super(searchContext, by, valueProvider, timeout, interval, false);
    }
}
