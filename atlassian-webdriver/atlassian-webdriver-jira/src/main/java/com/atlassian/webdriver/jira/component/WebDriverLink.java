package com.atlassian.webdriver.jira.component;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.Page;
import com.atlassian.webdriver.AtlassianWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.SearchContext;

import javax.inject.Inject;

/**
 *
 */
public class WebDriverLink<P extends Page>
{
    @Inject
    private AtlassianWebDriver webDriver;

    @Inject
    private PageBinder pageBinder;

    private final By locator;
    private final Class<P> pageObjectClass;

    public WebDriverLink(By locator, Class<P> pageObjectClass)
    {
        this.locator = locator;
        this.pageObjectClass = pageObjectClass;
    }

    public P activate()
    {
        return activate(webDriver);
    }

    public P activate(SearchContext context)
    {
        if (webDriver.elementExistsAt(locator, context))
        {
            context.findElement(locator).click();

            return pageBinder.bind(pageObjectClass);
        }

        throw new ElementNotVisibleException("The link could not be activated By(" + locator + ") failed to find element");
    }
}
