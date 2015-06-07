package com.atlassian.selenium.visualcomparison.v2.settings;

import com.atlassian.annotations.ExperimentalApi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

/**
 * Represents screen resolution (in pixels) that can be used for visual comparisons.
 *
 * <p/>
 * Valid string representations of resolutions are in form {@code <width>x<height>}. {@link #toString()} and
 * {@link #parse(String)} methods can be used to serialize/deserialize valid resolutions to/from strings, and are
 * compatible with each other.
 *
 * @since 2.3
 */
@ExperimentalApi
@Immutable
public final class Resolution implements Comparable<Resolution>
{
    public static final Resolution R1024_768 = new Resolution(1024, 768);

    private static final String SEPARATOR = "x";

    private final int width;
    private final int height;

    public Resolution(int width, int height)
    {
        checkArgument(width > 0, "Width must be >0");
        checkArgument(height > 0, "Height must be >0");
        this.width = width;
        this.height = height;
    }

    /**
     * Parse
     *
     * @param resolutionString
     * @return
     */
    public static Resolution parse(@Nonnull String resolutionString)
    {
        String[] parts = checkNotNull(resolutionString, "resolutionString").split(SEPARATOR);
        try
        {
            checkArgument(parts.length == 2, "More than one 'x' in " + resolutionString);
            return new Resolution(parseInt(parts[0]), parseInt(parts[1]));
        } catch (Exception e)
        {
            throw new IllegalArgumentException(format("'%s' is not a valid screen resolution representation. "
                    + "Valid values are in form <width>x<height>", resolutionString));
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    @Override
    public int compareTo(@Nonnull Resolution that)
    {
        checkNotNull(that, "that");
        int widthCompare = Integer.valueOf(width).compareTo(that.width);
        if (widthCompare != 0)
        {
            return widthCompare;
        } else
        {
            return Integer.valueOf(height).compareTo(that.height);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resolution that = (Resolution) o;
        return width == that.width && height == that.height;
    }

    @Override
    public int hashCode()
    {
        int result = width;
        result = 31 * result + height;
        return result;
    }

    @Nonnull
    @Override
    public String toString()
    {
        return width + SEPARATOR + height;
    }
}
