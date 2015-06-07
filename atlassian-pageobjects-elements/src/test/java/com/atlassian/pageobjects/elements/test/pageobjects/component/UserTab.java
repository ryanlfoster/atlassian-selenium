package com.atlassian.pageobjects.elements.test.pageobjects.component;

import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.components.ActivatedComponent;
import org.openqa.selenium.By;

/**
 * Represents the UserTab of the rolestab that is present in the AUIPage.
 */
public class UserTab implements ActivatedComponent<UserTab>
{
    private final String tabTitle = "User Tab";
    private final UserRoleTabs tabs;

    public UserTab(UserRoleTabs tabs)
    {
        this.tabs = tabs;
    }

    public String header()
    {
        return getView().find(By.tagName("h4")).getText();
    }

    public PageElement getTrigger()
    {
        return tabs.selectedTab();
    }

    public PageElement getView()
    {
        return tabs.selectedView();
    }

    public UserTab open()
    {
        tabs.openTab(this.tabTitle);
        return this;
    }

    public boolean isOpen()
    {
        return tabs.selectedTab().getText().equals(tabTitle);
    }
}
