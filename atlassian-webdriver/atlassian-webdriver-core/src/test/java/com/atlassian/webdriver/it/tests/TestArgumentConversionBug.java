package com.atlassian.webdriver.it.tests;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.webdriver.it.AbstractSimpleServerTest;
import com.atlassian.webdriver.it.pageobjects.page.ArgumentConversionBugPage;
import com.atlassian.webdriver.utils.JavaScriptUtils;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;
import java.util.List;

@IgnoreBrowser(Browser.HTMLUNIT_NOJS)
public class TestArgumentConversionBug extends AbstractSimpleServerTest
{
    @Inject
    private  WebDriver driver;

    @Before
    public void goToPage()
    {
        product.visit(ArgumentConversionBugPage.class);
    }

    // This test is checking that the arg processing bug has been fixed.
    // http://code.google.com/p/selenium/issues/detail?id=1280
    @Test
    public void testDriverDoesNotFailToProcessArgs()
    {
        List<WebElement> els = driver.findElements(By.tagName("div"));
        Object[] args = new Object[] { els };
        JavaScriptUtils.execute("return arguments[0] == null", driver, args);
    }

}
