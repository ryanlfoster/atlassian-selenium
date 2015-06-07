package com.atlassian.webdriver.browsers.chrome;

import com.atlassian.browsers.BrowserConfig;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @since 2.1
 */
public class ChromeBrowser
{
    private static final Logger log = LoggerFactory.getLogger(ChromeBrowser.class);

    private ChromeBrowser()
    {
    }

    public static ChromeDriver getChromeDriver()
    {
        return new ChromeDriver();
    }

    /**
     * Configures a ChromeDriver based on the browserConfig
     * @param browserConfig browser config that points to the binary path of the browser and
     * optional profile
     * @return A configured ChromeDriver based on the browserConfig passed in
     */
    public static ChromeDriver getChromeDriver(BrowserConfig browserConfig)
    {
        if (browserConfig != null)
        {
            final ChromeOptions options = new ChromeOptions();
            options.setBinary(browserConfig.getBinaryPath());
            setDefaultArgs(options);
            addCommandLine(options);

            final ChromeDriverService.Builder chromeServiceBuilder = new ChromeDriverService.Builder();
            setChromeServicePath(browserConfig, chromeServiceBuilder);
            setEnvironment(chromeServiceBuilder);
            chromeServiceBuilder.usingAnyFreePort();
            ChromeDriverService chromeDriverService = chromeServiceBuilder.build();
            return new ChromeDriver(chromeDriverService, options);
        }

        // Fall back on default chrome driver
        return getChromeDriver();
    }

    /**
     * Gets a chrome driver based on the browser path based in
     * @param browserPath the path to the chrome binary to use for the chrome driver.
     * @return A ChromeDriver that is using the binary at the browserPath
     */
    public static ChromeDriver getChromeDriver(String browserPath)
    {
        if (browserPath != null)
        {
            final ChromeOptions options = new ChromeOptions();
            options.setBinary(browserPath);
            return new ChromeDriver(options);
        }
        else
        {
            // Fall back on default chrome driver
            log.info("Browser path was null, falling back to default chrome driver.");
            return getChromeDriver();
        }
    }

    private static void setDefaultArgs(ChromeOptions options) {
        // as of Chrome 30+ setuid based sandbox doesn't work on Linux due to permission issues
        options.addArguments("--disable-setuid-sandbox");
    }

    private static void setChromeServicePath(BrowserConfig browserConfig, ChromeDriverService.Builder chromeServiceBuilder)
    {
        if (browserConfig.getProfilePath() != null)
        {
            File profilePath = new File(browserConfig.getProfilePath());
            File chromeDriverFile = new File(profilePath, "chromedriver");
            if (chromeDriverFile.exists())
            {
                chromeServiceBuilder.usingDriverExecutable(chromeDriverFile);
            }
        }
    }

    /**
     * Add command line options if provided via system property {@literal webdriver.chrome.switches}.
     *
     */
    private static void addCommandLine(final ChromeOptions options)
    {
        String[] switches = StringUtils.split(System.getProperty("webdriver.chrome.switches"), ",");
        if(switches != null && switches.length > 0) {
            List<String> switchList = Arrays.asList(switches);
            log.info("Setting command line arguments for Chrome: " + switchList);
            options.addArguments(switchList);
        }
    }

    /**
     * Sets up system properties on the chrome driver service.
     * @param chromeDriverServiceBuilder the chrome driver service to set environment map on.
     */
    private static void setEnvironment(ChromeDriverService.Builder chromeDriverServiceBuilder)
    {
        Map<String, String> env = Maps.newHashMap();
        if (System.getProperty("DISPLAY") != null)
        {
            env.put("DISPLAY", System.getProperty("DISPLAY"));
        }
        chromeDriverServiceBuilder.withEnvironment(env);
    }

}
