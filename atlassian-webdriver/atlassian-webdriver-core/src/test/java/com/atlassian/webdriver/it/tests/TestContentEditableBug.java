package com.atlassian.webdriver.it.tests;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.webdriver.it.AbstractSimpleServerTest;
import com.atlassian.webdriver.it.pageobjects.page.contenteditable.ContentEditablePage;
import com.atlassian.webdriver.utils.element.WebDriverPoller;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;

import static com.atlassian.webdriver.utils.element.WebElementMatchers.withTextThat;
import static org.hamcrest.Matchers.containsString;

@IgnoreBrowser(value = {Browser.HTMLUNIT, Browser.HTMLUNIT_NOJS}, reason = "SELENIUM-165 HtmlUnit does not support contenteditable")
public class TestContentEditableBug extends AbstractSimpleServerTest
{
    @Inject
    private WebDriver driver;

    @Inject
    private WebDriverPoller poller;

    private ContentEditablePage contentEditablePage;

    @Before
    public void init()
    {
        contentEditablePage = product.visit(ContentEditablePage.class);
    }

    @Test
    @IgnoreBrowser(value = { Browser.HTMLUNIT, Browser.HTMLUNIT_NOJS })
    public void testEditingContentEditableIframeWorks()
    {
        WebElement contentEditable = contentEditablePage.getContentEditable();
        WebElement element = contentEditable.findElement(By.id("test"));

        element.click();
        element.sendKeys("HELLO");

        poller.waitUntil(element, withTextThat(containsString("HELLO")));
    }

    @Test
    public void testEditingContentEditableDivWorks()
    {
        WebElement element = driver.findElement(By.id("div-ce"));
        element.sendKeys("WORLD");

        poller.waitUntil(element, withTextThat(containsString("WORLD")));
    }

}
