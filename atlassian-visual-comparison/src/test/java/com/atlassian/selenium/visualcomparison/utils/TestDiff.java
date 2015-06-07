package com.atlassian.selenium.visualcomparison.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import com.atlassian.selenium.visualcomparison.utils.BoundingBox;
import com.atlassian.selenium.visualcomparison.utils.Screenshot;
import com.atlassian.selenium.visualcomparison.utils.ScreenshotDiff;

public class TestDiff 
{
	private Screenshot screenshot1;
	private Screenshot screenshot2;

	public TestDiff () throws IOException
	{
		screenshot1 = new Screenshot (new File("src/test/resources/EAC-1.1024x768.png"));
		screenshot2 = new Screenshot (new File("src/test/resources/EAC-2.1024x768.png"));
	}

    @Test
    public void testDiffIdentical() throws IOException
    {	
    	// The same screenshot compared to itself should have no differences.
    	ScreenshotDiff diff = screenshot1.getDiff(screenshot1);
    	Assert.assertFalse("Identical images should have no differences", diff.hasDifferences());
    	Assert.assertEquals("Should be no areas of difference", 0, diff.getDiffAreas().size());
    }
	
    @Test
    public void testDiffSameSizeDifferentContent() throws IOException
    {	
    	// Two screenshots of the same size but with different content should have differences.
    	ScreenshotDiff diff = screenshot1.getDiff(screenshot2);
    	Assert.assertTrue("Different images should have differences", diff.hasDifferences());
    	List<BoundingBox> boxes = diff.getDiffAreas();
    	Assert.assertEquals("Should be only one area of difference", 1, boxes.size());
    	BoundingBox box = boxes.get(0);
    	Assert.assertEquals("Bounding box left should be correct", 459, box.getLeft());
    	Assert.assertEquals("Bounding box top should be correct", 163, box.getTop());
    	Assert.assertEquals("Bounding box right should be correct", 1069, box.getRight());
    	Assert.assertEquals("Bounding box bottom should be correct", 633, box.getBottom());
    }
    
    @Test
    public void testDiffIgnoreEntireDifferentArea() throws IOException
    {
    	// If all differences are ignored, they should be the same.
    	// Two screenshots of the same size but with different content should have differences.
    	ScreenshotDiff diff = screenshot1.getDiff(screenshot2, Arrays.asList(new BoundingBox [] { new BoundingBox (459, 163, 1069, 633) }), false);
    	Assert.assertFalse("All differences should be ignored", diff.hasDifferences());
    	Assert.assertEquals("Should be no areas of difference", 0, diff.getDiffAreas().size());
    }

    @Test
    public void testDiffIgnorePartialDifferentAreaVertical() throws Exception
    {	
    	// One large failure area with an ignored vertical stripe across the middle
    	ScreenshotDiff diff = screenshot1.getDiff(screenshot2, Arrays.asList(new BoundingBox [] { new BoundingBox (500, 163, 550, 633) }), false);
    	Assert.assertTrue("Different images should have differences", diff.hasDifferences());
    	List<BoundingBox> boxes = diff.getDiffAreas();
    	Assert.assertEquals("Should be four areas of difference with an ignored area in between", 4, boxes.size());

    	BoundingBox box1 = boxes.get(0);
    	Assert.assertEquals("Bounding box 1 left should be correct", 459, box1.getLeft());
    	Assert.assertEquals("Bounding box 1 top should be correct", 163, box1.getTop());
    	Assert.assertEquals("Bounding box 1 right should be correct", 499, box1.getRight());
    	Assert.assertEquals("Bounding box 1 bottom should be correct", 292, box1.getBottom());
    
    	BoundingBox box2 = boxes.get(1);
    	Assert.assertEquals("Bounding box 2 left should be correct", 459, box2.getLeft());
    	Assert.assertEquals("Bounding box 2 top should be correct", 339, box2.getTop());
    	Assert.assertEquals("Bounding box 2 right should be correct", 499, box2.getRight());
    	Assert.assertEquals("Bounding box 2 bottom should be correct", 415, box2.getBottom());
    	
    	BoundingBox box3 = boxes.get(2);
    	Assert.assertEquals("Bounding box 3 left should be correct", 459, box3.getLeft());
    	Assert.assertEquals("Bounding box 3 top should be correct", 441, box3.getTop());
    	Assert.assertEquals("Bounding box 3 right should be correct", 499, box3.getRight());
    	Assert.assertEquals("Bounding box 3 bottom should be correct", 633, box3.getBottom());
    	
    	BoundingBox box4 = boxes.get(3);
    	Assert.assertEquals("Bounding box 4 left should be correct", 551, box4.getLeft());
    	Assert.assertEquals("Bounding box 4 top should be correct", 163, box4.getTop());
    	Assert.assertEquals("Bounding box 4 right should be correct", 1069, box4.getRight());
    	Assert.assertEquals("Bounding box 4 bottom should be correct", 633, box4.getBottom());
    }
    
    @Test
    public void testDiffIgnorePartialDifferentAreaHorizontal() throws IOException
    {	
    	// One large failure area with an ignored horizontal stripe across the middle
    	ScreenshotDiff diff = screenshot1.getDiff(screenshot2, Arrays.asList(new BoundingBox [] { new BoundingBox (459, 300, 1069, 400) }), false);
    	Assert.assertTrue("Different images should have differences", diff.hasDifferences());
    	List<BoundingBox> boxes = diff.getDiffAreas();
    	Assert.assertEquals("Should be two areas of difference with an ignored area in between", 2, boxes.size());
    	
    	BoundingBox box1 = boxes.get(0);
    	Assert.assertEquals("Bounding box 1 left should be correct", 459, box1.getLeft());
    	Assert.assertEquals("Bounding box 1 top should be correct", 163, box1.getTop());
    	Assert.assertEquals("Bounding box 1 right should be correct", 1069, box1.getRight());
    	Assert.assertEquals("Bounding box 1 bottom should be correct", 299, box1.getBottom());
    
    	BoundingBox box2 = boxes.get(1);
    	Assert.assertEquals("Bounding box 2 left should be correct", 459, box2.getLeft());
    	Assert.assertEquals("Bounding box 2 top should be correct", 401, box2.getTop());
    	Assert.assertEquals("Bounding box 2 right should be correct", 1069, box2.getRight());
    	Assert.assertEquals("Bounding box 2 bottom should be correct", 633, box2.getBottom());
    }
    
}
