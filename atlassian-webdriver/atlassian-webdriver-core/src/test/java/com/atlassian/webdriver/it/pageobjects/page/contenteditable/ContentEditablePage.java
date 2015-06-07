package com.atlassian.webdriver.it.pageobjects.page.contenteditable;

import com.atlassian.pageobjects.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.inject.Inject;

/**
 * TODO: Document this class / interface here
 *
 * @since v4.4
 */
public class ContentEditablePage implements Page
{
    @Inject
    WebDriver driver;

    @FindBy(name = "iframe-test")
    WebElement iframe;

    public String getUrl()
    {
        return "/html/contenteditable/content-editable.html";
    }

    public WebElement getContentEditable()
    {
        driver.switchTo().frame(iframe);
        return driver.switchTo().activeElement();
    }

    public void leaveContentEditable()
    {
        driver.switchTo().defaultContent();
    }


}
