package it.com.atlassian.webdriver.jira.test;

import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.jira.JiraTestedProduct;
import com.atlassian.webdriver.jira.page.DashboardPage;
import com.atlassian.webdriver.jira.page.LicenseDetailsPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @since 2.0
 */
public class TestLicenseDetailsPage
{

    private static final JiraTestedProduct JIRA = TestedProductFactory.create(JiraTestedProduct.class);

    @Before
    public void login()
    {
       JIRA.gotoLoginPage().loginAsSysAdmin(DashboardPage.class);
    }

    @After
    public void cleanUpCookies()
    {
        JIRA.getTester().getDriver().manage().deleteAllCookies();
    }

    @Test
    public void testLicenseDetailsPage()
    {
        JIRA.getPageBinder().navigateToAndBind(LicenseDetailsPage.class);
    }

}
