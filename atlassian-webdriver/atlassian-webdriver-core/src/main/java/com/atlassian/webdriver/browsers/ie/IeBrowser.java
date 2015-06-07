package com.atlassian.webdriver.browsers.ie;

import com.atlassian.browsers.BrowserConfig;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;

/**
 *
 */
public final class IeBrowser
{
    private static final Logger log = LoggerFactory.getLogger(IeBrowser.class);

    public static final String IE_SERVICE_EXECUTABLE = "IEDriverServer.exe";

    private IeBrowser()
    {
        throw new AssertionError("Don't instantiate me");
    }

    public static InternetExplorerDriver createIeDriver(@Nullable String browserPath, @Nullable BrowserConfig config)
    {
        if (noBrowserPath(browserPath) && noServicePath(config))
        {
            return createDefaultDriver();
        }
        else
        {
            final InternetExplorerDriverService service = setBrowserExecutablePath(browserPath,
                    setServiceExecutablePath(config, new InternetExplorerDriverService.Builder()))
                    .usingAnyFreePort()
                    .build();
            return new InternetExplorerDriver(service);
        }
    }

    public static InternetExplorerDriver createDefaultDriver()
    {
        return new InternetExplorerDriver();
    }

    private static boolean noBrowserPath(@Nullable String browserPath)
    {
        return browserPath == null;
    }

    private static boolean hasServicePath(@Nullable BrowserConfig config)
        {
            return config != null && config.getProfilePath() != null;
        }

    private static boolean noServicePath(@Nullable BrowserConfig config)
    {
        return !hasServicePath(config);
    }

    private static InternetExplorerDriverService.Builder setServiceExecutablePath(BrowserConfig browserConfig, InternetExplorerDriverService.Builder builder)
    {
        if (hasServicePath(browserConfig))
        {
            File profilePath = new File(browserConfig.getProfilePath());
            File ieDriverFile = new File(profilePath, IE_SERVICE_EXECUTABLE);
            if (ieDriverFile.isFile())
            {
                builder.usingDriverExecutable(ieDriverFile);
            }
        }
        return builder;
    }

    private static InternetExplorerDriverService.Builder setBrowserExecutablePath(String browserPath, InternetExplorerDriverService.Builder builder)
        {
            if (browserPath != null)
            {
                // can't do much here, IE driver knows better where to look for IE
                log.warn("Non-null browser path configured for IE: '{}', but IEDriver does not support custom browser paths");
            }
            return builder;
        }

}
