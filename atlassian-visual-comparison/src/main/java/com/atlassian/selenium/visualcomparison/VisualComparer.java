package com.atlassian.selenium.visualcomparison;

import com.atlassian.annotations.Internal;
import com.atlassian.selenium.visualcomparison.utils.BoundingBox;
import com.atlassian.selenium.visualcomparison.utils.PageDifference;
import com.atlassian.selenium.visualcomparison.utils.PageElementInfo;
import com.atlassian.selenium.visualcomparison.utils.ScreenResolution;
import com.atlassian.selenium.visualcomparison.utils.Screenshot;
import com.atlassian.selenium.visualcomparison.utils.ScreenshotDiff;
import junit.framework.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Implements the visual comparison functionality.
 *
 * <p/>
 * NOTE: this class is considered internal as of 2.3 and may be subject to API changes and removal as of 3.0. Please
 * use {@link com.atlassian.selenium.visualcomparison.v2.Comparer} and the associated family of classes making up the
 * Visual Comparison V2 API instead of this class directly.
 */
@Internal
public class VisualComparer
{
    private ScreenResolution[] resolutions = new ScreenResolution[]
            {
                    new ScreenResolution(1280, 1024)
            };
    private VisualComparableClient client;
    private boolean refreshAfterResize = false;
    private boolean reportingEnabled = false;
    private String reportOutputPath;
    private String imageSubDirName = "report_images";
    private String tempPath = System.getProperty("java.io.tmpdir");
    private Map<String, String> uiStringReplacements = null;
    private long waitforJQueryTimeout = 0;
    private List<BoundingBox> ignoreAreas = null;
    private boolean ignoreSingleLineDiffs = false;

    public long getWaitforJQueryTimeout() {
        return waitforJQueryTimeout;
    }

    public void setWaitforJQueryTimeout(long waitforJQueryTimeout) {
        this.waitforJQueryTimeout = waitforJQueryTimeout;
    }

    public VisualComparer(VisualComparableClient client)
    {
        this.client = client;
    }

    public void setScreenResolutions(ScreenResolution[] resolutions)
    {
        this.resolutions = resolutions;
    }

    public ScreenResolution[] getScreenResolutions()
    {
        return this.resolutions;
    }

    public void setRefreshAfterResize(boolean refreshAfterResize)
    {
        this.refreshAfterResize = refreshAfterResize;
    }

    public boolean getRefreshAfterResize()
    {
        return this.refreshAfterResize;
    }

    public void setUIStringReplacements(Map<String,String> uiStringReplacements)
    {
        this.uiStringReplacements = uiStringReplacements;
    }

    public Map<String,String> getUIStringReplacements()
    {
        return this.uiStringReplacements;
    }

    public void enableReportGeneration(String reportOutputPath)
    {
        this.reportingEnabled = true;
        this.reportOutputPath = reportOutputPath;
        
        File file = new File(reportOutputPath + "/" + imageSubDirName);
        file.mkdirs();
    }

    public void disableReportGeneration()
    {
        this.reportingEnabled = false;
    }

    public void setTempPath(String tempPath)
    {
        File file = new File(tempPath);
        file.mkdirs();
        this.tempPath = tempPath;
    }

    public String getTempPath()
    {
        return this.tempPath;
    }


    public List<BoundingBox> getIgnoreAreas()
    {
        return ignoreAreas;
    }

    public boolean getIgnoreSingleLineDiffs()
    {
        return ignoreSingleLineDiffs;
    }

    public void setIgnoreSingleLineDiffs(boolean ignoreSingleLineDiffs)
    {
        this.ignoreSingleLineDiffs = ignoreSingleLineDiffs;
    }

    public void setIgnoreAreas(List<BoundingBox> ignoreAreas)
    {
        this.ignoreAreas = ignoreAreas;
    }

    public void assertUIMatches(String id, String baselineImagePath)
    {
        try
        {
            Assert.assertTrue("Screenshots were not equal", uiMatches(id, baselineImagePath));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean uiMatches(final String id, final String baselineImagePath) throws Exception
    {
        ArrayList<Screenshot> currentScreenshots = takeScreenshots(id);
        ArrayList<Screenshot> baselineScreenshots = loadBaselineScreenshots(id, baselineImagePath);
        return compareScreenshots(baselineScreenshots, currentScreenshots);
    }

    public ArrayList<Screenshot> takeScreenshots(final String id) throws IOException
    {
        // Capture a series of screenshots in all the valid screen resolutions.
        ArrayList<Screenshot> screenshots = new ArrayList<Screenshot>();
        for (ScreenResolution res : resolutions)
        {
            client.resizeScreen(res, refreshAfterResize);
            if (waitforJQueryTimeout > 0)
            {
                if (!client.waitForJQuery (waitforJQueryTimeout))
                {
                    Assert.fail("Timed out while waiting for jQuery to complete");
                }
            }
            if (uiStringReplacements != null)
            {
                // Remove strings from the UI that we are expecting will change
                // (such as the build number in the JIRA footer)
                for (String key : uiStringReplacements.keySet())
                {
                    replaceUIHtml(key, uiStringReplacements.get(key));
                }
            }
            screenshots.add(new Screenshot(client, id, tempPath, res));
        }
        Collections.sort(screenshots);
        return screenshots;
    }

    public ArrayList<Screenshot> loadBaselineScreenshots(final String id, final String baselineImagePath) throws IOException
    {
        File screenshotDir = new File(baselineImagePath);
        File[] screenshotFiles = screenshotDir.listFiles(
                new FileFilter()
                {
                    public boolean accept(File file) { return file.getName().startsWith(id);}
                });

        ArrayList<Screenshot> screenshots = new ArrayList<Screenshot>();
        for (File screenshotFile : screenshotFiles)
        {
            screenshots.add(new Screenshot(screenshotFile));
        }
        Collections.sort(screenshots);
        return screenshots;
    }

    protected void replaceUIHtml(String id, String newContent)
    {
        final String script = "var content, el = window.document.getElementById('" + id + "');" +
                "if (el) { content = el.innerHTML; el.innerHTML = \"" + newContent + "\"; } content;";
        final Object result = client.execute(script);
        final String value = String.valueOf(result);
    }

    public boolean compareScreenshots(ArrayList<Screenshot> oldScreenshots, ArrayList<Screenshot> newScreenshots)
            throws Exception
    {
        if (oldScreenshots.size() != newScreenshots.size())
        {
            if (oldScreenshots.size() == 0)
            {
                if (reportingEnabled)
                {
                    String imageOutputDir = ScreenshotDiff.getImageOutputDir(reportOutputPath, imageSubDirName);
                    for (Screenshot newScreenshot : newScreenshots)
                    {
                        // Copy the new image to the output directory.
                        ImageIO.write(newScreenshot.getImage(), "png", new File(imageOutputDir + newScreenshot.getFileName()));
                    }
                }
                throw new IllegalArgumentException("There were new screenshots, but no baseline images. Is this a new test?"
                    + " If reporting is enabled, the new screenshots will be output in the report.");
            }
            else
            {
                throw new IllegalArgumentException("Incorrect number of images."
                        + " There were " + oldScreenshots.size() + " baseline images,"
                        + " but only " + newScreenshots.size() + " new images.");
            }
        }

        boolean matches = true;
        for (int i = 0; i < oldScreenshots.size(); i++)
        {
            ScreenshotDiff diff = getScreenshotDiff(oldScreenshots.get(i), newScreenshots.get(i));

            for (PageDifference difference : diff.getDifferences())
            {
                BoundingBox box = difference.getBoundingBox();
                int x = new Double(Math.floor(box.getLeft() + box.getWidth() / 2)).intValue();
                int y = new Double(Math.floor(box.getTop() + box.getHeight() / 2)).intValue();
                ScreenElement thing = client.getElementAtPoint(x, y);
                PageElementInfo info = new PageElementInfo();
                info.htmlContent = thing.getHtml();
                info.position = new Point(x,y);
                difference.addPageElement(info);
            }

            if (reportingEnabled)
            {
                diff.writeDiffReport(reportOutputPath, imageSubDirName);
            }
            matches = !diff.hasDifferences() && matches;
        }

        return matches;
    }

    public ScreenshotDiff getScreenshotDiff(Screenshot oldScreenshot, Screenshot newScreenshot) throws Exception
    {
        return oldScreenshot.getDiff(newScreenshot, ignoreAreas, ignoreSingleLineDiffs);
    }


}
