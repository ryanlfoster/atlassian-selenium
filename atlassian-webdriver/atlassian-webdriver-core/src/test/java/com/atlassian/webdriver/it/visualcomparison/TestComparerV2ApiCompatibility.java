package com.atlassian.webdriver.it.visualcomparison;

import com.atlassian.selenium.visualcomparison.VisualComparer;
import com.atlassian.selenium.visualcomparison.utils.BoundingBox;
import com.atlassian.selenium.visualcomparison.utils.ScreenResolution;
import com.atlassian.selenium.visualcomparison.v2.Comparer;
import com.atlassian.selenium.visualcomparison.v2.DefaultComparer;
import com.atlassian.selenium.visualcomparison.v2.VisualComparisonFailedException;
import com.atlassian.selenium.visualcomparison.v2.settings.Resolution;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.debug.WebDriverDebug;
import com.atlassian.webdriver.it.AbstractSimpleServerTest;
import com.atlassian.webdriver.it.pageobjects.page.VisualComparisonPage;
import com.atlassian.webdriver.rule.test.TemporaryFolderPreservingOnFailure;
import com.atlassian.webdriver.testing.annotation.WindowSize;
import com.atlassian.webdriver.visualcomparison.VisualComparisonSupport;
import com.atlassian.webdriver.visualcomparison.WebDriverBrowserEngine;
import com.atlassian.webdriver.visualcomparison.WebDriverVisualComparableClient;
import junit.framework.AssertionFailedError;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.selenium.visualcomparison.v2.ComparisonSettings.emptySettings;
import static com.atlassian.selenium.visualcomparison.v2.settings.Resolution.R1024_768;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.*;

@WindowSize(width = 1024, height = 768)
public class TestComparerV2ApiCompatibility extends AbstractSimpleServerTest
{
    private static final String RESOLUTION_STRING = R1024_768.toString();

    private static final String TEST_1_ID = "test-1";
    private static final String BASELINE_1_NAME = TEST_1_ID + "." + RESOLUTION_STRING + ".png";
    private static final String REPORT_1_FILE_NAME = "report-" + TEST_1_ID + "-" + RESOLUTION_STRING + ".html";

    private static final String TEST_2_ID = "test-2";
    private static final String BASELINE_2_NAME = TEST_2_ID + "." + RESOLUTION_STRING + ".png";
    private static final String REPORT_2_FILE_NAME = "report-" + TEST_2_ID + "-" + RESOLUTION_STRING + ".html";

    private static final String[] BASELINES = { BASELINE_2_NAME };

    @Inject
    private AtlassianWebDriver webDriver;

    @Inject
    private VisualComparisonSupport comparisonSupport;

    @Inject
    private WebDriverDebug webDriverDebug;

    @Rule
    public final TemporaryFolderPreservingOnFailure testFolder =
            new TemporaryFolderPreservingOnFailure(new File("target/visualcomparisontests"));

    private File baselineDir;
    private File v1reportDir;
    private File v2reportDir;

    @Before
    public void setUpDirs() throws IOException
    {
        baselineDir = testFolder.getFolder().newFolder("baseline");
        v1reportDir = testFolder.getFolder().newFolder("reports-v1");
        v2reportDir = testFolder.getFolder().newFolder("reports-v2");
        copyBaselines();
    }

    private void copyBaselines() throws IOException
    {
        for (String baseline : BASELINES)
        {
            copyBaseline(baseline);
        }
    }

    private void copyBaseline(String baselineName) throws IOException
    {
        InputStream baseline = null;
        OutputStream baselineFile = null;
        try
        {
            baseline = getClass().getResourceAsStream("/visualcomparison/" + baselineName);
            baselineFile = new FileOutputStream(new File(baselineDir, baselineName));
            IOUtils.copy(baseline, baselineFile);
        }
        finally
        {
            IOUtils.closeQuietly(baseline);
            IOUtils.closeQuietly(baselineFile);
        }
    }

    @Test
    public void testCompatibilitySuccessfulCompare() throws IOException
    {

        product.visit(VisualComparisonPage.class, 1);
        VisualComparer legacyComparer = getLegacyComparer();
        addIgnoredElementOldStyle(legacyComparer, By.className("aui-header-logo-device"));
        webDriverDebug.takeScreenshotTo(new File(baselineDir, BASELINE_1_NAME));

        legacyComparer.assertUIMatches(TEST_1_ID, baselineDir.getAbsolutePath());

        Comparer newComparer = new DefaultComparer(new WebDriverBrowserEngine(webDriver), emptySettings()
                .withResolution(Resolution.R1024_768)
                .withBaselineDirectory(baselineDir)
                .withReportingEnabled(v2reportDir)
                .ignoringSingleLineDifferences(true));

        newComparer.compare("test-1", emptySettings()
                .ignoringPart(comparisonSupport.asPagePart(By.className("aui-header-logo-device"))));

        assertFalse("Report HTML for V1 should not exist", new File(v1reportDir, REPORT_1_FILE_NAME).exists());
        assertFalse("Report HTML for V2 should not exist", new File(v2reportDir, REPORT_1_FILE_NAME).exists());
    }

    @Test
    public void testCompatibilityFailedCompare() throws IOException
    {
        VisualComparer legacyComparer = getLegacyComparer();
        product.visit(VisualComparisonPage.class, 2);

        try
        {
            legacyComparer.assertUIMatches(TEST_2_ID, baselineDir.getAbsolutePath());
            fail("Legacy comparer should throw exception on comparison failure");
        }
        catch (AssertionFailedError expected)
        {
        }

        Comparer newComparer = new DefaultComparer(new WebDriverBrowserEngine(webDriver), emptySettings()
                .withResolution(Resolution.R1024_768)
                .withBaselineDirectory(baselineDir)
                .withReportingEnabled(v2reportDir)
                .ignoringSingleLineDifferences(true));

        try
        {
            newComparer.compare(TEST_2_ID);
            fail("New comparer should throw exception on comparison failure");
        }
        catch (VisualComparisonFailedException expected)
        {
        }

        assertEquals(readFileToString(new File(v1reportDir, REPORT_2_FILE_NAME)),
                readFileToString(new File(v2reportDir, REPORT_2_FILE_NAME)));
    }

    @Test(expected = VisualComparisonFailedException.class)
    public void testBaselineMissing()
    {
        Comparer comparer = new DefaultComparer(new WebDriverBrowserEngine(webDriver), emptySettings()
                .withResolution(Resolution.R1024_768)
                .withBaselineDirectory(baselineDir)
                .withReportingEnabled(v2reportDir)
                .ignoringSingleLineDifferences(true));

        product.visit(VisualComparisonPage.class, 1);
        comparer.compare("test-3"); // baseline doesn't exist, should error out
    }

    private VisualComparer getLegacyComparer()
    {
        VisualComparer oldComparer = new VisualComparer(new WebDriverVisualComparableClient(webDriver));
        oldComparer.setScreenResolutions(new ScreenResolution[] {
                new ScreenResolution(1024, 768)
        });
        oldComparer.setWaitforJQueryTimeout(2000);
        oldComparer.setIgnoreSingleLineDiffs(true);
        oldComparer.enableReportGeneration(v1reportDir.getAbsolutePath());
        return oldComparer;
    }

    private void addIgnoredElementOldStyle(VisualComparer oldComparer, By selector)
    {
        WebElement elementBySelector = webDriver.findElement(selector);
        Dimension size = elementBySelector.getSize();
        Point location = elementBySelector.getLocation();
        BoundingBox elementArea = new BoundingBox(location.x, location.y, location.x + size.getWidth(),
                location.y + size.getHeight());

        List<BoundingBox> ignoreAreas = oldComparer.getIgnoreAreas();
        ignoreAreas = (null == ignoreAreas) ? new ArrayList<BoundingBox>() : new ArrayList<BoundingBox>(ignoreAreas);
        ignoreAreas.add(elementArea);
        oldComparer.setIgnoreAreas(ignoreAreas);
    }
}
