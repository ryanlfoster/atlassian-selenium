package com.atlassian.webdriver.greenhopper.page.admin;

import com.atlassian.webdriver.jira.JiraTestedProduct;
import com.atlassian.webdriver.jira.page.JiraAdminAbstractPage;

/**
 * @since 2.0
 */
public class GreenHopperEnabledProjectsPage extends JiraAdminAbstractPage
{
    private static final String URI = "/secure/GHProjects.jspa?decorator=admin";

    public String getUrl()
    {
        return URI;
    }
}
