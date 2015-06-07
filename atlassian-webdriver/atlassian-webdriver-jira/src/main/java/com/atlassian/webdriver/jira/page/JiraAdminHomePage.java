package com.atlassian.webdriver.jira.page;

import com.atlassian.pageobjects.page.AdminHomePage;
import com.atlassian.webdriver.jira.component.header.JiraHeader;
import com.atlassian.webdriver.jira.component.menu.AdminSideMenu;

/**
 *
 */
public class JiraAdminHomePage extends JiraAbstractPage implements AdminHomePage<JiraHeader>
{
    private final static String URI = "/secure/admin";

    public String getUrl()
    {
        return URI;
    }

    public AdminSideMenu getAdminSideMenu()
    {
        return pageBinder.bind(AdminSideMenu.class);
    }

    
}
