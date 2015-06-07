package com.atlassian.webdriver.jira.component.menu;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.utils.Check;
import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;

/**
 * Represents an AUI dropdown menu
 */
public class AuiDropdownMenu
{
    @Inject
    protected AtlassianWebDriver driver;

    @Inject
    protected PageBinder pageBinder;

    private final By rootElementLocator;
    private WebElement rootElement;

    /**
     * Default constructor
     * @param rootElementLocator The locator to the root element of the menu.
     */
    public AuiDropdownMenu(By rootElementLocator)
    {
        this.rootElementLocator = rootElementLocator;
    }

    @Init
    public void initialize()
    {
        rootElement = driver.findElement(rootElementLocator);
    }

    public AuiDropdownMenu open()
    {
        if(!isOpen())
        {
            rootElement.findElement(By.cssSelector("a.drop")).click();
            waitUntilOpen();
        }
        return this;
    }

    public boolean isOpen()
    {
        return Check.hasClass("active", rootElement)
                && !Check.elementExists(By.className("loading"), rootElement)
                && Check.elementExists(By.tagName("li"), rootElement);
    }

    public AuiDropdownMenu close()
    {
        if(isOpen())
        {
            rootElement.findElement(By.cssSelector("a.drop")).click();
            waitUntilClose();
        }
        return this;
    }

    public void waitUntilOpen()
    {
        driver.waitUntil(new Function<WebDriver,Boolean>(){
            public Boolean apply( WebDriver webDriver) {
                return isOpen();
            }
        });
    }

    public void waitUntilClose()
    {
        driver.waitUntil(new Function<WebDriver,Boolean>(){
            public Boolean apply( WebDriver webDriver) {
                return !isOpen();
            }
        });
    }
}