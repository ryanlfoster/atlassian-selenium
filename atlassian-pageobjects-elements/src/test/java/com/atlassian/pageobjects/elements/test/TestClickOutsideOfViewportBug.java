package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.test.pageobjects.page.BigPageWithButtons;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

@IgnoreBrowser(Browser.HTMLUNIT_NOJS)
public class TestClickOutsideOfViewportBug extends AbstractPageElementBrowserTest
{
    private BigPageWithButtons bigPageWithButtons;

    @Before
    public void init()
    {
        bigPageWithButtons = product.visit(BigPageWithButtons.class);
    }

    @Test
    public void testClickingOnButtonsOutsideOfViewportHorizontal()
    {
        assertTrue("Right button should be present", bigPageWithButtons.getRightButton().isPresent());
        bigPageWithButtons.getRightButton().click();

        // the message should pop-up
        Poller.waitUntilTrue("Message should appear on right button click",
                bigPageWithButtons.getRightButtonClickedMessage().timed().isPresent());
    }

    @Test
    public void testClickingOnButtonsOutsideOfViewportVertical()
    {
        assertTrue("Bottom button should be present", bigPageWithButtons.getBottomButton().isPresent());
        bigPageWithButtons.getBottomButton().click();

        // the message should pop-up
        Poller.waitUntilTrue("Message should appear on bottom button click",
                bigPageWithButtons.getBottomButtonClickedMessage().timed().isPresent());
    }
}
