package com.atlassian.selenium.visualcomparison.utils;

import com.atlassian.annotations.Internal;
import com.atlassian.selenium.visualcomparison.VisualComparableClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Internal
public class Screenshot implements Comparable<Screenshot>
{
    private ScreenResolution resolution;
    private String id;
    private File file;

    public Screenshot(VisualComparableClient client, String id, String imageDir, ScreenResolution resolution) throws IOException
    {
        final String filePath = imageDir + "/" + id + "." + resolution + ".png";
        client.captureEntirePageScreenshot(filePath);
        init(filePath, id, resolution);
    }

    public Screenshot(File file) throws IOException
    {
        String[] fileNameParts = file.getName().split("\\.");
        if (fileNameParts.length != 3)
        {
            throw new IOException("Invalid screenshot name - " + file.getName());
        }
        init(file.getAbsolutePath(), fileNameParts[0], new ScreenResolution(fileNameParts[1]));
    }

    public static String generateFileName(String id, ScreenResolution resolution)
    {
        return id.replace('.', '-') + "." + resolution + ".png";
    }

    private void init(String filePath, String id, ScreenResolution resolution) throws IOException
    {
        this.id = id;
        this.resolution = resolution;
        this.file = new File(filePath);
    }

    public String getFileName()
    {
        return file.getName();
    }

    public String getId()
    {
        return id;
    }

    public ScreenResolution getResolution()
    {
        return resolution;
    }

    public BufferedImage getImage() throws IOException
    {
        return ImageIO.read(file);
    }

    public int compareTo(Screenshot other)
    {
        int result = this.id.compareTo(other.id);
        if (result != 0) {
            return result;
        }
        return this.resolution.compareTo(other.resolution);
    }

    public ScreenshotDiff getDiff(Screenshot other) throws IOException
    {
        return getDiff (other, null, false);
    }

    public ScreenshotDiff getDiff(Screenshot other, List<BoundingBox> ignoreAreas, boolean ignoreSingleLines) throws IOException
    {
        return new ScreenshotDiffer().diff(this, other, ignoreAreas, ignoreSingleLines);
    }
}
