package com.atlassian.selenium;

public class SystemPropertySeleniumConfiguration extends AbstractSeleniumConfiguration
{

    protected String serverLocation;
    protected int serverPort = 4444;
    protected String browserStartString;
    protected boolean startSeleniumServer;
    protected boolean useSingleBrowser;

    protected String appLocation;
    protected String appPort;
    protected String appContext;

    protected SystemPropertySeleniumConfiguration(String selServerLocationProp, String seleniumServerLocationDefault,
                                                  String selServerPortProp, int selServerPortDefault,
                                                  String selStartServerProp, boolean startServerDefault,

                                                  String appLocationProp, String appLocationDefault, String appPortProp,
                                                  String appPortDefault, String appContextProp, String appContextDefault)
    {
        serverLocation = System.getProperty(selServerLocationProp, seleniumServerLocationDefault);
        String serverPortStr = System.getProperty(selServerPortProp, Integer.toString(selServerPortDefault));
        if(!nullOrEmpty(serverPortStr))
        {
                serverPort = Integer.parseInt(serverPortStr);
        }

        String startSeleniumServerStr = System.getProperty(selStartServerProp, Boolean.toString(startServerDefault));
        if(nullOrEmpty(startSeleniumServerStr))
        {
            startSeleniumServer = Boolean.parseBoolean(startSeleniumServerStr);
        }
        browserStartString = System.getProperty("seleniumBrowserStartString", "*firefox");

        String useSingleBrowserStr  = System.getProperty("seleniumStartServer", "false");
        if(nullOrEmpty(useSingleBrowserStr))
        {
            useSingleBrowser = Boolean.parseBoolean(useSingleBrowserStr);
        }


        appLocation = System.getProperty(appLocationProp, appLocationDefault);
        appPort = System.getProperty(appPortProp, appPortDefault);
        appContext = System.getProperty(appContextProp, appContextDefault);
    }

    private static boolean nullOrEmpty(String str)
    {
        return (str == null) || (str.trim().length() == 0);
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
        return "http://" + appLocation + ":" + appPort + "/" + appContext;
    }

    public boolean getStartSeleniumServer() {
        return startSeleniumServer;
    }

    public String getApplicationContext()
    {
        return appContext;
    }
}
