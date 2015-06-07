package com.atlassian.webdriver.jira.component.menu;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.utils.by.ByJquery;

import javax.inject.Inject;

/**
 * TODO: Document this class / interface here
 *
 */
public class DashboardMenu
{
    @Inject
    PageBinder pageBinder;

    private AuiDropdownMenu dashboardMenu;

    @Init
    public void initialise()
    {
        dashboardMenu = pageBinder.bind(AuiDropdownMenu.class, ByJquery.$("#home_link").parent("li"));
    }

    public DashboardMenu open()
    {
        dashboardMenu.open();
        return this;
    }

    public boolean isOpen()
    {
        return dashboardMenu.isOpen();
    }

    public DashboardMenu close()
    {
        dashboardMenu.close();
        return this;
    }

}
