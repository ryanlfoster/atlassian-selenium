package com.atlassian.selenium.visualcomparison.utils;

import com.atlassian.annotations.Internal;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Internal
public class ScreenshotDiffer
{
    public ScreenshotDiff diff(Screenshot oldScreen, Screenshot newScreen,
                               List<BoundingBox> ignoreAreas, boolean ignoreSingleLines) throws IOException
    {
        final BufferedImage thisImage = oldScreen.getImage();
        final BufferedImage otherImage = newScreen.getImage();
        final int thisWidth = thisImage.getWidth();
        final int thisHeight = thisImage.getHeight();
        final int otherWidth = otherImage.getWidth();
        final int otherHeight = otherImage.getHeight();
        final int maxWidth = Math.max(thisWidth, otherWidth);
        final int maxHeight = Math.max(thisHeight, otherHeight);

        // Iterate through the pixels in both images and create a diff image.
        BufferedImage diffImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
        ArrayList<BoundingBox> boxes = new ArrayList<BoundingBox>();
        for (int x = 0; x < maxWidth; x++)
        {
            for (int y = 0; y < maxHeight; y++)
            {
                // The images aren't necessarily the same size, only compare the pixels if they're
                // present in both.
                if ((x < thisWidth) && (x < otherWidth) && (y < thisHeight) && (y < otherHeight))
                {
                    int thisPixel = thisImage.getRGB(x, y);
                    int otherPixel = otherImage.getRGB(x, y);
                    if (shouldIgnorePixel (x, y, ignoreAreas) || thisPixel == otherPixel)
                    {
                        // The pixels are the same or we don't care if they're different, draw it as-is in the diff.
                        diffImage.setRGB(x, y, thisPixel);
                    }
                    else
                    {
                        // The pixels are different, set the pixel to red in the diff.
                        diffImage.setRGB(x, y, 0xFF0000);

                        // If this pixel is near any bounding boxes, expand them to include it.
                        boolean foundBox = false;
                        for (BoundingBox box : boxes)
                        {
                            if (box.isNear(x, y))
                            {
                                box.merge(x, y);
                                foundBox = true;
                            }
                        }
                        // Otherwise, start a new bounding box.
                        if (!foundBox)
                        {
                            boxes.add(new BoundingBox(x, y));
                        }
                    }
                }
                else
                {
                    // The two images are different sizes and this pixel is out of bounds in one of them.
                    // Set the pixel to black in the diff.
                    diffImage.setRGB(x, y, 0x000000);
                }
            }
        }
        if (ignoreSingleLines)
        {
            BoundingBox.deleteSingleLineBoxes(boxes);
        }

        BoundingBox.mergeOverlappingBoxes(boxes);

        thisImage.flush();
        otherImage.flush();

        return new ScreenshotDiff(oldScreen, newScreen, oldScreen.getId(), oldScreen.getResolution(), diffImage, boxes, ignoreAreas);
    }

    private boolean shouldIgnorePixel (int x, int y, List<BoundingBox> ignoreAreas)
    {
        if (ignoreAreas == null)
        {
            return false;
        }
        for (BoundingBox ignoreArea : ignoreAreas)
        {
            if (ignoreArea.contains (x,y))
            {
                return true;
            }
        }
        return false;
    }
}
