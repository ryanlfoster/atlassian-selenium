package com.atlassian.selenium.visualcomparison.v2;

import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.selenium.visualcomparison.ScreenElement;
import com.atlassian.selenium.visualcomparison.VisualComparableClient;
import com.atlassian.selenium.visualcomparison.VisualComparer;
import com.atlassian.selenium.visualcomparison.utils.BoundingBox;
import com.atlassian.selenium.visualcomparison.utils.ScreenResolution;
import com.atlassian.selenium.visualcomparison.v2.settings.PagePart;
import com.atlassian.selenium.visualcomparison.v2.settings.Replacement;
import com.atlassian.selenium.visualcomparison.v2.settings.Resolution;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.util.List;
import java.util.Map;

import static com.atlassian.selenium.visualcomparison.v2.ComparisonSettings.emptySettings;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;

/**
 * Current default implementation of {@link Comparer}. This implementation delegates the visual comparison requests to
 * the {@link com.atlassian.selenium.visualcomparison.VisualComparer old visual comparison library}, transforming the
 * request, the settings and the results in the process.
 *
 * <p/>
 * {@link BrowserEngine} is used as the underlying SPI for the browser automation framework and
 * {@link ComparisonSettings} as the vehicle for configuring the visual comparison, both at the
 * {@link #DefaultComparer(BrowserEngine, ComparisonSettings) instance} and at the
 * {@link #compare(String, ComparisonSettings) single comparison} level.
 *
 * <p/>
 * NOTE: this implementation is likely to change in the future to migrate away from the old library. Depending on
 * implementation details of this class is highly discouraged.
 *
 * @since 2.3
 */
@ExperimentalApi
@Immutable
public final class DefaultComparer implements Comparer
{
    private final BrowserEngine engine;
    private final ComparisonSettings settings;

    public DefaultComparer(@Nonnull BrowserEngine engine, ComparisonSettings settings) {
        this.engine = checkNotNull(engine, "engine");
        this.settings = checkNotNull(settings, "settings");
    }

    @Override
    public void compare(@Nonnull String id)
    {
        compare(id, emptySettings());
    }

    @Override
    public void compare(@Nonnull String id, @Nonnull ComparisonSettings extraSettings)
    {
        checkNotNull(id, "id");
        checkNotNull(extraSettings, "settings");
        final ComparisonSettings effectiveSettings = settings.merge(extraSettings);
        validateSettings(effectiveSettings);
        VisualComparer comparer = getComparer(effectiveSettings);
        try
        {
            if (!comparer.uiMatches(id, effectiveSettings.getBaselineDirectory().getAbsolutePath()))
            {
                String message = "Screenshots did not match the baseline in "
                        + effectiveSettings.getBaselineDirectory().getAbsolutePath() + ".";
                if (effectiveSettings.isReportingEnabled())
                {
                    message += " Check reports in " + effectiveSettings.getReportingDirectory().getAbsolutePath()
                            + " for more details.";
                }
                throw new VisualComparisonFailedException(id, message);
            }
        }
        catch (Exception e)
        {
            if (e instanceof VisualComparisonFailedException)
            {
                throw (VisualComparisonFailedException) e;
            }
            else
            {
                throw new VisualComparisonFailedException(id, "Error when performing comparison", e);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void validateSettings(ComparisonSettings effectiveSettings)
    {
        checkState(effectiveSettings.getBaselineDirectory() != null, "Baseline directory must be provided");
        if (!effectiveSettings.getBaselineDirectory().isDirectory())
        {
            checkState(effectiveSettings.getBaselineDirectory().mkdirs(), "Unable to create baseline directory "
                    + effectiveSettings.getBaselineDirectory().getAbsolutePath());
        }
        if (effectiveSettings.isReportingEnabled() && !effectiveSettings.getReportingDirectory().isDirectory())
        {
            checkState(effectiveSettings.getReportingDirectory().mkdirs(), "Unable to create reporting directory "
                                + effectiveSettings.getReportingDirectory().getAbsolutePath());
        }
    }

    private VisualComparer getComparer(ComparisonSettings effectiveSettings)
    {
        VisualComparer comparer = new VisualComparer(new BrowserEngineComparableClient());
        comparer.setScreenResolutions(getResolutions(effectiveSettings));
        if (effectiveSettings.isReportingEnabled())
        {
            comparer.enableReportGeneration(effectiveSettings.getReportingDirectory().getAbsolutePath());
        }
        comparer.setIgnoreSingleLineDiffs(effectiveSettings.isIgnoreSingleLineDifferences());
        comparer.setRefreshAfterResize(effectiveSettings.isRefreshAfterResize());
        comparer.setIgnoreAreas(getIgnoreAreas(effectiveSettings));
        comparer.setUIStringReplacements(getReplacements(effectiveSettings));
        comparer.setWaitforJQueryTimeout(5000);
        return comparer;
    }

    private ScreenResolution[] getResolutions(ComparisonSettings effectiveSettings)
    {
        return toArray(transform(effectiveSettings.getResolutions(), new Function<Resolution, ScreenResolution>()
        {
            @Nullable
            @Override
            public ScreenResolution apply(Resolution input)
            {
                return new ScreenResolution(input.getWidth(), input.getHeight());
            }
        }), ScreenResolution.class);
    }

    private List<BoundingBox> getIgnoreAreas(ComparisonSettings effectiveSettings)
    {
        return ImmutableList.copyOf(transform(effectiveSettings.getIgnoredParts(), new Function<PagePart, BoundingBox>()
        {
            @Nullable
            @Override
            public BoundingBox apply(PagePart input)
            {
                return new BoundingBox(input.getLeft(), input.getTop(), input.getRight(), input.getBottom());
            }
        }));
    }

    private Map<String, String> getReplacements(ComparisonSettings effectiveSettings)
    {
        ImmutableMap.Builder<String,String> builder = ImmutableMap.builder();
        for (Replacement replacement : effectiveSettings.getReplacements())
        {
            builder.put(replacement.getElementId(), replacement.getHtml());
        }
        return builder.build();
    }

    private final class BrowserEngineComparableClient implements VisualComparableClient
    {

        @Override
        public void captureEntirePageScreenshot(String filePath)
        {
            engine.captureScreenshotTo(new File(filePath));
        }

        @Override
        public ScreenElement getElementAtPoint(int x, int y)
        {
            return engine.getElementAt(x, y);
        }

        @Override
        public void evaluate(String command)
        {
            execute(command);
        }

        @Override
        public Object execute(String command, Object... arguments)
        {
            return engine.executeScript(Object.class, command, arguments);
        }

        @Override
        public boolean resizeScreen(ScreenResolution resolution, boolean refreshAfterResize)
        {
            engine.resizeTo(new Resolution((int) resolution.getWidth(), (int) resolution.getHeight()));
            if (refreshAfterResize)
            {
                refreshAndWait();
            }
            return true;
        }

        @Override
        public void refreshAndWait()
        {
            engine.reloadPage();
        }

        @Override
        public boolean waitForJQuery(long waitTimeMillis)
        {
            Long jQueryActive = null;
            try
            {
                while (jQueryActive == null || jQueryActive != 0)
                {
                    jQueryActive = engine.executeScript(Long.class, "return window.jQuery.active");
                    Thread.sleep(100);
                }
            }
            catch (InterruptedException e)
            {
                return false;
            }
            return true;
        }
    }
}
