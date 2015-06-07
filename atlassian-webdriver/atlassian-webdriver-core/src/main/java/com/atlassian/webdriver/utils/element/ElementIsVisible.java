package com.atlassian.webdriver.utils.element;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

/**
 * Continues to execute until a particular element is visible.
 *
 */
public class ElementIsVisible extends ElementVisibilityCondition
{

    public ElementIsVisible(By by)
    {
        super(by, Visibility.VISIBLE);
    }

    public ElementIsVisible(By by, SearchContext el)
    {
        super(by, el, Visibility.VISIBLE);
    }
}
