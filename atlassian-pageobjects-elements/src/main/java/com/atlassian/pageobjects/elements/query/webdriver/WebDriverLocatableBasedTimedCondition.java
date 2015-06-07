package com.atlassian.pageobjects.elements.query.webdriver;

import com.atlassian.pageobjects.elements.WebDriverLocatable;
import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.google.common.base.Function;
import org.openqa.selenium.WebElement;

/**
 * Timed condition based on {@link org.openqa.selenium.WebElement}.
 *
 */
public class WebDriverLocatableBasedTimedCondition extends WebDriverLocatableBasedTimedQuery<Boolean> implements TimedCondition
{
    public WebDriverLocatableBasedTimedCondition(WebDriverLocatable locatable, AtlassianWebDriver webDriver,
                                                 Function<WebElement, Boolean> valueProvider, long timeout, long interval)
    {
        super(locatable, webDriver, valueProvider, timeout, interval, false);
    }
}
