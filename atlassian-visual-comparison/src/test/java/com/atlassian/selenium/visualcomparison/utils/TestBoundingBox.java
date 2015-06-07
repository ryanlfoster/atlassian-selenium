package com.atlassian.selenium.visualcomparison.utils;

import java.util.ArrayList;
import org.junit.Test;
import junit.framework.Assert;
import com.atlassian.selenium.visualcomparison.utils.BoundingBox;

public class TestBoundingBox 
{
	@Test
	public void testMarginsInsideImage ()
	{
		BoundingBox box = new BoundingBox (100, 200, 300, 400);
		Assert.assertEquals("Box's left margin should be correct", 75, box.getMarginLeft());
		Assert.assertEquals("Box's top margin should be correct", 175, box.getMarginTop());
		Assert.assertEquals("Box's right margin should be correct", 325, box.getMarginRight(500));
		Assert.assertEquals("Box's bottom margin should be correct", 425, box.getMarginBottom(500));
	}
	
	@Test
	public void testMarginsOutsideImage ()
	{
		BoundingBox box = new BoundingBox (10, 15, 100, 200);
		Assert.assertEquals("Box's left margin should be correct", 0, box.getMarginLeft());
		Assert.assertEquals("Box's top margin should be correct", 0, box.getMarginTop());
		Assert.assertEquals("Box's right margin should be correct", 110, box.getMarginRight(110));
		Assert.assertEquals("Box's bottom margin should be correct", 210, box.getMarginBottom(210));
	}
	
	@Test
	public void testBoxMerging ()
	{
		ArrayList<BoundingBox> boxes = new ArrayList<BoundingBox> ();
		boxes.add (new BoundingBox (10, 10, 20, 20));
		boxes.add (new BoundingBox (45, 45, 50, 50));
		
		BoundingBox.mergeOverlappingBoxes(boxes);
		Assert.assertEquals("Boxes should be merged into one", 1, boxes.size());
		BoundingBox box = boxes.get(0);
		Assert.assertEquals("Bounding box 1 left should be correct", 10, box.getLeft());
		Assert.assertEquals("Bounding box 1 top should be correct", 10, box.getTop());
		Assert.assertEquals("Bounding box 1 right should be correct", 50, box.getRight());
		Assert.assertEquals("Bounding box 1 bottom should be correct", 50, box.getBottom());
	}
	
	@Test
	public void testBoxesNotMerged ()
	{
		ArrayList<BoundingBox> boxes = new ArrayList<BoundingBox> ();
		boxes.add (new BoundingBox (10, 10, 20, 20));
		boxes.add (new BoundingBox (46, 46, 50, 50));
		
		BoundingBox.mergeOverlappingBoxes(boxes);
		Assert.assertEquals("Boxes should not be merged", 2, boxes.size());
		BoundingBox box1 = boxes.get(0);
		Assert.assertEquals("Bounding box 1 left should be correct", 10, box1.getLeft());
		Assert.assertEquals("Bounding box 1 top should be correct", 10, box1.getTop());
		Assert.assertEquals("Bounding box 1 right should be correct", 20, box1.getRight());
		Assert.assertEquals("Bounding box 1 bottom should be correct", 20, box1.getBottom());
		BoundingBox box2 = boxes.get(1);
		Assert.assertEquals("Bounding box 2 left should be correct", 46, box2.getLeft());
		Assert.assertEquals("Bounding box 2 top should be correct", 46, box2.getTop());
		Assert.assertEquals("Bounding box 2 right should be correct", 50, box2.getRight());
		Assert.assertEquals("Bounding box 2 bottom should be correct", 50, box2.getBottom());		
	}
	
	@Test
	public void testSingleLineBoxDeletion ()
	{
		ArrayList<BoundingBox> boxes = new ArrayList<BoundingBox> ();
		boxes.add (new BoundingBox (10, 10, 20, 20));
		boxes.add (new BoundingBox (30, 30, 30, 50));
		boxes.add (new BoundingBox (30, 30, 50, 30));
		
		BoundingBox.deleteSingleLineBoxes(boxes);
		
		Assert.assertEquals("One box should be left after removing single-line boxes", 1, boxes.size());
		BoundingBox box = boxes.get(0);
		Assert.assertEquals("Bounding box left should be correct", 10, box.getLeft());
		Assert.assertEquals("Bounding box top should be correct", 10, box.getTop());
		Assert.assertEquals("Bounding box right should be correct", 20, box.getRight());
		Assert.assertEquals("Bounding box bottom should be correct", 20, box.getBottom());		
	}
}
