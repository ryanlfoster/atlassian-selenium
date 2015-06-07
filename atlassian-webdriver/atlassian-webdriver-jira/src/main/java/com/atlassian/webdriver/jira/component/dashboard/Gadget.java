package com.atlassian.webdriver.jira.component.dashboard;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.utils.Check;
import com.atlassian.webdriver.utils.MouseEvents;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;

/**
 * Implements a object for interacting with a JIRA Dashboard Gadget.
 */
public class Gadget
{
    @Inject
    AtlassianWebDriver driver;

    @Inject
    PageBinder pageBinder;

    private final String id;
    private String titleId;
    private String frameId;
    private WebElement chrome;

    public Gadget(String id)
    {
        this.id = id;
    }

    @Init
    public void init()
    {
        this.frameId = "gadget-" + id;
        this.titleId = frameId + "-title";
        final String chromeId = frameId + "-chrome";

        // This should only get invoked when the gadget doesn't exist. If there are unexpected timing
        // issues, could change to a wait until.
        if (!Check.elementExists(By.id(titleId), driver))
        {
            throw new IllegalStateException("Gadget with id: " + id + " not found on page.");
        }

        driver.waitUntilElementIsLocated(By.id(frameId));
        this.chrome = driver.findElement(By.id(chromeId));
    }

    /**
     * Switches to the gadget iframe and returns a GadgetView which is the html page for the gadget.
     * @return
     */
    public GadgetView view()
    {
        driver.switchTo().frame(frameId);
        driver.waitUntilElementIsLocated(By.className("view"));

        return pageBinder.bind(GadgetView.class, driver.findElement(By.className("view")));
    }

    public void minimize()
    {

        WebElement dropdown = openDropdown();
        if (Check.elementExists(By.className("minimization"), dropdown))
        {
            dropdown.findElement(By.className("minimization")).click();
        }
        else
        {
            closeDropdown();
        }

    }

    public void maximize()
    {

        WebElement dropdown = openDropdown();
        if (Check.elementExists(By.className("maximization"), dropdown))
        {
            dropdown.findElement(By.className("maximization")).click();
        }
        else
        {
            closeDropdown();
        }

    }

    public void refresh()
    {

        WebElement dropdown = openDropdown();
        if (Check.elementExists(By.className("reload"), dropdown))
        {
            dropdown.findElement(By.cssSelector(".reload .no_target")).click();
        }
        else
        {
            closeDropdown();
        }

    }

    public String getGadgetTitle()
    {
        return driver.findElement(By.id(titleId)).getText();
    }

    private WebElement openDropdown()
    {
        WebElement dropdown = getDropdown();

        if (Check.hasClass("hidden", dropdown))
        {
            MouseEvents.hover(chrome, driver)
                    .findElement(By.className(("aui-dd-trigger")))
                    .click();
        }

        return dropdown;
    }

    private void closeDropdown()
    {
        WebElement dropdown = getDropdown();

        if (!Check.hasClass("hidden", dropdown))
        {
            chrome.findElement(By.className("aui-dd-trigger")).click();
        }

    }

    private WebElement getDropdown()
    {
        return chrome.findElement(By.className("aui-dropdown"));
    }

}