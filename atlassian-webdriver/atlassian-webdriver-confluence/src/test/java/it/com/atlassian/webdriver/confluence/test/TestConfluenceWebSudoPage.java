package it.com.atlassian.webdriver.confluence.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.component.WebSudoBanner;
import com.atlassian.pageobjects.page.WebSudoPage;
import com.atlassian.webdriver.confluence.page.DashboardPage;
import com.atlassian.webdriver.confluence.page.PeopleDirectoryPage;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 */
public class TestConfluenceWebSudoPage extends AbstractConfluenceWebTest
{
    @Test
    public void testAdministratorAccessPage()
    {
        PeopleDirectoryPage peoplePage = product.visit(WebSudoPage.class).confirm(PeopleDirectoryPage.class);
        assertTrue(peoplePage.hasUser("admin"));
    }

    @Test
    @IgnoreBrowser(value = Browser.HTMLUNIT_NOJS, reason = "Test uses AJAX to drop websudo privileges")
    public void testWebSudoBanner()
    {
        PeopleDirectoryPage peoplePage = product.visit(WebSudoPage.class).confirm(PeopleDirectoryPage.class);
        WebSudoBanner webSudoBanner = peoplePage.getHeader().getWebSudoBanner();
        assertTrue(webSudoBanner.isShowing());

        DashboardPage dashboard = webSudoBanner.dropWebSudo(DashboardPage.class);
        assertFalse(dashboard.getHeader().getWebSudoBanner().isShowing());
    }
}
