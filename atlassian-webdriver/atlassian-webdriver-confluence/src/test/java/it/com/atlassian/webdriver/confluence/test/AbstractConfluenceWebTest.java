package it.com.atlassian.webdriver.confluence.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.page.LoginPage;
import com.atlassian.webdriver.confluence.ConfluenceTestedProduct;
import com.atlassian.webdriver.confluence.page.DashboardPage;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import com.atlassian.webdriver.testing.rule.IgnoreBrowserRule;
import com.atlassian.webdriver.testing.rule.SessionCleanupRule;
import com.atlassian.webdriver.testing.rule.TestedProductRule;
import com.atlassian.webdriver.testing.rule.WebDriverScreenshotRule;
import org.junit.Before;
import org.junit.Rule;

/**
 */
@IgnoreBrowser(value = Browser.HTMLUNIT, reason = "HtmlUnit and Raphael.js don't mix")
public abstract class AbstractConfluenceWebTest
{

    @Rule public IgnoreBrowserRule ignoreRule = new IgnoreBrowserRule();
    @Rule public TestedProductRule<ConfluenceTestedProduct> product =
        new TestedProductRule<ConfluenceTestedProduct>(ConfluenceTestedProduct.class);
// enable TestBrowserRule (and make sure it's listed after IgnoreBrowserRule) if you want to be able to switch browsers mid-suite
//    @Rule public TestBrowserRule testBrowserRule = new TestBrowserRule();
    @Rule public WebDriverScreenshotRule webDriverScreenshotRule = new WebDriverScreenshotRule();
    @Rule public SessionCleanupRule sessionCleanupRule = new SessionCleanupRule();

    protected DashboardPage dashboard;

    @Before
    public void login()
    {
        dashboard = product.visit(LoginPage.class).loginAsSysAdmin(DashboardPage.class);
    }

}
