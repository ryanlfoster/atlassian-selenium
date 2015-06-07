package com.atlassian.webdriver.debug;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test case for {@link WebDriverDebug}.
 *
 * @since 2.2
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWebDriverDebug
{

    private WebDriver mockDriver;
    private WebDriver mockWrappingDriver;
    private WebDriver mockDriverTakingScreenshot;
    private WebDriver mockDriverNotTakingScreenshot;

    @Rule public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void initDrivers()
    {
        mockDriver = mock(WebDriver.class);
        mockDriverTakingScreenshot = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
        mockDriverNotTakingScreenshot = mock(WebDriver.class);
        mockWrappingDriver = mock(WebDriver.class, withSettings().extraInterfaces(WrapsDriver.class));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldNotAcceptNullDriver()
    {
        new WebDriverDebug(null);

    }

    @Test
    public void shouldReturnCurrentUrl()
    {
        when(mockDriver.getCurrentUrl()).thenReturn("http://some-url.com");
        assertEquals("http://some-url.com", new WebDriverDebug(mockDriver).getCurrentUrl());

    }

    @Test
    public void dumpPageSourceToNonExistingFile() throws IOException
    {
        final File dumpDir = temporaryFolder.newFolder("TestWebDriverDebug");
        final File dumpFile = new File(dumpDir, "test.html");
        when(mockDriver.getPageSource()).thenReturn("<html>Awesome</html>");
        final WebDriverDebug debug = new WebDriverDebug(mockDriver);
        assertTrue("Should dump HTML source successfully", debug.dumpSourceTo(dumpFile));
        assertEquals("<html>Awesome</html>", readFileToString(dumpFile));
    }

    @Test
    public void dumpPageSourceToExistingFile() throws IOException
    {
        final File dumpFile = temporaryFolder.newFile("test.html");
        writeStringToFile(dumpFile, "blah blah");
        when(mockDriver.getPageSource()).thenReturn("<html>Awesome no blah</html>");
        final WebDriverDebug debug = new WebDriverDebug(mockDriver);
        assertTrue("Should dump HTML source successfully", debug.dumpSourceTo(dumpFile));
        // should overwrite the previous contents
        assertEquals("<html>Awesome no blah</html>", readFileToString(dumpFile));
    }

    @Test
    public void dumpPageShouldNotThrowExceptionOnIoError() throws IOException
    {
        final File dumpFile = temporaryFolder.newFile("test.html");
        assertTrue(dumpFile.setWritable(false));
        when(mockDriver.getPageSource()).thenReturn("<html>Awesome</html>");
        final WebDriverDebug debug = new WebDriverDebug(mockDriver);
        assertFalse("Should fail to dump HTML source into non-writable file", debug.dumpSourceTo(dumpFile));
    }

    @Test
    public void dumpPageSourceToWriterShouldWorkGivenWorkingWriter()
    {
        when(mockDriver.getPageSource()).thenReturn("<html>Awesome</html>");
        final WebDriverDebug debug = new WebDriverDebug(mockDriver);
        final StringWriter output = new StringWriter();
        assertTrue("Should dump HTML source successfully", debug.dumpPageSourceTo(output));
        assertEquals("<html>Awesome</html>", output.toString());
    }

    @Test
    public void shouldTakeScreenshotGivenDriverTakesScreenshot() throws IOException
    {
        final File dumpDir = temporaryFolder.newFolder("TestWebDriverDebug");
        final File fakeScreenshot = temporaryFolder.newFile("fake.png");
        final File testOutput = new File(dumpDir, "test.png");
        writeStringToFile(fakeScreenshot, "FAKE SCREENSHOT!");
        when(mockDriverTakingScreenshot().getScreenshotAs(OutputType.FILE)).thenReturn(fakeScreenshot);
        final WebDriverDebug debug = new WebDriverDebug(mockDriverTakingScreenshot);
        assertTrue(debug.takeScreenshotTo(testOutput));
        assertEquals("FAKE SCREENSHOT!", readFileToString(testOutput));
    }

    @Test
    public void shouldTakeScreenshotGivenUnderlyingDriverTakesScreenshot() throws IOException
    {
        final File dumpDir = temporaryFolder.newFolder("TestWebDriverDebug");
        final File fakeScreenshot = temporaryFolder.newFile("fake.png");
        final File testOutput = new File(dumpDir, "test.png");
        writeStringToFile(fakeScreenshot, "FAKE SCREENSHOT!");
        when(mockDriverTakingScreenshot().getScreenshotAs(OutputType.FILE)).thenReturn(fakeScreenshot);
        stubWrappedDriver(mockDriverTakingScreenshot);
        final WebDriverDebug debug = new WebDriverDebug(mockWrappingDriver);
        assertTrue(debug.takeScreenshotTo(testOutput));
        assertEquals("FAKE SCREENSHOT!", readFileToString(testOutput));
    }

    @Test
    public void shouldNotTakeScreenshotGivenNoUnderlyingDriverTakesScreenshot() throws IOException
    {
        final File dumpDir = temporaryFolder.newFolder("TestWebDriverDebug");
        final File fakeScreenshot = temporaryFolder.newFile("fake.png");
        final File testOutput = new File(dumpDir, "test.png");
        writeStringToFile(fakeScreenshot, "FAKE SCREENSHOT!");
        when(mockDriverTakingScreenshot().getScreenshotAs(OutputType.FILE)).thenReturn(fakeScreenshot);
        stubWrappedDriver(mockDriverNotTakingScreenshot);
        final WebDriverDebug debug = new WebDriverDebug(mockWrappingDriver);
        assertFalse("Screenshot should not be taken", debug.takeScreenshotTo(testOutput));
    }

    @Test
    public void shouldNotTakeScreenshotGivenIoError() throws IOException
    {
        final File fakeScreenshot = temporaryFolder.newFile("fake.png");
        final File testOutput = temporaryFolder.newFile("output.png");
        assertTrue(testOutput.setWritable(false));
        writeStringToFile(fakeScreenshot, "FAKE SCREENSHOT!");
        when(mockDriverTakingScreenshot().getScreenshotAs(OutputType.FILE)).thenReturn(fakeScreenshot);
        stubWrappedDriver(mockDriverTakingScreenshot);
        final WebDriverDebug debug = new WebDriverDebug(mockWrappingDriver);
        assertFalse("Screenshot should not be taken given output file not writable", debug.takeScreenshotTo(testOutput));
    }


    private void stubWrappedDriver(WebDriver wrappedDriver)
    {
        final WrapsDriver wrapsDriver = (WrapsDriver) mockWrappingDriver;
        when(wrapsDriver.getWrappedDriver()).thenReturn(wrappedDriver);
    }

    private TakesScreenshot mockDriverTakingScreenshot()
    {
        return (TakesScreenshot) mockDriverTakingScreenshot;
    }
}
