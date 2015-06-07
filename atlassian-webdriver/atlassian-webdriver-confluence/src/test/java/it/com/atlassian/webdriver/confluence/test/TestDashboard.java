package it.com.atlassian.webdriver.confluence.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.webdriver.confluence.page.ConfluenceAdminHomePage;
import com.atlassian.webdriver.confluence.page.DashboardPage;
import com.atlassian.webdriver.confluence.page.LogoutPage;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class TestDashboard extends AbstractConfluenceWebTest
{

    @Test
    public void testDashboard()
    {
        DashboardPage dashboard = product.getTestedProduct().gotoHomePage();
        assertTrue(dashboard.isAdmin());
        assertTrue(dashboard.isLoggedIn());

        product.visit(LogoutPage.class);
    }

    @Test
    @IgnoreBrowser(Browser.HTMLUNIT_NOJS)
    public void testDashboardMenu()
    {
        DashboardPage dashboard = product.getTestedProduct().gotoHomePage();
        ConfluenceAdminHomePage adminPage = dashboard.getBrowseMenu().open().gotoAdminPage();

        LogoutPage logoutPage = adminPage.getUserMenu().open().logout();

        assertFalse(logoutPage.isLoggedIn());

    }

}
