package com.atlassian.selenium.visualcomparison.v2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Raised when {@link Comparer#compare(String) visual comparison} fails for any reason. The details of the comparison
 * and the problem should be provided by this exception.
 *
 * @since 2.3
 */
public class VisualComparisonFailedException extends RuntimeException
{
    private final String id;
    // more context?

    public VisualComparisonFailedException(@Nonnull String id)
    {
        this.id = checkNotNull(id, "id");
    }

    public VisualComparisonFailedException(@Nonnull String id, @Nullable String message)
    {
        super(message);
        this.id = checkNotNull(id, "id");
    }

    public VisualComparisonFailedException(@Nonnull String id, @Nullable String message, @Nullable Throwable cause)
    {
        super(message, cause);
        this.id = checkNotNull(id, "id");
    }

    /**
     * @return Comparison ID
     */
    @Nonnull
    public String getId()
    {
        return id;
    }
}
