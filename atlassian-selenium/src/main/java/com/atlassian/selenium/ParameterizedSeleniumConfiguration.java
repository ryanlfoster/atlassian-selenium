package com.atlassian.selenium;

import com.atlassian.selenium.AbstractSeleniumConfiguration;

public class ParameterizedSeleniumConfiguration extends AbstractSeleniumConfiguration
{

    private final String serverLocation, browserStartString, baseUrl;
    private final int serverPort;

    public ParameterizedSeleniumConfiguration(String serverLocation, int serverPort,
                                                String browserStartString, String baseUrl)
    {
        this.serverLocation = serverLocation;
        this.serverPort = serverPort;
        this.browserStartString = browserStartString;
        this.baseUrl = baseUrl;
    }

    public String getServerLocation() {
        return serverLocation;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getBrowserStartString() {
        return browserStartString;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean getStartSeleniumServer() {
        return false;
    }
}
