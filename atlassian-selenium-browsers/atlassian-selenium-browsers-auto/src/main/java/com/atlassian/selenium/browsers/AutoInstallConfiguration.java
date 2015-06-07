package com.atlassian.selenium.browsers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.atlassian.browsers.BrowserAutoInstaller;
import com.atlassian.browsers.BrowserConfig;
import com.atlassian.browsers.InstallConfigurator;
import com.atlassian.selenium.AbstractSeleniumConfiguration;
import com.atlassian.selenium.browsers.firefox.DisplayAwareFirefoxChromeLauncher;

import org.openqa.selenium.server.browserlaunchers.BrowserLauncherFactory;

/**
 * Configures Selenium by detecting if installation of a browserStartString is required, and if so, installs it for this session.
 *
 * @since 2.0
 */
class AutoInstallConfiguration extends AbstractSeleniumConfiguration
{

    private static final String LOCATION = System.getProperty("selenium.location", "localhost");
    private static final int PORT = Integer.getInteger("selenium.port", pickFreePort());

    //private static final String BROWSER = System.getProperty("selenium.browser", "firefox-3.5");

    private static final String BASE_URL = System.getProperty("baseurl", "http://localhost:8080/");
    private static final String BROWSER_PROFILE = System.getProperty("selenium.browser.profile");

    private static final long MAX_WAIT_TIME = 10000;
    private static final long CONDITION_CHECK_INTERVAL = 100;

    private String firefoxProfileTemplate = BROWSER_PROFILE;
    private String baseUrl;

    private String browserStartString = SeleniumBrowserConfiguration.BROWSER;

    AutoInstallConfiguration()
    {

        BrowserAutoInstaller browserInstaller = new BrowserAutoInstaller(new SeleniumBrowserConfiguration(),
            new InstallConfigurator() {
                public void setupFirefoxBrowser(BrowserConfig browserConfig)
                {
                    firefoxProfileTemplate = browserConfig.getProfilePath();
                    browserStartString = "*" + BrowserAutoInstaller.CHROME_XVFB + " " + browserConfig.getBinaryPath();
                }

                public void setupChromeBrowser(BrowserConfig browserConfig)
                {

                }
            });

        browserInstaller.setupBrowser();

        BrowserLauncherFactory.addBrowserLauncher(BrowserAutoInstaller.CHROME_XVFB, DisplayAwareFirefoxChromeLauncher.class);

        if (browserStartString == null)
        {
            browserStartString = SeleniumBrowserConfiguration.BROWSER;
        }
        if (!BASE_URL.endsWith("/"))
        {
            baseUrl = BASE_URL + "/";
        }
        else
        {
            baseUrl = BASE_URL;
        }
    }

    public String getServerLocation()
    {
        return LOCATION;
    }

    public int getServerPort()
    {
        return PORT;
    }

    public String getBrowserStartString()
    {
        return browserStartString;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public boolean getStartSeleniumServer()
    {
        return !isPortInUse();
    }

    private boolean isPortInUse()
    {
        try
        {
            new Socket("localhost", getServerPort());
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public long getActionWait()
    {
        return MAX_WAIT_TIME;
    }

    public long getPageLoadWait()
    {
        return MAX_WAIT_TIME;
    }

    public long getConditionCheckInterval()
    {
        return CONDITION_CHECK_INTERVAL;
    }

    @Override
    public String getFirefoxProfileTemplate()
    {
        return firefoxProfileTemplate;
    }

    static int pickFreePort()
    {
        ServerSocket socket = null;
        try
        {
            socket = new ServerSocket(0);
            return socket.getLocalPort();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error opening socket", e);
        }
        finally
        {
            if (socket != null)
            {
                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Error closing socket", e);
                }
            }
        }
    }
}
