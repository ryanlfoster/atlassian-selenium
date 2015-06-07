package com.atlassian.selenium.visualcomparison.utils;

import com.atlassian.annotations.Internal;

import java.io.File;

@Internal
public class PageDifferenceImages
{
    private File oldImageFile;
    private File newImageFile;
    private File diffImageFile;
    private String outputDir;

    public PageDifferenceImages(File oldImageFile, File newImageFile, File diffImageFile, String outputDir)
    {
        this.oldImageFile = oldImageFile;
        this.newImageFile = newImageFile;
        this.diffImageFile = diffImageFile;
        this.outputDir = outputDir;
    }

    public String getOldImageFile()
    {
        return ScreenshotDiff.relativePath(oldImageFile, outputDir);
    }

    public String getNewImageFile()
    {
        return ScreenshotDiff.relativePath(newImageFile, outputDir);
    }

    public String getDiffImageFile()
    {
        return ScreenshotDiff.relativePath(diffImageFile, outputDir);
    }
}
