package com.atlassian.webdriver.jira.page.user;

import com.atlassian.pageobjects.Page;
import com.atlassian.webdriver.jira.page.JiraAdminAbstractPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @since 2.0
 */
public class ViewUserPage extends JiraAdminAbstractPage
{
    
    private static final String URI = "/secure/admin/user/ViewUser.jspa";

    @FindBy (id = "username")
    private WebElement username;

    @FindBy (className = "fn")
    private WebElement fullname;

    @FindBy (className = "email")
    private WebElement email;

    @FindBy (id = "loginCount")
    private WebElement loginCount;

    @FindBy (id = "lastLogin")
    private WebElement lastLogin;

    @FindBy (id = "previousLogin")
    private WebElement previousLogin;

    @FindBy (id = "lastFailedLogin")
    private WebElement lastFailedLogin;

    @FindBy (id = "currentFailedLoginCount")
    private WebElement currentFailedLoginCount;

    @FindBy (id = "totalFailedLoginCount")
    private WebElement totalFailedLoginCount;

    @FindBy (id ="deleteuser_link")
    WebElement deleteUserLink;

    @FindBy (id = "editgroups_link")
    WebElement editUserLink;

    // TODO: groups are currently image bullets, don't handle them. Make JIRA change to <ul>.

    public String getUrl()
    {
        return URI;
    }

    public DeleteUserPage gotoDeleteUserPage()
    {
        deleteUserLink.click();

        return pageBinder.bind(DeleteUserPage.class);
    }

    public Page setPassword()
    {
        throw new UnsupportedOperationException("Set password on ViewUserPage is not supported");
    }

    public Page editDetails()
    {
        throw new UnsupportedOperationException("Edit details on ViewUserPage is not supported");
    }

    public Page viewProjectsRoles()
    {
        throw new UnsupportedOperationException("View project roles on ViewUSerPage is not supported");
    }

    public EditUserGroupsPage editGroups()
    {
        editUserLink.click();

        return pageBinder.bind(EditUserGroupsPage.class);
    }

    public Page editProperties()
    {
        throw new UnsupportedOperationException("Edit properties on the ViewUserPage is not supported");
    }

    public Page viewPublicProfile()
    {
        throw new UnsupportedOperationException("View public profile on ViewUserPage is not supported");
    }


    public String getUsername()
    {
        return username.getText();
    }

    public String getFullname()
    {
        return fullname.getText();
    }

    public String getEmail()
    {
        return email.getText();
    }

    public String getLoginCount()
    {
        return loginCount.getText();
    }

    public String getLastLogin()
    {
        return lastLogin.getText();
    }

    public String getPreviousLogin()
    {
        return previousLogin.getText();
    }

    public String getLastFailedLogin()
    {
        return lastFailedLogin.getText();
    }

    public String getCurrentFailedLoginCount()
    {
        return currentFailedLoginCount.getText();
    }

    public String getTotalFailedLoginCount()
    {
        return totalFailedLoginCount.getText();
    }
}
