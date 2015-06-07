package it.com.atlassian.webdriver.jira.test;

import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.pageobjects.page.LoginPage;
import com.atlassian.webdriver.jira.JiraTestedProduct;
import com.atlassian.webdriver.jira.page.user.AddUserPage;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 */
public class TestAddUserPage
{
    private static final JiraTestedProduct JIRA = TestedProductFactory.create(JiraTestedProduct.class);

    /**
     * Test for SELENIUM-99
     */
    @Test
    public void testAddUserWithErrors()
    {
        AddUserPage page = JIRA.visit(LoginPage.class).loginAsSysAdmin(AddUserPage.class);

        page.setUsername("admin")
            .setPassword("admin")
            .setConfirmPassword("admin")
            .setEmail("admin@atlassian.com")
            .setFullname("Administrator");

        assertTrue(page.createUserExpectingError().hasError());
    }
}
