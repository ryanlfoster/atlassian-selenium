package it.com.atlassian.webdriver.jira.test;

import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.jira.JiraTestedProduct;
import com.atlassian.webdriver.jira.component.header.JiraHeader;
import com.atlassian.webdriver.jira.page.DashboardPage;
import com.atlassian.webdriver.jira.page.LogoutPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class TestJiraHeader
{
    private static final JiraTestedProduct JIRA = TestedProductFactory.create(JiraTestedProduct.class);

    @Test
    public void testLoginInfo()
    {
        JiraHeader header = JIRA.gotoLoginPage().loginAsSysAdmin(DashboardPage.class).getHeader();

        assertTrue(header.isAdmin());
        assertTrue(header.isLoggedIn());
    }

    @Test
    public void testLogout()
    {
        JiraHeader header = JIRA.gotoHomePage().getHeader();
        assertFalse(header.isAdmin());
        assertFalse(header.isLoggedIn());
    }

    @Test
    public void testHeaderLogout()
    {
        JiraHeader header = JIRA.gotoLoginPage().loginAsSysAdmin(DashboardPage.class).getHeader();
        header = header.logout(DashboardPage.class).getHeader();

        assertFalse(header.isLoggedIn());
    }

    @After
    @Before
    public void logout()
    {
        if (JIRA.gotoHomePage().isLoggedIn())
        {
            JIRA.getPageBinder().navigateToAndBind(LogoutPage.class).confirmLogout();
        }
    }

}
