package com.atlassian.selenium.junit4;

import com.atlassian.selenium.Browser;
import com.atlassian.selenium.SeleniumClient;
import com.atlassian.selenium.SeleniumConfiguration;
import com.atlassian.selenium.SeleniumStarter;
import org.apache.commons.lang.StringUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This is a {@link org.junit.runner.notification.RunListener} and captures a screenshot when a test fails.
 * It writes the screenshot into the output directory configured for the maven surefire plugin. The location of this directory
 * is exposed via the system property 'reportsDirectory' by the AMPS IntegrationTestMojo.
 * 
 * It will only take a screenshot if the browser is FireFox. Selenium requires for IE a plugin to be able to take screenshots.
 *
 * @since 2.0
 */
public class CaptureScreenshotListener extends RunListener
{
    public CaptureScreenshotListener()
    {
        super();
    }

    @Override
    public void testFailure(final Failure failure) throws Exception
    {
        //This is the output directory configured for the maven surefire plugin.
        final String reportsDirectory = System.getProperty("reportsDirectory");
        if (!StringUtils.isEmpty(reportsDirectory))
        {
            try
            {
                //Can throw NPE, if selenium client is not initialised yet.
                final SeleniumClient seleniumClient = SeleniumStarter.getInstance().getSeleniumClient((SeleniumConfiguration) null);
                if (seleniumClient.getBrowser().equals(Browser.FIREFOX))
                {
                    //Default background color is WHITE
                    seleniumClient.captureEntirePageScreenshot(reportsDirectory + "/" + createSreenshotFileName(failure.getDescription()), "background=#FFFFFF");
                }
            }
            catch (Exception e)
            {
                //ignored
            }
        }
    }

    protected String createSreenshotFileName(Description description)
    {
        final String displayName = description.getDisplayName();
        int startIndex = displayName.indexOf("(");
        int endIndex = displayName.lastIndexOf(")");
        final String testName = displayName.substring(0, startIndex);

        String filename;
        if (startIndex != -1 && endIndex != -1)
        {
            filename = displayName.substring(startIndex + 1, endIndex);
            if (!StringUtils.isEmpty(testName))
            {
                 filename += "_" + testName;
            }
        }
        else
        {
            filename = description.getDisplayName();
        }

        try
        {
            filename = URLEncoder.encode(filename, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            //ignored
        }
        return filename + ".png";
    }
}