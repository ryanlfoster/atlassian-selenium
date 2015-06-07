package com.atlassian.selenium.visualcomparison.v2;

import com.atlassian.annotations.ExperimentalApi;

import javax.annotation.Nonnull;

/**
 * The main tool for visual comparison. Instances of this interface can be used for performing visual comparison of
 * a single page. They should be {@link ComparisonSettings configurable} at the instance level, as well as per
 * {@link #compare(String, ComparisonSettings) single comparison}. The settings passed on for comparison should merge
 * into, or override the comparer settings where appropriate.
 *
 * @since 2.3
 */
@ExperimentalApi
public interface Comparer
{

    /**
     * Take a snapshot of the current page and compare it with a baseline using given {@code id}. Use default settings
     * specific to this instance of {@code Comparer}.
     *
     * @param id ID of the comparison
     * @throws VisualComparisonFailedException if the visual comparison fails. Depending on the settings, this may also
     * trigger generating a report containing details of what parts of the page did not match the baseline.
     * @throws IllegalStateException if settings associated with this comparison are invalid for the purpose of this
     * comparison
     */
    void compare(@Nonnull String id);

    /**
     * Take a snapshot of the current page and compare it with a baseline using given {@code id}. Use {@code settings}
     * in preference, or in addition to any settings specified for this {@code Comparer} itself.
     *
     * @param id ID of the comparison
     * @param settings extra settings specific to this comparison
     * @throws VisualComparisonFailedException if the visual comparison fails. Depending on the settings, this may also
     * trigger generating a report containing details of what parts of the page did not match the baseline.
     * @throws IllegalStateException if settings associated with this comparison are invalid for the purpose of this
     * comparison
     */
    void compare(@Nonnull String id, @Nonnull ComparisonSettings settings);
}
