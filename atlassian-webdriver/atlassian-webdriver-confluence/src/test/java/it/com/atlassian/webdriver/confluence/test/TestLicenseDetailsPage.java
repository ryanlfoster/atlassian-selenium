package it.com.atlassian.webdriver.confluence.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.webdriver.confluence.page.LicenseDetailsPage;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import org.junit.Test;

/**
 */
@IgnoreBrowser(value = {Browser.HTMLUNIT, Browser.HTMLUNIT_NOJS} , reason = "LicenseDetailsPage requires jQuery")
public class TestLicenseDetailsPage extends AbstractConfluenceWebTest
{

    @Test
    public void testLicenseDetailsPage()
    {
        product.visit(LicenseDetailsPage.class);
    }

}
