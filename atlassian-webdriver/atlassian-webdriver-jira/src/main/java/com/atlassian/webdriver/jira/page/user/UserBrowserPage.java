package com.atlassian.webdriver.jira.page.user;

import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.jira.data.User;
import com.atlassian.webdriver.jira.page.JiraAdminAbstractPage;
import com.atlassian.webdriver.utils.by.ByJquery;
import com.atlassian.webdriver.utils.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Page object implementation for the User browser page in JIRA.
 * TODO: Handle pagination when there are more users
 */
public class UserBrowserPage extends JiraAdminAbstractPage
{
    private static final String URI = "/secure/admin/user/UserBrowser.jspa";

    private String MAX = "1000000";
    private String TEN = "10";
    private String TWENTY = "20";
    private String FIFTY = "50";
    private String ONE_HUNDRED = "100";

    private final Map<String, User> users;
    private WebElement filterSubmit;
    
    @FindBy (id = "add_user")
    private WebElement addUserLink;

    @FindBy (xpath = "/html/body/table/tbody/tr/td[3]/table/tbody/tr/td/form/table/tbody/tr[2]/td/p[3]/strong[3]")
    private WebElement numUsers;

    @FindBy (name = "max")
    private WebElement usersPerPageDropdown;

    @FindBy (id = "user_browser_table")
    private WebElement userTable;

    public UserBrowserPage()
    {
        users = new HashMap<String, User>();
    }


    public String getUrl()
    {
        return URI;
    }

    @Init
    public void init()
    {
        filterSubmit = driver.findElement(By.cssSelector("form[name=\"jiraform\"] input[type=\"submit\"]"));

        setUserFilterToShowAllUsers();

        getUsers();
    }

    public boolean hasUser(User user)
    {
        return users.containsKey(user.getUsername());
    }

    /**
     * When editing a users groups from this page, EditUserGroups always returns back to
     * UserBrowser unless there was an error.
     * @param user
     * @return
     */
    public EditUserGroupsPage editUserGroups(User user)
    {

        if (hasUser(user))
        {
            String editGroupsId = "editgroups_" + user.getUsername();

            driver.findElement(By.id(editGroupsId)).click();

            return pageBinder.bind(EditUserGroupsPage.class);
        }
        else
        {
            throw new IllegalStateException("User: " + user.getUsername() + " was not found.");
        }

    }

    public Set<String> getUsersGroups(User user)
    {

        if (hasUser(user))
        {
            Set<String> groups = new HashSet<String>();
            WebElement groupCol = userTable.findElements(ByJquery.$("#" + user.getUsername()).parents("tr.vcard").find("td")).get(4);

            for (WebElement groupEl : groupCol.findElements(By.tagName("a")))
            {
                groups.add(groupEl.getText());
            }

            return groups;
        }
        else
        {
            throw new IllegalStateException("User: " + user.getUsername() + " was not found.");
        }
    }

    public ViewUserPage gotoViewUserPage(User user)
    {
        if (hasUser(user))
        {
            User actualUser = users.get(user.getUsername());
            WebElement userEmailLink = driver.findElement(By.linkText(actualUser.getEmail()));
            userEmailLink.click();

            return pageBinder.bind(ViewUserPage.class);
        }
        else
        {
            throw new IllegalStateException("The user: " + user + " was not found on the page");
        }
    }

    public int getNumberOfUsers()
    {
        return Integer.valueOf(numUsers.getText());
    }

    public UserBrowserPage filterByUserName(String username)
    {
        driver.findElement(By.name("userNameFilter")).sendKeys(username);
        filterSubmit.click();

        return pageBinder.bind(UserBrowserPage.class);
    }

    /**
     * navigates to the addUserPage by activating the add User link
     * @return
     */
    public AddUserPage gotoAddUserPage()
    {
        addUserLink.click();

        return pageBinder.bind(AddUserPage.class);
    }

    /**
     * Takes User object and fills out the addUserPage form and creates the user.
     * @param user the user to create
     * @param sendPasswordEmail sets the send email tick box to on or off
     * @return the user browser page which should have the new user added to the count.
     */
    public ViewUserPage addUser(User user, boolean sendPasswordEmail)
    {

        AddUserPage addUserPage = gotoAddUserPage();

        return addUserPage.setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setConfirmPassword(user.getPassword())
                .setFullname(user.getFullName())
                .setEmail(user.getEmail())
                .sendPasswordEmail(sendPasswordEmail)
                .createUser();
    }

    private void setUserFilterToShowAllUsers()
    {
        WebElement userOption = usersPerPageDropdown.findElement(By.cssSelector("option[value=\"" + MAX + "\"]"));
        if (!userOption.isSelected())
        {
            userOption.click();
        }
        filterSubmit.click();
    }

    private void getUsers()
    {
        users.clear();

        List<WebElement> rows = userTable.findElements(By.tagName("tr"));

        for (WebElement row : rows)
        {
            // Check it's not the headings (th) tags.
            if (Check.elementExists(By.tagName("td"), row))
            {
                List<WebElement> cols = row.findElements(By.tagName("td"));

                String username = cols.get(0).getText();
                String email = cols.get(1).getText();
                String fullName = cols.get(2).getText();

//                Set<Group> groups = new HashSet<Group>();
//
//                for (WebElement group : cols.get(4).findElements(By.tagName("a")))
//                {
//                    groups.add(new Group(group.getText()));
//                }

                users.put(username, new User(username, fullName, email));
            }
        }

    }
}