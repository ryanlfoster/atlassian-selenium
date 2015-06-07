package com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging;

import com.atlassian.pageobjects.Page;
import org.apache.commons.lang.StringUtils;

public abstract class LogConsoleOutputPage implements Page
{
    protected final String pathTo(String fileName)
    {
        return "/html/jsconsolelogging/" + fileName;
    }
}
