package it.com.atlassian.webdriver.jira.test;

import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.jira.JiraTestedProduct;
import com.atlassian.webdriver.jira.page.DashboardPage;
import com.atlassian.webdriver.jira.page.ViewAttachmentsSettingsPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @since 2.0
 */
public class TestViewAttachmentsSettingsPage
{

    private static final JiraTestedProduct JIRA = TestedProductFactory.create(JiraTestedProduct.class);

    private DashboardPage dashboard;

    @Before
    public void login()
    {
        dashboard = JIRA.gotoLoginPage().loginAsSysAdmin(DashboardPage.class);
    }

    @After
    public void cleanUpCookies()
    {
        JIRA.getTester().getDriver().manage().deleteAllCookies();
    }

    @Test
    public void testViewAttachmentSettingsPage()
    {
        JIRA.getPageBinder().navigateToAndBind(ViewAttachmentsSettingsPage.class);
    }

}
