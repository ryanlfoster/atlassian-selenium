package com.atlassian.webdriver.jira.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.page.HomePage;
import com.atlassian.pageobjects.page.LoginPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * Page object implementation for the LoginPage in JIRA.
 */
public class JiraLoginPage extends JiraAbstractPage implements LoginPage
{
    private static final String URI = "/login.jsp";

    @FindBy (name = "os_username")
    private WebElement usernameField;

    @FindBy (name = "os_password")
    private WebElement passwordField;

    @FindBy (name = "os_cookie")
    private WebElement rememberMeTickBox;

    @FindBy (how = How.ID_OR_NAME, using = "login")
    private WebElement loginButton;

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

        loginButton.click();

        return HomePage.class.isAssignableFrom(nextPage) ? pageBinder.bind(nextPage) : pageBinder.navigateToAndBind(nextPage);
    }


}