package com.atlassian.webdriver.jira.page.user;

import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.jira.page.JiraAdminAbstractPage;
import com.atlassian.webdriver.utils.by.ByJquery;
import com.atlassian.webdriver.utils.Check;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashSet;
import java.util.Set;

/**
 * @since 2.0
 */
public class DeleteUserPage extends JiraAdminAbstractPage
{
    private static String URI = "/secure/admin/user/DeleteUser.jspa";

    private static String ERROR_SELECTOR = ".formErrors ul li";

    @FindBy (id = "numberOfFilters")
    private WebElement numberOfSharedFiltersElement;

    @FindBy (id = "numberOfOtherFavouritedFilters")
    private WebElement numberOfOtherSharedFiltersElement;

    @FindBy (id = "numberOfNonPrivatePortalPages")
    private WebElement numberOfSharedDashboardsElement;

    @FindBy (id = "numberOfOtherFavouritedPortalPages")
    private WebElement numberOfOtherFavoriteDashboardsElement;

    @FindBy (id = "delete_submit")
    private WebElement deleteUserButton;

    @FindBy (id = "cancelButton")
    private WebElement cancelDeleteUserButton;

    private WebElement numberOfAssignedIssuesElement;
    private WebElement numberOfReportedIssuesElement;

    private Set<String> errors = new HashSet <String>();

    public String getUrl()
    {
        return URI;
    }

    @Init
    public void parsePage()
    {
        //Check for errors on the page
        if (Check.elementExists(ByJquery.$(ERROR_SELECTOR), driver))
        {
            for (WebElement el : driver.findElements(ByJquery.$(ERROR_SELECTOR)))
            {
                errors.add(el.getText());
            }
        }

        numberOfAssignedIssuesElement = driver.findElement(ByJquery.$("td.fieldLabelArea:contains(Assigned Issues)").siblings("td"));
        numberOfReportedIssuesElement = driver.findElement(ByJquery.$("td.fieldLabelArea:contains(Reported Issues)").siblings("td"));
    }

    public boolean hasErrors()
    {
        return !errors.isEmpty();
    }

    public boolean hasError(String errorString)
    {
        return errors.contains(errorString);
    }

    public UserBrowserPage deleteUser()
    {
        deleteUserButton.click();

        return pageBinder.bind(UserBrowserPage.class);
    }

    public DeleteUserPage deleteUserExpectingError()
    {
        deleteUserButton.click();

        return pageBinder.bind(DeleteUserPage.class);
    }

    public int getNumberOfSharedFiltersElement()
    {
        return getIntValue(numberOfSharedFiltersElement);
    }

    public int getNumberOfOtherSharedFiltersElement()
    {
        return getIntValue(numberOfOtherSharedFiltersElement);
    }

    public int getNumberOfSharedDashboardsElement()
    {
        return getIntValue(numberOfSharedDashboardsElement);
    }

    public int getNumberOfOtherFavoriteDashboardsElement()
    {
        return getIntValue(numberOfOtherFavoriteDashboardsElement);
    }

    public int getDeleteUserButton()
    {
        return getIntValue(deleteUserButton);
    }

    public int getCancelDeleteUserButton()
    {
        return getIntValue(cancelDeleteUserButton);
    }

    public int getNumberOfAssignedIssuesElement()
    {
        return getIntValue(numberOfAssignedIssuesElement);
    }

    public int getNumberOfReportedIssuesElement()
    {
        return getIntValue(numberOfReportedIssuesElement);
    }

    private int getIntValue(WebElement element)
    {
        return Integer.parseInt(element.getText());
    }
}
