package com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging;

public class WindowErrorPage extends LogConsoleOutputPage
{
    @Override
    public String getUrl()
    {
        return pathTo("window-errors.html");
    }
}
