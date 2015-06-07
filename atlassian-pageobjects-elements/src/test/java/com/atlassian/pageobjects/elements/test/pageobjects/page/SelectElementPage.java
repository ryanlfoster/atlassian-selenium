package com.atlassian.pageobjects.elements.test.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.MultiSelectElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.SelectElement;
import com.atlassian.pageobjects.elements.WebDriverMultiSelectElement;
import com.atlassian.pageobjects.elements.WebDriverSelectElement;
import org.openqa.selenium.By;

import javax.inject.Inject;

/**
 * Represents selectelement.html
 */
public class SelectElementPage implements Page
{
    @Inject
    PageElementFinder elementFinder;

    @ElementBy(id="test1_Select")
    SelectElement select1;

    @ElementBy(id="test4_Select")
    MultiSelectElement select4;


    public String getUrl()
    {
         return "/html/selectelement.html";
    }

    public SelectElement findSelect(By locator)
    {
        return elementFinder.find(locator, WebDriverSelectElement.class);
    }

    public MultiSelectElement findMultiSelect(By locator)
    {
        return elementFinder.find(locator, WebDriverMultiSelectElement.class);
    }

    public SelectElement getSelectElement1()
    {
        return select1;
    }

    public MultiSelectElement getMultiSelectElement4()
    {
        return select4;
    }
}
