package com.atlassian.pageobjects.components.aui;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.pageobjects.binder.InvalidPageStateException;
import com.atlassian.pageobjects.components.TabbedComponent;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.query.Poller;
import org.openqa.selenium.By;

import javax.inject.Inject;
import java.util.List;

/**
 * Represents a tabbed content area created via AUI.
 *
 * This is an example of a reusable components.
 */
public class AuiTabs implements TabbedComponent
{
    @Inject
    protected PageBinder pageBinder;

    @Inject
    protected PageElementFinder elementFinder;

    private final By rootLocator;

    private PageElement rootElement;

    public AuiTabs(By locator)
    {
        this.rootLocator = locator;
    }

    @Init
    public void initialize()
    {
        this.rootElement = elementFinder.find(rootLocator);
    }

    public PageElement selectedTab()
    {
        List<PageElement> items = rootElement.find(By.className("tabs-menu")).findAll(By.tagName("li"));

        for(int i = 0; i < items.size(); i++)
        {
            PageElement tab = items.get(i);
            if(tab.hasClass("active-tab"))
            {
                return tab;
            }
        }

        throw new InvalidPageStateException("A tab must be active.", this);
    }

    public PageElement selectedView()
    {
         List<PageElement> panes = rootElement.findAll(By.className("tabs-pane"));
        for(int i = 0; i < panes.size(); i++)
        {
            PageElement pane = panes.get(i);
            if(pane.hasClass("active-pane"))
            {
                return pane;
            }
        }
        
        throw new InvalidPageStateException("A pane must be active", this);
    }

    public List<PageElement> tabs()
    {
        return rootElement.find(By.className("tabs-menu")).findAll(By.tagName("li"));
    }

    public PageElement openTab(String tabText)
    {
        List<PageElement> tabs = rootElement.find(By.className("tabs-menu")).findAll(By.tagName("a"));

        for(int i = 0; i < tabs.size(); i++)
        {
            if(tabs.get(i).getText().equals(tabText))
            {
                PageElement listItem = tabs.get(i);
                listItem.click();

                // find the pane and wait until it has class "active-pane"
                String tabViewHref = listItem.getAttribute("href");
                String tabViewClassName = tabViewHref.substring(tabViewHref.indexOf('#') + 1);
                PageElement pane = rootElement.find(By.id(tabViewClassName));
                Poller.waitUntilTrue(pane.timed().hasClass("active-pane"));

                return pane;
            }
        }

        throw new InvalidPageStateException("Tab not found", this);
    }

    public PageElement openTab(PageElement tab)
    {
        String tabIdentifier = tab.getText();
        return openTab(tabIdentifier);
    }
}
