package com.atlassian.webdriver.confluence.page;


import com.atlassian.pageobjects.page.HomePage;
import com.atlassian.webdriver.confluence.component.header.ConfluenceHeader;

/**
 * Page object implementation for the Dashbaord page in Confluence.
 * TODO: extend to handle more the page properly.
 */
public class DashboardPage extends ConfluenceAbstractPage implements HomePage<ConfluenceHeader>
{
    private static String URI = "/dashboard.action";

    public String getUrl()
    {
        return URI;
    }
}
