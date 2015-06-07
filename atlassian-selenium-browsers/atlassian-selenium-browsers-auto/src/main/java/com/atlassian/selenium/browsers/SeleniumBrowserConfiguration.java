package com.atlassian.selenium.browsers;

import com.atlassian.browsers.BrowserConfiguration;
import com.atlassian.browsers.BrowserVersion;

import java.io.File;

/**
 * Defines the selenium browser configuration to be used by the BrowserAutoInstaller
 * @see com.atlassian.browsers.BrowserAutoInstaller
 */
public class SeleniumBrowserConfiguration implements BrowserConfiguration
{
    public static final String BROWSER = System.getProperty("selenium.browser", BrowserVersion.FIREFOX_3_5.getBrowserName());
    private final File targetDir = new File("target");
    private final File seleniumDir = new File(targetDir, "seleniumTmp");

    public SeleniumBrowserConfiguration(){}

    public File getTmpDir()
    {
        return seleniumDir;
    }

    public String getBrowserName()
    {
        return BROWSER;
    }
}
