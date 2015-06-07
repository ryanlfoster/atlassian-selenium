package it.com.atlassian.webdriver.confluence.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.page.LoginPage;
import com.atlassian.webdriver.confluence.ConfluenceTestedProduct;
import com.atlassian.webdriver.confluence.component.header.ConfluenceHeader;
import com.atlassian.webdriver.confluence.page.DashboardPage;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import com.atlassian.webdriver.testing.rule.IgnoreBrowserRule;
import com.atlassian.webdriver.testing.rule.SessionCleanupRule;
import com.atlassian.webdriver.testing.rule.TestedProductRule;
import com.atlassian.webdriver.testing.rule.WebDriverScreenshotRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@IgnoreBrowser(value = {Browser.HTMLUNIT, Browser.HTMLUNIT_NOJS}, reason = "HtmlUnit and Raphael.js don't mix, logout via the header requires javascript")
// doesn't inherit from AbstractConfluenceWebTest so it doesn't log in before each test
public class TestConfluenceHeader
{
    @Rule
    public IgnoreBrowserRule ignoreRule = new IgnoreBrowserRule();
    @Rule public TestedProductRule<ConfluenceTestedProduct> product =
        new TestedProductRule<ConfluenceTestedProduct>(ConfluenceTestedProduct.class);
    // enable TestBrowserRule (and make sure it's listed after IgnoreBrowserRule) if you want to be able to switch browsers mid-suite
//    @Rule public TestBrowserRule testBrowserRule = new TestBrowserRule();
    @Rule public WebDriverScreenshotRule webDriverScreenshotRule = new WebDriverScreenshotRule();
    @Rule public SessionCleanupRule sessionCleanupRule = new SessionCleanupRule();

    @Test
    public void testLoginInfo()
    {
        ConfluenceHeader header = product.getTestedProduct().gotoLoginPage().loginAsSysAdmin(DashboardPage.class).getHeader();

        assertTrue(header.isAdmin());
        assertTrue(header.isLoggedIn());
    }

    @Test
    public void testLogout()
    {
        ConfluenceHeader header = product.getTestedProduct().gotoHomePage().getHeader();
        assertFalse(header.isAdmin());
        assertFalse(header.isLoggedIn());
    }

    @After
    @Before
    public void logout()
    {
        product.getTestedProduct().gotoHomePage().getHeader().logout(LoginPage.class);
    }
}
