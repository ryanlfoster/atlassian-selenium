package com.atlassian.webdriver.utils.element;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

/**
 * Continues to execute until a particular element is located.
 */
public class ElementLocated extends ElementLocationCondition
{

    public ElementLocated(By by)
    {
        super(by, Locatable.LOCATED);
    }

    public ElementLocated(By by, SearchContext el)
    {
        super(by, el, Locatable.LOCATED);
    }

}