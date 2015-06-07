package it.com.atlassian.webdriver.confluence.test;

import com.atlassian.webdriver.confluence.page.ConfluenceAdminHomePage;
import org.junit.Test;

/**
 */
public class TestConfluenceAdminHomePage extends AbstractConfluenceWebTest
{

    @Test
    public void testAdminHomePage()
    {
        product.visit(ConfluenceAdminHomePage.class);
    }

}
