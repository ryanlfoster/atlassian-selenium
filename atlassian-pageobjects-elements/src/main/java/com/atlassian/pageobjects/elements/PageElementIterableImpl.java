package com.atlassian.pageobjects.elements;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;

import com.atlassian.pageobjects.elements.timeout.TimeoutType;

/**
 * Iterable of PageElements defined by the criteria given to the constructor.
 * 
 * Each call to iterator() returns a fresh list.
 * @since 2.1
 */
public class PageElementIterableImpl implements Iterable<PageElement>
{
    private final PageElementFinder finder;
    private final Class<? extends PageElement> fieldType;
    private final By by;
    private final TimeoutType timeoutType;

    public PageElementIterableImpl(PageElementFinder finder, Class<? extends PageElement> fieldType, By by, TimeoutType timeoutType)
    {
        super();
        this.finder = finder;
        this.fieldType = fieldType;
        this.by = by;
        this.timeoutType = timeoutType;
    }

    public Iterator<PageElement> iterator()
    {
        List<? extends PageElement> pageElements = finder.findAll(by, fieldType, timeoutType);
        return (Iterator<PageElement>) pageElements.iterator();
    }
}
