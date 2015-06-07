package com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging;

public class IncludedScriptErrorPage extends LogConsoleOutputPage
{
    @Override
    public String getUrl()
    {
        return pathTo("included-script-errors.html");
    }

    public String objectIsNotFunctionScriptUrl()
    {
        return "included-script-errors-1.js";
    }

    public String throwErrorObjectScriptUrl()
    {
        return "included-script-errors-2.js";
    }
}
