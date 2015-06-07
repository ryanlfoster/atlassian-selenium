package it.com.atlassian.webdriver.jira.test;

import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.jira.JiraTestedProduct;
import com.atlassian.webdriver.jira.page.ProjectsViewPage;
import org.junit.After;
import org.junit.Test;

/**
 * @since 2.0
 */
public class TestProjectsViewPage
{
    private static final JiraTestedProduct JIRA = TestedProductFactory.create(JiraTestedProduct.class);

    private ProjectsViewPage projectsViewPage;

    @Test
    public void testProjectsViewPage()
    {
        projectsViewPage = JIRA.gotoLoginPage().loginAsSysAdmin(ProjectsViewPage.class);
    }

    @After
    public void cleanUpCookies()
    {
        JIRA.getTester().getDriver().manage().deleteAllCookies();
    }

}
