package com.atlassian.webdriver.utils.element;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

/**
 * Continues to execute until a particular element is no longer visible.
 *
 */
public class ElementNotVisible extends ElementVisibilityCondition
{
    public ElementNotVisible(By by)
    {
        super(by, Visibility.NOTVISIBLE);
    }

    public ElementNotVisible(By by, SearchContext el)
    {
        super(by, el, Visibility.NOTVISIBLE);
    }
}
