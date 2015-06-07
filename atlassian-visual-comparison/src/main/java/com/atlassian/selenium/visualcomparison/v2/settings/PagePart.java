package com.atlassian.selenium.visualcomparison.v2.settings;

import com.atlassian.annotations.ExperimentalApi;

/**
 * Represents a part of the page identified by a top-left and bottom-right corner coordinates.
 *
 * @since 2.3
 */
@ExperimentalApi
public interface PagePart
{
    public int getLeft();

    public int getTop();

    public int getRight();

    public int getBottom();
}
