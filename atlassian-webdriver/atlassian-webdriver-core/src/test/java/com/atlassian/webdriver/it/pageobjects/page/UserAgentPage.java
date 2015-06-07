package com.atlassian.webdriver.it.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.binder.WaitUntil;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @since 2.1.0
 */
public class UserAgentPage implements Page
{
    @Inject
    AtlassianWebDriver driver;

    @FindBy(id = "user-agent")
    WebElement userAgent;

    public String getUrl()
    {
        return "/html/user-agent.html";
    }

    @WaitUntil
    public void waitUntilLoaded()
    {
        driver.waitUntilElementIsLocated(By.tagName("h1"));
    }

    public String getUserAgent()
    {
        return userAgent.getText();
    }
}
