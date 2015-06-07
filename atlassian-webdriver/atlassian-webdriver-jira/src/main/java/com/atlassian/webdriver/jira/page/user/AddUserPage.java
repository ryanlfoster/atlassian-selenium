package com.atlassian.webdriver.jira.page.user;

import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.jira.page.JiraAdminAbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 2.0
 */
public class AddUserPage extends JiraAdminAbstractPage
{
    private static String URI = "/secure/admin/user/AddUser!default.jspa";

    @FindBy (name ="username")
    private WebElement usernameField;

    @FindBy (name = "password")
    private WebElement passwordField;

    @FindBy (name = "confirm")
    private WebElement confirmPasswordField;

    @FindBy (name = "fullname")
    private WebElement fullnameField;

    @FindBy (name = "email")
    private WebElement emailField;

    @FindBy (name = "sendEmail")
    private WebElement sendEmailCheckbox;

    @FindBy (id = "create_submit")
    private WebElement createButton;

    @FindBy (id = "cancelButton")
    private WebElement cancelButton;

    private Set<String> errors = new HashSet<String>();

    public String getUrl()
    {
        return URI;
    }

    @Init
    public void checkForErrors()
    {
        List<WebElement> errorElements = driver.findElements(By.className("errMsg"));

        for (WebElement errEl : errorElements)
        {
            errors.add(errEl.getText());
        }
    }

    public AddUserPage setUsername(String username)
    {
        usernameField.sendKeys(username);
        return this;
    }

    public AddUserPage setPassword(String password)
    {
        passwordField.sendKeys(password);
        return this;
    }

    public AddUserPage setConfirmPassword(String password)
    {
        confirmPasswordField.sendKeys(password);
        return this;
    }

    public AddUserPage setFullname(String fullname)
    {
        fullnameField.sendKeys(fullname);
        return this;
    }

    public AddUserPage setEmail(String email)
    {
        emailField.sendKeys(email);

        return this;
    }

    public AddUserPage sendPasswordEmail(boolean sendPasswordEmail)
    {
        if (sendEmailCheckbox.isSelected())
        {
            if (!sendPasswordEmail)
            {
                sendEmailCheckbox.click();
            }
        }
        else
        {
            if (sendPasswordEmail)
            {
                sendEmailCheckbox.click();
            }
        }

        return this;
    }

    public ViewUserPage createUser()
    {
        createButton.click();

        return pageBinder.bind(ViewUserPage.class);
    }

    public AddUserPage createUserExpectingError()
    {
        createButton.click();

        return pageBinder.bind(AddUserPage.class);
    }

    public UserBrowserPage cancelCreateUser()
    {
        cancelButton.click();

        return pageBinder.bind(UserBrowserPage.class);
    }

    public boolean hasError()
    {
        return errors.size() > 0;
    }
}
