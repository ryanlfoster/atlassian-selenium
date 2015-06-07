package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.google.common.collect.Lists;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * {@link com.atlassian.pageobjects.elements.PageElementFinder} for the global search context (whole page).
 *
 */
public class GlobalElementFinder implements PageElementFinder {

    @Inject
    AtlassianWebDriver driver;

    @Inject
    PageBinder pageBinder;

    @Nonnull
    public PageElement find(@Nonnull final By by)
    {
        return pageBinder.bind(WebDriverElement.class, by);
    }

    @Nonnull
    public PageElement find(@Nonnull final By by, @Nonnull TimeoutType timeoutType)
    {
        return pageBinder.bind(WebDriverElement.class, by, timeoutType);
    }

    @Nonnull
    public <T extends PageElement> T find(@Nonnull final By by, @Nonnull Class<T> elementClass)
    {
        return pageBinder.bind(WebDriverElementMappings.findMapping(elementClass), by);
    }

    @Nonnull
    public <T extends PageElement> T find(@Nonnull final By by, @Nonnull Class<T> elementClass,
                                          @Nonnull TimeoutType timeoutType)
    {
        return pageBinder.bind(WebDriverElementMappings.findMapping(elementClass), by, timeoutType);
    }

    @Nonnull
    public List<PageElement> findAll(@Nonnull final By by)
    {
        return findAll(by, TimeoutType.DEFAULT);
    }

    @Nonnull
    public List<PageElement> findAll(@Nonnull final By by, @Nonnull TimeoutType timeoutType)
    {
        return findAll(by, PageElement.class, timeoutType);
    }

    @Nonnull
    public <T extends PageElement> List<T> findAll(@Nonnull final By by, @Nonnull Class<T> elementClass)
    {
        return findAll(by, elementClass, TimeoutType.DEFAULT);
    }

    @Nonnull
    public <T extends PageElement> List<T> findAll(@Nonnull final By by, @Nonnull Class<T> elementClass,
                                                   @Nonnull TimeoutType timeoutType)
    {
        List<T> elements = Lists.newLinkedList();
        List<WebElement> webElements = driver.findElements(by);

        for(int i = 0; i < webElements.size(); i++)
        {
            elements.add(pageBinder.bind(WebDriverElementMappings.findMapping(elementClass),
                    WebDriverLocators.list(webElements.get(i), by, i, WebDriverLocators.root()), timeoutType));
        }

        return elements;
    }
}
