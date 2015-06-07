package com.atlassian.webdriver.jira.page;

import com.atlassian.pageobjects.binder.WaitUntil;
import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.webdriver.utils.by.ByJquery;
import org.openqa.selenium.By;

/**
 * Page object implementation for the Logout page in JIRA. 
 */
public class LogoutPage extends JiraAbstractPage
{

    private static final String URI = "/secure/Logout.jspa";

    public String getUrl()
    {
        return URI;
    }

    public LogoutPage confirmLogout()
    {
        if (driver.elementExists(By.id("confirm-logout-submit")))
        {
            driver.findElement(By.id("confirm-logout-submit")).click();
            return pageBinder.bind(LogoutPage.class);
        }
        else
        {
            throw new IllegalStateException("Already logged out. Not at the confirm logout page.");
        }
    }

    @WaitUntil
    @IgnoreBrowser(value = Browser.HTMLUNIT_NOJS, reason = "Selector is not possible without jQuery")
    public void waitForLogoutPrompt()
    {
        driver.waitUntilElementIsLocated(ByJquery.$("h2:contains(ogout)")); /*in one page is Logout in the other is logout*/
    }
}