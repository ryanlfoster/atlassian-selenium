package com.atlassian.webdriver.jira.page.user;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.jira.page.JiraAdminAbstractPage;
import com.atlassian.webdriver.utils.Check;
import com.atlassian.webdriver.utils.by.ByJquery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashSet;
import java.util.Set;

/**
 * Page object implementation for the edit user's group page in JIRA. 
 *
 * @since 2.0
 */
public class  EditUserGroupsPage extends JiraAdminAbstractPage
{

    private static final String URI = "/secure/admin/user/EditUserGroups.jspa";
    private static String ERROR_SELECTOR = ".formErrors ul li";

    private Set<String> errors = new HashSet<String>();

    @FindBy (id = "return_link")
    private WebElement returnLink;

    @FindBy (name = "join")
    private WebElement joinButton;

    @FindBy (name = "leave")
    private WebElement leaveButton;

    @FindBy (name = "groupsToJoin")
    private WebElement groupsToJoinSelect;

    @FindBy (name = "groupsToLeave")
    private WebElement groupsToLeaveSelect;

    @FindBy (name = "jiraform")
    private WebElement editGroupsForm;

    public String getUrl()
    {
        return URI;
    }

    @Init
    public void parsePage()
    {

        if (Check.elementExists(ByJquery.$(ERROR_SELECTOR), driver))
        {
            for (WebElement el : driver.findElements(ByJquery.$(ERROR_SELECTOR)))
            {
                errors.add(el.getText());
            }
        }


    }

    public boolean hasErrors()
    {
        return !errors.isEmpty();
    }

    public boolean hasError(String errorStr)
    {
        return errors.contains(errorStr);
    }

    public ViewUserPage returnToUserView()
    {
        returnLink.click();

        return pageBinder.bind(ViewUserPage.class);
    }

    /**
     * Add to groups either redirects the user to another page or returns the user to
     * the EditUserGroupsPage.
     * @param groups
     * @return
     */
    public <T extends Page> T addToGroupsAndReturnToPage(Class<T> pageClass, String ... groups)
    {
        selectGroups(groupsToJoinSelect, groups);

        joinButton.click();

        return pageBinder.bind(pageClass);
    }

    public EditUserGroupsPage addToGroupsExpectingError(String ... groups)
    {
        return addToGroupsAndReturnToPage(EditUserGroupsPage.class, groups);
    }

    public <T extends Page> T removeFromGroupsAndReturnToPage(Class<T> pageClass, String ... groups)
    {
        selectGroups(groupsToLeaveSelect, groups);

        leaveButton.click();

        return pageBinder.bind(pageClass);
    }

    public EditUserGroupsPage removeFromGroupsExpectingError(String ... groups)
    {
        return removeFromGroupsAndReturnToPage(EditUserGroupsPage.class, groups);
    }

    private void selectGroups(WebElement select, String ... groups)
    {
        for (String group : groups)
        {
            String groupSelector = "option[value=" + group + "]";

            if (Check.elementExists(ByJquery.$(groupSelector), select))
            {
                WebElement option = select.findElement(ByJquery.$(groupSelector));
                if (!option.isSelected())
                {
                    option.click();
                }
            }
        }

    }
}
