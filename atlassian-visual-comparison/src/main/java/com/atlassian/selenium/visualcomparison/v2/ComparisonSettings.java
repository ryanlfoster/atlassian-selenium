package com.atlassian.selenium.visualcomparison.v2;

import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.selenium.visualcomparison.v2.settings.PagePart;
import com.atlassian.selenium.visualcomparison.v2.settings.Replacement;
import com.atlassian.selenium.visualcomparison.v2.settings.Resolution;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * Represents settings used for visual comparison. This is in essence an immutable bean that holds various settings.
 *
 * <p/>
 * A single instance of code {@code ComparisonSettings} can be {@link #merge(ComparisonSettings) merged} with another
 * instance, which will result in the other instance 'single' fields overriding this instance's corresponding fields,
 * unless they were not set on the other instance at all. Collection fields are merged by combining collections from
 * both merged instances.
 *
 * <p/>
 * A family of short-hand methods is also available on this class (many of them starting with a {@code with} prefix) to
 * facilitate creating a new instance of settings overriding, or adding just one specific field from the original
 * instance. Those work in essence as if this instance was {@link #merge(ComparisonSettings) merged} with another
 * instance of settings that had just that one particular field set.
 *
 * @since 2.3
 */
@Immutable
@ExperimentalApi
public final class ComparisonSettings
{
    private final Set<Resolution> resolutions;

    private final File baselineDir;
    private final Boolean reportingEnabled;
    private final File reportingDir;

    private final Boolean ignoreSingleLineDifferences;
    private final Boolean refreshAfterResize;

    private final Iterable<PagePart> ignoredParts;
    private final Iterable<Replacement> replacements;

    private ComparisonSettings()
    {
        this(new Builder());
    }

    private ComparisonSettings(Builder builder)
    {
        resolutions = builder.resolutions.build();

        baselineDir = builder.baselineDir;
        reportingEnabled = builder.reportingEnabled;
        reportingDir = builder.reportingDir;

        ignoreSingleLineDifferences = builder.ignoreSingleLineDifferences;
        refreshAfterResize = builder.refreshAfterResize;

        ignoredParts = builder.ignoredParts.build();
        replacements = builder.replacements.build();
    }

    /**
     * Creates a new empty instance of settings. All simple fields are {@code null} (not set) and the collection
     * fields are empty collections.
     *
     * @return new empty instance of {@code ComparisonSettings}.
     */
    @Nonnull
    public static ComparisonSettings emptySettings()
    {
        return new ComparisonSettings();
    }

    @Nonnull
    public Set<Resolution> getResolutions()
    {
        return resolutions;
    }

    @Nullable
    public File getBaselineDirectory()
    {
        return baselineDir;
    }

    public boolean isReportingEnabled()
    {
        return reportingEnabled != null && reportingEnabled;
    }

    @Nullable
    public File getReportingDirectory()
    {
        return reportingDir;
    }

    public boolean isIgnoreSingleLineDifferences()
    {
        return ignoreSingleLineDifferences != null && ignoreSingleLineDifferences;
    }

    public boolean isRefreshAfterResize()
    {
        return refreshAfterResize != null && refreshAfterResize;
    }

    @Nonnull
    public Iterable<PagePart> getIgnoredParts()
    {
        return ignoredParts;
    }

    @Nonnull
    public Iterable<Replacement> getReplacements()
    {
        return replacements;
    }

    @Nonnull
    public ComparisonSettings merge(ComparisonSettings that)
    {
        return new Builder(this).merge(that).build();
    }

    @Nonnull
    public ComparisonSettings withResolution(@Nonnull Resolution resolution)
    {
        return new Builder(this).resolution(resolution).build();
    }

    @Nonnull
    public ComparisonSettings withResolutions(@Nonnull Resolution first, @Nonnull Resolution... more)
    {
        return new Builder(this).resolutions(first, more).build();
    }

    @Nonnull
    public ComparisonSettings withBaselineDirectory(@Nonnull File baselineDirectory)
    {
        return new Builder(this).baselineDirectory(baselineDirectory).build();
    }

    @Nonnull
    public ComparisonSettings withReportingEnabled(@Nonnull File value)
    {
        return new Builder(this).enableReporting(value).build();
    }

    @Nonnull
    public ComparisonSettings withReportingDisabled()
    {
        return new Builder(this).disableReporting().build();
    }

    @Nonnull
    public ComparisonSettings ignoringSingleLineDifferences(boolean value)
    {
        return new Builder(this).ignoreSingleLineDifferences(value).build();
    }

    @Nonnull
    public ComparisonSettings refreshingAfterResize(boolean refreshAfterResize)
    {
        return new Builder(this).refreshAfterResize(refreshAfterResize).build();
    }

    @Nonnull
    public ComparisonSettings ignoringPart(@Nonnull PagePart part) {
        return new Builder(this).ignorePart(part).build();
    }

    @Nonnull
    public ComparisonSettings withReplacement(@Nonnull Replacement replacement) {
        return new Builder(this).replacement(replacement).build();
    }

    private static final class Builder
    {
        private final ImmutableSortedSet.Builder<Resolution> resolutions = ImmutableSortedSet.naturalOrder();

        private File baselineDir;
        private Boolean reportingEnabled;
        private File reportingDir;

        private Boolean ignoreSingleLineDifferences;
        private Boolean refreshAfterResize;

        private ImmutableList.Builder<PagePart> ignoredParts = ImmutableList.builder();
        private ImmutableList.Builder<Replacement> replacements = ImmutableList.builder();

        Builder()
        {
        }

        Builder(@Nonnull ComparisonSettings settings)
        {
            merge(settings);
        }

        Builder merge(@Nonnull ComparisonSettings settings)
        {
            checkNotNull(settings, "settings");
            resolutions.addAll(settings.resolutions);

            baselineDir = settings.baselineDir != null ? settings.baselineDir : baselineDir;
            reportingEnabled = settings.reportingEnabled != null ? settings.reportingEnabled : reportingEnabled;
            reportingDir = settings.reportingEnabled != null ? settings.reportingDir : reportingDir;

            ignoreSingleLineDifferences = settings.ignoreSingleLineDifferences != null
                    ? settings.ignoreSingleLineDifferences : ignoreSingleLineDifferences;
            refreshAfterResize = settings.refreshAfterResize != null ? settings.refreshAfterResize : refreshAfterResize;

            ignoredParts.addAll(settings.ignoredParts);
            replacements.addAll(settings.replacements);
            return this;
        }

        @Nonnull
        Builder resolution(@Nonnull Resolution resolution)
        {
            resolutions.add(resolution);
            return this;
        }

        @Nonnull
        Builder resolutions(@Nonnull Resolution resolution, @Nonnull Resolution... more)
        {
            resolutions.add(resolution).addAll(asList(more));
            return this;
        }

        @Nonnull
        public Builder baselineDirectory(@Nonnull File value)
        {
            this.baselineDir = checkNotNull(value, "baselineDirectory");
            return this;
        }

        @Nonnull
        Builder enableReporting(@Nonnull File value)
        {
            checkNotNull(value, "reportingDir");
            reportingEnabled = true;
            reportingDir = value;
            return this;
        }

        @Nonnull
        Builder disableReporting()
        {
            reportingEnabled = false;
            reportingDir = null;
            return this;
        }

        @Nonnull
        Builder ignoreSingleLineDifferences(boolean value)
        {
            ignoreSingleLineDifferences = value;
            return this;
        }

        @Nonnull
        Builder refreshAfterResize(boolean value)
        {
            refreshAfterResize = value;
            return this;
        }

        @Nonnull
        Builder ignorePart(@Nonnull PagePart part)
        {
            ignoredParts.add(part);
            return this;
        }

        @Nonnull
        Builder ignoreParts(@Nonnull PagePart first, @Nonnull PagePart... more)
        {
            ignoredParts.add(first).addAll(asList(more));
            return this;
        }

        @Nonnull
        Builder replacement(@Nonnull Replacement replacement)
        {
            replacements.add(replacement);
            return this;
        }

        @Nonnull
        Builder replacements(@Nonnull Replacement first, @Nonnull Replacement... more)
        {
            replacements.add(first).addAll(asList(more));
            return this;
        }

        public ComparisonSettings build()
        {
            return new ComparisonSettings(this);
        }
    }
}
