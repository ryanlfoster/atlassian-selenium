package com.atlassian.webdriver.it.visualcomparison;

import com.atlassian.selenium.visualcomparison.ScreenElement;
import com.atlassian.webdriver.debug.WebDriverDebug;
import com.atlassian.webdriver.it.AbstractSimpleServerTest;
import com.atlassian.webdriver.it.pageobjects.page.VisualComparisonPage;
import com.atlassian.webdriver.testing.annotation.WindowSize;
import com.atlassian.webdriver.visualcomparison.WebDriverBrowserEngine;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

public class TestWebDriverBrowserEngine extends AbstractSimpleServerTest
{
    @Inject
    WebDriverBrowserEngine engine;

    @Inject
    WebDriverDebug debug;

    @Test
    @WindowSize(width = 1024, height = 768)
    public void testGetElementAtNoScrolling()
    {
        product.visit(VisualComparisonPage.class, 1);
        ScreenElement element = engine.getElementAt(38, 20);
        assertEquals("<span class=\"aui-header-logo-device\">AUI</span>", element.getHtml());
    }

    @Test
    @WindowSize(width = 1024, height = 768)
    public void testGetElementAtWithScrolling()
    {
        product.visit(VisualComparisonPage.class, 1);
        ScreenElement element = engine.getElementAt(530, 1410);
        assertEquals("<input class=\"upfile\" id=\"uploadFile\" name=\"uploadFile\" title=\"upload file\" type=\"file\">",
                element.getHtml());
    }

}
