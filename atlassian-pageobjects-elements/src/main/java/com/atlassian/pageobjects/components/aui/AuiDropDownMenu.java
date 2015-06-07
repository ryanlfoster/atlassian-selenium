package com.atlassian.pageobjects.components.aui;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.pageobjects.components.ActivatedComponent;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.query.Poller;
import org.openqa.selenium.By;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a DropDown built via AUI.
 *
 * This is an example of a reusable components.
 */
public class AuiDropDownMenu implements ActivatedComponent<AuiDropDownMenu>
{
    @Inject
    protected PageBinder pageBinder;

    @Inject
    protected PageElementFinder elementFinder;
    
    private final By locator;
    private PageElement rootElement;
    private PageElement triggerElement;
    private PageElement viewElement;

    public AuiDropDownMenu(By locator)
    {
        this.locator = locator;
    }

    @Init
    public void initialize()
    {
        rootElement = elementFinder.find(locator);
        triggerElement = rootElement.find(By.className("aui-dd-trigger"));
        viewElement = rootElement.find(By.className("aui-dropdown"));
    }

    public PageElement getTrigger()
    {
        return triggerElement;
    }

    public PageElement getView()
    {
        return viewElement;
    }

    public AuiDropDownMenu open()
    {
        if(!isOpen())
        {
            triggerElement.click();
            Poller.waitUntilTrue(viewElement.timed().isVisible());
        }
        return this;
    }

    public AuiDropDownMenu close()
    {
        if(isOpen())
        {
            triggerElement.click();
            Poller.waitUntilTrue(viewElement.timed().isVisible());
        }
        return this;
    }

    public boolean isOpen()
    {
        return viewElement.isVisible();
    }

    public List<String> getItems()
    {
        List<String> items = new ArrayList<String>();
        for(PageElement e: viewElement.findAll(By.tagName("li")))
        {
            items.add(e.getText());
        }

        return items;
    }
}
