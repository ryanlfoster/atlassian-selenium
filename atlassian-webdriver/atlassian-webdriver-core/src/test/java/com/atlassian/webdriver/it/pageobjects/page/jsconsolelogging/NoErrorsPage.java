package com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging;

public class NoErrorsPage extends LogConsoleOutputPage
{
    @Override
    public String getUrl()
    {
        return pathTo("no-errors.html");
    }
}
