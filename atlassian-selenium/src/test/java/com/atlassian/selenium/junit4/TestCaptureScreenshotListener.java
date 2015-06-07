package com.atlassian.selenium.junit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;

import static org.junit.Assert.assertEquals;

public class TestCaptureScreenshotListener
{
    private CaptureScreenshotListener captureScreenshotListener;

    @Before
    public void setUp() throws Exception
    {
        captureScreenshotListener = new CaptureScreenshotListener();
    }

    @Test
    public void testCreateFileName() throws Exception
    {
        final Description desc = Description.createTestDescription(TestCaptureScreenshotListener.class, "testAnotherTest");
        final String screenshotFileName = captureScreenshotListener.createSreenshotFileName(desc);
        assertEquals("com.atlassian.selenium.junit4.TestCaptureScreenshotListener_testAnotherTest.png", screenshotFileName);
    }
    

    @Test
    public void testCreateFileNameTestNameMissing() throws Exception
    {
       final Description desc = Description.createTestDescription(TestCaptureScreenshotListener.class, "");
       final String screenshotFileName = captureScreenshotListener.createSreenshotFileName(desc);
       assertEquals("com.atlassian.selenium.junit4.TestCaptureScreenshotListener.png", screenshotFileName);
    }


}
