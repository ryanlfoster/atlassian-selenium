package com.atlassian.webdriver.confluence.page;

import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.confluence.component.macro.UserMacro;
import com.atlassian.webdriver.utils.by.ByJquery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class PeopleDirectoryPage extends ConfluenceAbstractPage
{
    private static final String URI = "/browsepeople.action";

    @FindBy (linkText = "All People")
    private WebElement allPeopleLink;

    @FindBy (linkText = "People with Personal Spaces")
    private WebElement peopleWithPersonalSpacesLink;

    private Map<String, UserMacro> users = new HashMap<String, UserMacro>();


    public String getUrl()
    {
        return URI;
    }

    @Init
    public void parseUsers()
    {
        for (WebElement profile : driver.findElements(By.className("profile-macro")))
        {
            UserMacro userMacro = pageBinder.bind(UserMacro.class, By.className("vcard"), profile);
            users.put(userMacro.getUsername(), userMacro);
        }
    }

    /**
     * @return a set which contains all the usernames on the page.
     */
    public Set<String> getAllUsernames()
    {
        return users.keySet();
    }

    public PeopleDirectoryPage showAllPeople()
    {
        if (!isShowingAllPeople())
        {
            allPeopleLink.click();
        }

        return pageBinder.bind(PeopleDirectoryPage.class);
    }

    public PeopleDirectoryPage showAllPeopleWithPersonalSpaces()
    {

        if (!isShowingPeopleWithPersonalSpaces())
        {
            peopleWithPersonalSpacesLink.click();
        }

        return pageBinder.bind(PeopleDirectoryPage.class);

    }

    public boolean hasUser(String username)
    {
        return users.containsKey(username);
    }

    public UserMacro getUserMacro(String username)
    {
        return hasUser(username) ? users.get(username) : null;
    }

    public boolean isShowingAllPeople()
    {
        return driver.elementExists(ByJquery.$("div.greybox p strong:contains(All People)"));
    }

    public boolean isShowingPeopleWithPersonalSpaces()
    {
        return driver.elementExists(ByJquery.$("div.greybox p strong:contains(People with Personal Spaces)"));
    }
}
