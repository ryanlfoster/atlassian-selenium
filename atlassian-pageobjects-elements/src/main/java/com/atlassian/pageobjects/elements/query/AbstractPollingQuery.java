package com.atlassian.pageobjects.elements.query;

import org.hamcrest.StringDescription;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract implementation of the {@link PollingQuery} interface.
 *
 */
public class AbstractPollingQuery implements PollingQuery
{
    protected final long interval;
    protected final long defaultTimeout;

    protected AbstractPollingQuery(long interval, long defaultTimeout)
    {
        checkArgument(interval > 0, new StringDescription().appendText("interval is ").appendValue(interval)
                .appendText(" should be > 0").toString());
        checkArgument(defaultTimeout > 0, new StringDescription().appendText("defaultTimeout is ").appendValue(defaultTimeout)
                .appendText(" should be > 0").toString());
        checkArgument(defaultTimeout >= interval, new StringDescription().appendText("defaultTimeout is ").appendValue(defaultTimeout)
                .appendText(" interval is ").appendValue(interval).appendText(" defaultTimeout should be >= interval").toString());
        this.interval = interval;
        this.defaultTimeout = defaultTimeout;
    }

    protected AbstractPollingQuery(PollingQuery other)
    {
        this(checkNotNull(other, "other").interval(), other.defaultTimeout());
    }

    public long interval()
    {
        return interval;
    }

    public long defaultTimeout()
    {
        return defaultTimeout;
    }
}
