package com.atlassian.webdriver.utils.element;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

/**
 * Continues to execute until a particular element is no longer located.
 */
public class ElementNotLocated extends ElementLocationCondition
{

    public ElementNotLocated(By by)
    {
        super(by, Locatable.NOTLOCATED);
    }

    public ElementNotLocated(By by, SearchContext el)
    {
        super(by, el, Locatable.NOTLOCATED);
    }

}