package com.atlassian.selenium.visualcomparison.v2.settings;

import com.atlassian.annotations.ExperimentalApi;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Simple implementation of {@link PagePart} that stores all the coordinates internally.
 *
 * @since 2.3
 */
@Immutable
@ExperimentalApi
public final class SimplePagePart implements PagePart
{
    private final int left;
    private final int top;
    private final int right;
    private final int bottom;

    public SimplePagePart(int left, int top, int right, int bottom)
    {
        checkArgument(left >= 0, "left must be >= 0");
        checkArgument(top >= 0, "top must be >= 0");
        checkArgument(right >= 0, "right must be >= 0");
        checkArgument(bottom >= 0, "bottom must be >= 0");
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
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
}
