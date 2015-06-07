package com.atlassian.webdriver.jira.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.page.WebSudoPage;

import javax.inject.Inject;

/**
 * The Jira WebSudo page
 */
public class JiraWebSudoPage implements WebSudoPage
{
    @Inject
    private PageBinder pageBinder;

    @ElementBy(id="login-form-authenticatePassword")
    private PageElement passwordTextbox;

    @ElementBy(id="authenticateButton")
    private PageElement confirmButton;

    public String getUrl()
    {
        return "/secure/admin/WebSudoAuthenticate!default.jspa";
    }

    public <T extends Page> T confirm(Class<T> targetPage)
    {
        return confirm("admin", targetPage);
    }

    public <T extends Page> T confirm(String password, Class<T> targetPage)
    {
        passwordTextbox.type(password);
        confirmButton.click();
        return pageBinder.navigateToAndBind(targetPage);
    }


}
