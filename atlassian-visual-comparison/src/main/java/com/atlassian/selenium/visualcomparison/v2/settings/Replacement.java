package com.atlassian.selenium.visualcomparison.v2.settings;

import com.atlassian.annotations.ExperimentalApi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Replacement for part of the compared page with some alternative {@code html}.
 *
 * <p/>
 * Currently the only way to identify the replaced part is by element ID, but this is subject to change in the future.
 *
 * @since 2.3
 */
@Immutable
@ExperimentalApi
public class Replacement
{
    private final String elementId;
    private final String html;

    private Replacement(String elementId, String html)
    {
        this.elementId = elementId;
        this.html = html;
    }

    @Nonnull
    public static Replacement forId(@Nonnull String elementId, @Nonnull String html)
    {
        return new Replacement(checkNotNull(elementId, "id"), checkNotNull(html, "html"));
    }

    @Nonnull
    public String getElementId()
    {
        return elementId;
    }

    @Nonnull
    public String getHtml()
    {
        return html;
    }
}
