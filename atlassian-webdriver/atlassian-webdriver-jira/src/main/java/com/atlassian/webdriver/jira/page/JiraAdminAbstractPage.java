package com.atlassian.webdriver.jira.page;


import com.atlassian.webdriver.jira.component.menu.AdminSideMenu;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public abstract class JiraAdminAbstractPage extends JiraAbstractPage
{

    // Set of admin links
    private static Set<Object> obs = new HashSet<Object>();

    public AdminSideMenu getAdminSideMenu()
    {
        return pageBinder.bind(AdminSideMenu.class);
    }

}
