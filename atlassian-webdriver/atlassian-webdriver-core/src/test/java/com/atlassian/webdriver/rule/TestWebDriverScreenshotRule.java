package com.atlassian.webdriver.rule;

import com.atlassian.webdriver.testing.rule.WebDriverScreenshotRule;
import com.google.common.base.Suppliers;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

import static com.atlassian.webdriver.matchers.FileMatchers.*;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test case for {@link com.atlassian.webdriver.testing.rule.WebDriverScreenshotRule}.
 *
 * @since 2.1
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWebDriverScreenshotRule
{

    @Mock private Statement mockTest;

    private WebDriver webDriver;

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void initWebDriver()
    {
        webDriver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
    }

    @Test
    public void shouldTakeScreenshotAndPageDump() throws Throwable
    {
		final String fakeScreenshotContent = "FAKE SCREENSHOT!";
		when(asTakingScreenshot().getScreenshotAs(OutputType.FILE)).thenReturn(prepareFakeScreenshot("fake.png", fakeScreenshotContent));
		when(webDriver.getCurrentUrl()).thenReturn("something");
		final String pageSource = "<html>some source</html>";
		when(webDriver.getPageSource()).thenReturn(pageSource);
        doThrow(new RuntimeException("failed")).when(mockTest).evaluate();
        final WebDriverScreenshotRule rule = createRule();
        final Description description = Description.createTestDescription(TestWebDriverScreenshotRule.class, "someTest");
        new SafeStatementInvoker(rule.apply(mockTest, description)).invokeSafely();
        assertThat(expectedTargetDir(), isDirectory());
		assertFilesGeneratedProperly(fakeScreenshotContent, pageSource, "someTest.html", "someTest.png");
    }

	private File prepareFakeScreenshot(final String fileName, final String data) throws IOException {
		final File fakeScreenshot = temporaryFolder.newFile(fileName);
		FileUtils.writeStringToFile(fakeScreenshot, data);
		return fakeScreenshot;
	}

	@Test
	public void shouldNotOverrideScreenshotAndPageDump() throws Throwable {
		final String[] screenshotContents = {"Fake Screenshot 1", "Fake Screenshot 2", "Fake Screenshot 3"};
		when(asTakingScreenshot().getScreenshotAs(OutputType.FILE))
				.thenReturn(prepareFakeScreenshot("fake-1.png", screenshotContents[0]))
				.thenReturn(prepareFakeScreenshot("fake-2.png", screenshotContents[1]))
				.thenReturn(prepareFakeScreenshot("fake-3.png", screenshotContents[2]));

		when(webDriver.getCurrentUrl()).thenReturn("something");
		final String[] sourceConents = {"<html>source 1</html>", "<html>source 2</html>", "<html>source 3</html>"};
		when(webDriver.getPageSource()).thenReturn(sourceConents[0]).thenReturn(sourceConents[1]).thenReturn(sourceConents[2]);

		doThrow(new RuntimeException("failed")).when(mockTest).evaluate();
		final WebDriverScreenshotRule rule = createRule();
		final Description description = Description.createTestDescription(TestWebDriverScreenshotRule.class, "someTest");

		// test three failures in row
		final String[] expectedFileNames = {"someTest", "someTest-1", "someTest-2"};
		for (int i = 0; i < screenshotContents.length; i++) {
			new SafeStatementInvoker(rule.apply(mockTest, description)).invokeSafely();
			assertThat(expectedTargetDir(), isDirectory());
			final String fn = expectedFileNames[i];
			assertFilesGeneratedProperly(screenshotContents[i], sourceConents[i], fn + ".html", fn + ".png");
		}
	}

	private void assertFilesGeneratedProperly(String firstFakeScreenshotContent, String sourceForFirstTest, String htmlFileName,
			String screenshotFileName) throws IOException {
		final File firstExpectedHtmlFile = expectedTargetFile(htmlFileName);
		final File firstExpectedScreenshotfile = expectedTargetFile(screenshotFileName);

		assertThat(firstExpectedHtmlFile, isFile());
		assertEquals(sourceForFirstTest, readFileToString(firstExpectedHtmlFile));
		assertThat(firstExpectedScreenshotfile, isFile());
		assertEquals(firstFakeScreenshotContent, readFileToString(firstExpectedScreenshotfile));
	}

	@Test
    public void shouldNotTakeScreenshotIfNotFailed() throws Throwable
    {
        when(webDriver.getCurrentUrl()).thenReturn("something");
        when(webDriver.getPageSource()).thenReturn("<html>some source</html>");
        final WebDriverScreenshotRule rule = createRule();
        final Description description = Description.createTestDescription(TestWebDriverScreenshotRule.class, "someTest");
        new SafeStatementInvoker(rule.apply(mockTest, description)).invokeSafely();
        verifyZeroInteractions(webDriver);
        assertThat(expectedTargetDir(), isDirectory()); // should still create the directory
        assertThat(expectedTargetFile("someTest.html"), not(exists()));
        assertThat(expectedTargetFile("someTest.png"), not(exists()));
    }

    private TakesScreenshot asTakingScreenshot()
    {
        return (TakesScreenshot) webDriver;
    }

    private File expectedTargetDir()
    {
        return new File(temporaryFolder.getRoot(), TestWebDriverScreenshotRule.class.getName());
    }

    private File expectedTargetFile(String name)
    {
        return new File(expectedTargetDir(), name);
    }

    private WebDriverScreenshotRule createRule()
    {
    	return new WebDriverScreenshotRule(Suppliers.ofInstance(webDriver))
        		.artifactDir(temporaryFolder.getRoot());
    }
}
