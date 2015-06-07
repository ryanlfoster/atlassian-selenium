package com.atlassian.webdriver.confluence.page;

/**
 * Extends the Login Page as Confluence redirects the user back to the login
 * page when they log out.
 */
public class LogoutPage extends ConfluenceLoginPage
{
    private static final String URI = "/logout.action";


    @Override
    public String getUrl()
    {
        return URI;
    }
}
