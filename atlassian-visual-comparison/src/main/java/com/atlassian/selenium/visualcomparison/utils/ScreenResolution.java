package com.atlassian.selenium.visualcomparison.utils;

import com.atlassian.annotations.Internal;

import java.awt.*;

@Internal
public class ScreenResolution extends Dimension implements Comparable<ScreenResolution>
{
    public ScreenResolution(int width, int height)
    {
        super(width, height);
    }

    public ScreenResolution(String value)
    {
        String[] parts = value.split("x");
        if (parts.length != 2)
        {
            throw new RuntimeException(value + " is not a valid screen resolution");
        }
        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);

        setSize(width, height);
    }

    public int compareTo(ScreenResolution other)
    {
        if (this.width < other.width)
        {
            return -1;
        }
        if (this.width > other.width)
        {
            return 1;
        }
        if (this.height < other.height)
        {
            return -1;
        }
        if (this.height > other.height)
        {
            return 1;
        }
        return 0;
    }

    public String toString()
    {
        return width + "x" + height;
    }
}
