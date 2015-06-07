package com.atlassian.webdriver.confluence.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.page.HomePage;
import com.atlassian.pageobjects.page.LoginPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

// TODO: Add the currently logged in logic.

/**
 * Page object implementation for the LoginPage in Confluence.
 */
public class ConfluenceLoginPage extends ConfluenceAbstractPage implements LoginPage
{
    public static final String URI = "/login.action";

    @FindBy (id = "os_username")
    private WebElement usernameField;

    @FindBy (id = "os_password")
    private WebElement passwordField;

    @FindBy (id = "os_cookie")
    private WebElement rememberMeTickBox;

    @FindBy (name = "loginform")
    private WebElement loginForm;

    public String getUrl()
    {
        return URI;
    }

    public <M extends Page> M login(String username, String password, Class<M> nextPage)
    {
        return login(username, password, false, nextPage);
    }

    public <M extends Page> M loginAsSysAdmin(Class<M> nextPage)
    {
        return login("admin", "admin", nextPage);
    }

    public <M extends Page> M login(String username, String password, boolean rememberMe, Class<M> nextPage)
    {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        if (rememberMe)
        {
            rememberMeTickBox.click();
        }      

        loginForm.submit();

        return HomePage.class.isAssignableFrom(nextPage) ? pageBinder.bind(nextPage) : pageBinder.navigateToAndBind(nextPage);
    }

}
