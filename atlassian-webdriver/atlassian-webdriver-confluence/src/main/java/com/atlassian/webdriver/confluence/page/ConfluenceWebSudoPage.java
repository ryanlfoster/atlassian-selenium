package com.atlassian.webdriver.confluence.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.page.WebSudoPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.inject.Inject;

/**
 * Confluence WebSudo page
 */
public class ConfluenceWebSudoPage implements WebSudoPage
{
    private static final String URI = "/authenticate.action";

    @Inject
    private PageBinder pageBinder;

    @FindBy(id="password")
    private WebElement passwordTextbox;

    @FindBy(id="authenticateButton")
    private WebElement confirmButton;

    public String getUrl()
    {
        return URI;
    }
    
    public <T extends Page> T confirm(Class<T> targetPage)
    {
        return confirm("admin", targetPage);
    }

    public <T extends Page> T confirm(String password, Class<T> targetPage)
    {
        passwordTextbox.sendKeys(password);
        confirmButton.click();
        return pageBinder.navigateToAndBind(targetPage);
    }


}
