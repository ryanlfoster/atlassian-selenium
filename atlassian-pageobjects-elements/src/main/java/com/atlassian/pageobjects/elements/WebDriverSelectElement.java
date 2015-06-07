package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link SelectElement}
 */
public class WebDriverSelectElement extends WebDriverElement implements SelectElement
{
     public WebDriverSelectElement(By locator)
    {
        super(locator);
    }

    public WebDriverSelectElement(By locator, TimeoutType defaultTimeout)
    {
        super(locator, defaultTimeout);
    }

    public WebDriverSelectElement(WebDriverLocatable locatable, TimeoutType timeoutType)
    {
        super(locatable, timeoutType);
    }

    public WebDriverSelectElement(By locator, WebDriverLocatable parent)
    {    
        super(locator, parent);
    }

    public WebDriverSelectElement(By locator, WebDriverLocatable parent, TimeoutType timeoutType)
    {
        super(locator, parent, timeoutType);
    }


    private Option buildOption(WebElement option)
    {
        return Options.full(
            option.getAttribute("id"),
            option.getAttribute("value"),
            option.getText());
    }

    public List<Option> getAllOptions()
    {
        List<Option> optionList = new ArrayList<Option>();

        for(WebElement option: new Select(waitForWebElement()).getOptions())
        {
            optionList.add(buildOption(option));
        }

        return optionList;
    }

    public Option getSelected()
    {
        return buildOption(new Select(waitForWebElement()).getFirstSelectedOption());
    }

    public SelectElement select(Option option)
    {
        for(WebElement currentOption: new Select(waitForWebElement()).getOptions())
        {
            if(option.equals(buildOption(currentOption)))
            {
                if (!currentOption.isSelected()) {
                    currentOption.click();
                }
                break;
            }
        }

        return this;
    }
}
