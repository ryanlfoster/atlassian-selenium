package com.atlassian.webdriver.jira.page;


import com.atlassian.pageobjects.binder.WaitUntil;
import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.page.HomePage;
import com.atlassian.webdriver.jira.component.dashboard.Gadget;
import com.atlassian.webdriver.jira.component.header.JiraHeader;
import org.openqa.selenium.By;

/**
 * Page object implementation for the Dashbaord page in JIRA.
 * TODO: extend to handle more operations like addGadget.
 */
public class DashboardPage extends JiraAbstractPage implements HomePage<JiraHeader>
{
    private static final String URI = "/secure/Dashboard.jspa";

    /**
     * TODO: fix this.
     */
    public boolean canAddGadget()
    {
        return false;
    }

    public Gadget getGadget(String gadgetId)
    {
        return pageBinder.bind(Gadget.class, gadgetId);
    }

    @WaitUntil
    @IgnoreBrowser(value = Browser.HTMLUNIT_NOJS, reason = "layout is added via javascript")
    public void waitForLayout()
    {
        driver.waitUntilElementIsLocated(By.className("layout"));
    }

    public String getUrl()
    {
        return URI;
    }
}