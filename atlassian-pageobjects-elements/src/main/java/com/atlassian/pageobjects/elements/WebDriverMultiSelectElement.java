package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link MultiSelectElement}.
 *
 */
public class WebDriverMultiSelectElement extends WebDriverElement implements MultiSelectElement
{
    public WebDriverMultiSelectElement(By locator)
    {
        super(locator);
    }

    public WebDriverMultiSelectElement(By locator, TimeoutType defaultTimeout)
    {
        super(locator, defaultTimeout);
    }

    public WebDriverMultiSelectElement(By locator, WebDriverLocatable parent)
    {
        super(locator, parent);
    }

    public WebDriverMultiSelectElement(WebDriverLocatable locatable, TimeoutType timeoutType)
    {
        super(locatable, timeoutType);
    }

    public WebDriverMultiSelectElement(By locator, WebDriverLocatable parent, TimeoutType timeoutType)
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


    public List<Option> getSelected()
    {
        List<Option> selectedOptions = new ArrayList<Option>();

        for(WebElement option: new Select(waitForWebElement()).getAllSelectedOptions())
        {
            selectedOptions.add(buildOption(option));
        }

        return selectedOptions;
    }

    public MultiSelectElement select(Option option)
    {
        for(WebElement currentOption: new Select(waitForWebElement()).getOptions())
        {
            if(option.equals(buildOption(currentOption)))
            {
                if(!currentOption.isSelected()) {
                    currentOption.click();
                }
                break;
            }
        }

        return this;
    }

    public MultiSelectElement unselect(Option option)
    {
        for(WebElement currentOption: new Select(waitForWebElement()).getOptions())
        {
            if(option.equals(buildOption(currentOption)))
            {
                if(currentOption.isSelected())
                {
                    currentOption.click();
                }
                break;
            }
        }

        return this;
    }

    public MultiSelectElement selectAll()
    {
        for(WebElement option: new Select(waitForWebElement()).getOptions())
        {
            if(!option.isSelected())
            {
                option.click();
            }
        }

        return null;
    }

    public MultiSelectElement unselectAll()
    {
        new Select(waitForWebElement()).deselectAll();
        return this;
    }
}
