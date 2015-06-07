package com.atlassian.pageobjects.elements.query;

import static com.atlassian.pageobjects.elements.util.StringConcat.asString;


/**
 * A {@link TimedQuery} that always immediately returns a pre-defined static value.
 *
 */
public final class StaticQuery<T> extends AbstractTimedQuery<T> implements TimedQuery<T>
{
    private final T value;

    public StaticQuery(T value, long defTimeout, long interval)
    {
        super(defTimeout, interval, ExpirationHandler.RETURN_CURRENT);
        this.value = value;
    }

    @Override
    protected boolean shouldReturn(T currentEval)
    {
        return true;
    }

    @Override
    protected T currentValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return asString(super.toString(), "[value=", value, "]");
    }
}
