package com.atlassian.webdriver.utils;

import com.atlassian.webdriver.AtlassianWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Implements mouse events that are not handled correctly in WebDriver atm.
 */
public class MouseEvents
{

    private MouseEvents() {}

    /**
     * Fires a mouse over event on an element that matches the By
     *
     * @param by the element matcher to apply the hover to.
     * @return the {@link WebElement} that the hover was triggered on.
     */
    public static WebElement hover(By by, WebDriver driver)
    {
        return hover(driver.findElement(by), driver);
    }

    /**
     * Fires a mouse over event on the specified element.
     *
     * @param el The element to fire the hover event on.
     *
     * @return the {@link WebElement} the hover was triggered on.
     */
    public static WebElement hover(WebElement el, WebDriver driver)
    {
        Actions hover = new Actions(driver).moveToElement(el);
        hover.perform();

        return el;
    }

    /**
     * Fires a mouse out event on the element that matches the By.
     *
     * @param by the element matcher to apply the hover to.
     *
     * @return the {@link WebElement} the mouseout was fired on.
     */
    public static WebElement mouseout(By by, WebDriver driver)
    {
        return mouseout(driver.findElement(by), driver);
    }

    /**
     * Fires a mouse out event on the element.
     *
     * @param el the element to fire the mouseout event on.
     *
     * @return the {@link WebElement} the mouseout was fired on.
     */
    public static WebElement mouseout(WebElement el, WebDriver driver)
    {
        // native mouseout event not supported in Firefox yet.
        if (WebDriverUtil.isFirefox(driver))
        {
            JavaScriptUtils.dispatchMouseEvent("mouseout", el, driver);
        }
        else
        {
            // Move to the element and then move away to the body element.
            Actions actions = new Actions(driver).moveToElement(el)
                    .moveToElement(driver.findElement(By.tagName("body")));
            actions.perform();
        }

        return el;
    }

}