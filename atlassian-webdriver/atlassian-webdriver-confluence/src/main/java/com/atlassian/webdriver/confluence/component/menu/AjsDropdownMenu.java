package com.atlassian.webdriver.confluence.component.menu;

import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.waiter.Waiter;
import com.atlassian.webdriver.utils.Check;
import com.atlassian.webdriver.utils.MouseEvents;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;

/**
 *
 */
public class AjsDropdownMenu
{

    @Inject
    protected AtlassianWebDriver driver;
    protected WebElement menuItem;

    @Inject
    Waiter waiter;

    private final By componentLocator;


    public AjsDropdownMenu(By componentLocator)
    {
        this.componentLocator = componentLocator;
    }

    @Init
    public void initialise()
    {
        menuItem = driver.findElement(componentLocator);
    }

    public boolean isOpen()
    {
        return Check.hasClass("opened", menuItem);
    }

    public AjsDropdownMenu open()
    {
        if (!isOpen())
        {
            MouseEvents.hover(menuItem, driver);
        }

        // Wait until the menu has finished loading items
        waiter.until().element(By.className("ajs-drop-down"), menuItem).isVisible().and()
            .element(By.cssSelector(".ajs-drop-down.assistive"), menuItem).doesNotExist().execute();



        return this;
    }

    public AjsDropdownMenu close()
    {
        if (isOpen())
        {
            MouseEvents.mouseout(menuItem.findElement(By.cssSelector("a.ajs-menu-title")), driver);
        }
        return this;
    }

}
