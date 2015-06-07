package it.com.atlassian.webdriver.jira.test;

import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.jira.JiraTestedProduct;
import com.atlassian.webdriver.jira.page.JiraAdminHomePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @since 2.0
 */
public class TestAdminHomePage
{

    private static final JiraTestedProduct JIRA = TestedProductFactory.create(JiraTestedProduct.class);

    private JiraAdminHomePage adminPage;

    @Before
    public void login()
    {
        adminPage = JIRA.gotoLoginPage().loginAsSysAdmin(JiraAdminHomePage.class);
    }

    @After
    public void cleanUpCookies()
    {
        JIRA.getTester().getDriver().manage().deleteAllCookies();
    }

    @Test
    public void testAdminHomePage()
    {
        assertEquals(JIRA.gotoAdminHomePage().getClass(), adminPage.getClass()); 
    }
}
