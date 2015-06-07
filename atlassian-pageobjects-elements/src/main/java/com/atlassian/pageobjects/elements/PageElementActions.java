package com.atlassian.pageobjects.elements;

import com.atlassian.webdriver.AtlassianWebDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import javax.inject.Inject;

import static com.atlassian.pageobjects.elements.WebDriverElement.getWebElement;

/**
 * <p/>
 * Wrapper around WebDriver's {@link org.openqa.selenium.interactions.Actions} class for convenient use
 * with {@link com.atlassian.pageobjects.elements.PageElement}s. @Inject it into your page objects, or
 * bind via {@link com.atlassian.pageobjects.PageBinder} to get instance explicitly.
 *
 * <p/>
 * Build a sequence of actions to execute by using the builder-style methods of this class. After that
 * {@link #build()} a resulting action to execute, or just {@link #perform()} the actions straight away.
 *
 * @see Actions
 * @since 2.1
 */
public class PageElementActions
{

    @Inject
    private AtlassianWebDriver webDriver;

    private Actions actions;

    private Actions getActions()
    {
        if (actions == null)
        {
            actions = new Actions(webDriver.getDriver());
        }
        return actions;
    }

    /**
     * @see Actions#keyDown(org.openqa.selenium.Keys)
     */
    public PageElementActions keyDown(Keys theKey)
    {
        getActions().keyDown(theKey);
        return this;
    }

    /**
     * @see Actions#keyDown(org.openqa.selenium.WebElement, org.openqa.selenium.Keys)
     */
    public PageElementActions keyDown(PageElement element, Keys theKey)
    {
        getActions().keyDown(getWebElement(element), theKey);
        return this;
    }

    /**
     * @see Actions#keyUp(org.openqa.selenium.Keys)
     */
    public PageElementActions keyUp(Keys theKey)
    {
        getActions().keyUp(theKey);
        return this;
    }

    /**
     * @see Actions#keyUp(org.openqa.selenium.WebElement, org.openqa.selenium.Keys)
     */
    public PageElementActions keyUp(PageElement element, Keys theKey)
    {
        getActions().keyUp(getWebElement(element), theKey);
        return this;
    }

    /**
     * @see Actions#sendKeys(CharSequence...)
     */
    public PageElementActions sendKeys(CharSequence... keysToSend)
    {
        getActions().sendKeys(keysToSend);
        return this;
    }

    /**
     * @see Actions#sendKeys(org.openqa.selenium.WebElement, CharSequence...)
     */
    public PageElementActions sendKeys(PageElement element, CharSequence... keysToSend)
    {
        getActions().sendKeys(getWebElement(element), keysToSend);
        return this;
    }

    /**
     * @see org.openqa.selenium.interactions.Actions#clickAndHold(org.openqa.selenium.WebElement)
     */
    public PageElementActions clickAndHold(PageElement onElement)
    {
        getActions().clickAndHold(getWebElement(onElement));
        return this;
    }

    /**
     * @see org.openqa.selenium.interactions.Actions#clickAndHold()
     */
    public PageElementActions clickAndHold()
    {
        getActions().clickAndHold();
        return this;
    }

    /**
     * @see org.openqa.selenium.interactions.Actions#release(org.openqa.selenium.WebElement)
     */
    public PageElementActions release(PageElement onElement)
    {
        getActions().release(getWebElement(onElement));
        return this;
    }

    /**
     * @see org.openqa.selenium.interactions.Actions#release()
     */
    public PageElementActions release()
    {
        getActions().release();
        return this;
    }

    /**
     * @see Actions#click(org.openqa.selenium.WebElement)
     */
    public PageElementActions click(PageElement onElement)
    {
        getActions().click(getWebElement(onElement));
        return this;
    }

    /**
     * @see org.openqa.selenium.interactions.Actions#click()
     */
    public PageElementActions click() {
        getActions().click();
        return this;
    }

    /**
     * @see org.openqa.selenium.interactions.Actions#doubleClick(org.openqa.selenium.WebElement)
     */
    public PageElementActions doubleClick(PageElement onElement)
    {
        getActions().doubleClick(getWebElement(onElement));
        return this;
    }

    /**
     * @see org.openqa.selenium.interactions.Actions#doubleClick()
     */
    public PageElementActions doubleClick()
    {
        getActions().doubleClick();
        return this;
    }

    /**
     * @see Actions#moveToElement(org.openqa.selenium.WebElement)
     */
    public PageElementActions moveToElement(PageElement toElement)
    {
        getActions().moveToElement(getWebElement(toElement));
        return this;
    }

    /**
     * @see Actions#moveToElement(org.openqa.selenium.WebElement, int, int)
     */
    public PageElementActions moveToElement(PageElement toElement, int xOffset, int yOffset)
    {
        getActions().moveToElement(getWebElement(toElement), xOffset, yOffset);
        return this;
    }

    /**
     * @see Actions#moveByOffset(int, int)
     */
    public PageElementActions moveByOffset(int xOffset, int yOffset)
    {
        getActions().moveByOffset(xOffset, yOffset);
        return this;
    }

    /**
     * @see Actions#contextClick(org.openqa.selenium.WebElement)
     */
    public PageElementActions contextClick(PageElement onElement)
    {
        getActions().contextClick(getWebElement(onElement));
        return this;
    }

    /**
     * @see Actions#dragAndDrop(org.openqa.selenium.WebElement, org.openqa.selenium.WebElement)
     */
    public PageElementActions dragAndDrop(PageElement source, PageElement target)
    {
        getActions().dragAndDrop(getWebElement(source), getWebElement(target));
        return this;
    }

    /**
     * @see Actions#dragAndDropBy(org.openqa.selenium.WebElement, int, int)
     */
    public PageElementActions dragAndDropBy(PageElement source, int xOffset, int yOffset) {
        getActions().dragAndDropBy(getWebElement(source), xOffset, yOffset);
        return this;
    }

    /**
     * Build an action to execute out of the provided sequence. This builder will be reset after calling that method.
     *
     * @return
     */
    Action build()
    {
        return getActions().build();
    }

    /**
     * Execute the specified series of actions. This also resets the actions.
     */
    public void perform()
    {
        getActions().perform();
    }

    /**
     * Reset the sequence of actions to execute.
     *
     * @return this actions instance
     */
    public PageElementActions reset()
    {
        getActions().build(); // resets
        return this;
    }
}
