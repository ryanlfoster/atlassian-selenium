package com.atlassian.webdriver.browsers;

import com.atlassian.browsers.BrowserConfiguration;
import com.atlassian.browsers.BrowserType;

import java.io.File;

/**
 * A default BrowserConfiguration that reads the specific browser from System properties 
 * and uses the maven 'target' directory as a tmp dir to install the driver in if auto-installing it.
 */
public class WebDriverBrowserConfiguration implements BrowserConfiguration
{
    private final File targetDir = new File("target");
    private final File webdriverDir = new File(targetDir, "webdriverTmp");

    public WebDriverBrowserConfiguration()
    {}

    public File getTmpDir()
    {
        return webdriverDir;
    }

    public String getBrowserName()
    {
        return System.getProperty("webdriver.browser", BrowserType.FIREFOX.getName());
    }
}
