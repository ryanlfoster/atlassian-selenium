package com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging;

public class UntypedErrorPage extends LogConsoleOutputPage
{
    @Override
    public String getUrl()
    {
        return pathTo("untyped-script-errors.html");
    }

    public String throwStringScriptUrl()
    {
        return "untyped-errors-1.js";
    }

    public String consoleErrorScriptUrl()
    {
        return "untyped-errors-2.js";
    }

}
