package com.atlassian.webdriver.jira.component.menu;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.jira.component.ClickableLink;
import com.atlassian.webdriver.jira.component.WebDriverLink;
import com.atlassian.webdriver.jira.page.LogoutPage;
import org.openqa.selenium.By;

import javax.inject.Inject;

/**
 * Object for interacting with the User Menu in the JIRA Header.
 */
public class JiraUserMenu
{
    @Inject
    PageBinder pageBinder;

    @ClickableLink(id = "log_out", nextPage = LogoutPage.class)
    WebDriverLink<LogoutPage> logoutLink;


    private AuiDropdownMenu userMenu;

    @Init
    public void initialise()
    {
        userMenu = pageBinder.bind(AuiDropdownMenu.class, By.id("header-details-user"));
    }

    public LogoutPage logout()
    {
        return logoutLink.activate();
    }

    public JiraUserMenu open()
    {
        userMenu.open();
        return this;
    }

    public boolean isOpen()
    {
        return userMenu.isOpen();
    }

    public JiraUserMenu close()
    {
        userMenu.close();
        return this;
    }
}
