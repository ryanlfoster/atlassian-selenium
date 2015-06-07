package com.atlassian.selenium.visualcomparison.utils;

import com.atlassian.annotations.Internal;

import java.util.ArrayList;
import java.util.List;

@Internal
public class BoundingBox
{
    private int left;
    private int top;
    private int right;
    private int bottom;

    private int leftMargin;
    private int topMargin;
    private int rightMargin;
    private int bottomMargin;
    private static final int MARGIN = 25;

    public BoundingBox(int x, int y)
    {
        this(x, y, x, y);
    }

    public BoundingBox(int left, int top, int right, int bottom)
    {
        setLeft(left);
        setTop(top);
        setRight(right);
        setBottom(bottom);
    }

    // Although the margin values are derived values, let's cache them because we'll be using them
    // far more often than we set them.
    private void setLeft(int left)
    {
        this.left = left;
        this.leftMargin = left - MARGIN;
    }

    private void setRight(int right)
    {
        this.right = right;
        this.rightMargin = right + MARGIN;
    }

    private void setTop(int top)
    {
        this.top = top;
        this.topMargin = top - MARGIN;
    }

    private void setBottom(int bottom)
    {
        this.bottom = bottom;
        this.bottomMargin = bottom + MARGIN;
    }

    public int getLeft()
    {
        return left;
    }

    public int getTop()
    {
        return top;
    }

    public int getRight()
    {
        return right;
    }

    public int getBottom()
    {
        return bottom;
    }

    public int getWidth()
    {
        return right - left + 1;
    }

    public int getHeight()
    {
        return bottom - top + 1;
    }

    // Accessor functions for the box's margins that restrict them to fit within the image.
    public int getMarginLeft()
    {
        return Math.max(left - MARGIN, 0);
    }

    public int getMarginTop()
    {
        return Math.max(top - MARGIN, 0);
    }

    public int getMarginRight(int maxX)
    {
        return Math.min(right + MARGIN, maxX);
    }

    public int getMarginBottom(int maxY)
    {
        return Math.min(bottom + MARGIN, maxY);
    }

    public int getMarginWidth(int maxX)
    {
        return getMarginRight(maxX) - getMarginLeft() + 1;
    }

    public int getMarginHeight(int maxY)
    {
        return getMarginBottom(maxY) - getMarginTop() + 1;
    }

    public boolean contains (int x, int y)
    {
        // Return true if the given co-ords are within this box.
        return ((x >= left) && (x <= right) &&
                (y >= top) && (y <= bottom));
    }

    public boolean isNear(int x, int y)
    {
        // Return true if the given co-ords are within this box, or within the given margin of it.
        return ((x >= leftMargin) && (x <= rightMargin) &&
                (y >= topMargin) && (y <= bottomMargin));
    }

    public boolean isNear(BoundingBox box)
    {
        // If any of this box's corners are near the other box, or any of the other box's corners are
        // near this box, then the boxes overlap or are very close.
        return (box.isNear(left, top) || box.isNear(right, top) ||
                box.isNear(left, bottom) || box.isNear(right, bottom) ||
                isNear(box.left, box.top) || isNear(box.right, box.top) ||
                isNear(box.left, box.bottom) || isNear(box.right, box.bottom));
    }

    public void merge(int x, int y)
    {
        // Expand this box to contain the given co-ords.
        if (x < left)
        {
            setLeft(x);
        }
        if (x > right)
        {
            setRight(x);
        }
        if (y < top)
        {
            setTop(y);
        }
        if (y > bottom)
        {
            setBottom(y);
        }
    }

    public void merge(BoundingBox box)
    {
        // Expand this box to contain the given box.
        merge(box.left, box.top);
        merge(box.right, box.bottom);
    }

    public static void mergeOverlappingBoxes(ArrayList<BoundingBox> boxes)
    {
        // This is very messy. The basic idea is to merge all boxes that overlap.
        // The current approach (until I think of a better one) is to compare every box
        // in the array with every box after it, and keep iterating over the whole
        // array until there are no longer any overlaps. The outer loop is needed because
        // merging two boxes could bring the combined box into conflict with a box we've
        // already checked.
        boolean mergePerformedThisLoop;
        do
        {
            mergePerformedThisLoop = false;
            for (int iCurrent = 0; iCurrent < boxes.size(); iCurrent++)
            {
                BoundingBox current = boxes.get(iCurrent);
                for (int iOther = iCurrent + 1; iOther < boxes.size();)
                {
                    if (current.isNear(boxes.get(iOther)))
                    {
                        current.merge(boxes.get(iOther));
                        boxes.remove(iOther);
                        mergePerformedThisLoop = true;
                    }
                    else
                    {
                        iOther++;
                    }
                }
            }
        }
        while (mergePerformedThisLoop);
    }

    public static void deleteSingleLineBoxes(List<BoundingBox> boxes)
    {
        // Remove any changes that are only one pixel wide or high
        for (int i = 0; i < boxes.size(); )
        {
            BoundingBox box = boxes.get(i);
            if (box.getWidth() == 1 || box.getHeight() == 1)
            {
                boxes.remove(i);
            }
            else
            {
                i++;
            }
        }
    }
}
