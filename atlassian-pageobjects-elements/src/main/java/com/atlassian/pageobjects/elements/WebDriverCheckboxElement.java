package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.openqa.selenium.By;

/**
 * WebDriver-based implementation of {@link CheckboxElement}
 */
public class WebDriverCheckboxElement extends WebDriverElement implements CheckboxElement
{
    public WebDriverCheckboxElement(By locator)
    {
        super(locator);
    }

    public WebDriverCheckboxElement(By locator, TimeoutType defaultTimeout)
    {
        super(locator, defaultTimeout);
    }

    public WebDriverCheckboxElement(WebDriverLocatable locatable, TimeoutType timeoutType)
    {
        super(locatable, timeoutType);
    }

    public WebDriverCheckboxElement(By locator, WebDriverLocatable parent)
    {
        super(locator, parent);
    }

    public WebDriverCheckboxElement(By locator, WebDriverLocatable parent, TimeoutType timeoutType)
    {
        super(locator, parent, timeoutType);
    }

    @Override
    public boolean isChecked()
    {
        return isSelected();
    }

    public CheckboxElement check()
    {
        select();
        return this;
    }

    public CheckboxElement uncheck()
    {
        if(isSelected())
        {
            toggle();
        }
        return this;
    }
}
