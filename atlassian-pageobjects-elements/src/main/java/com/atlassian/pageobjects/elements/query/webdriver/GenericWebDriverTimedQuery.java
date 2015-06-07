package com.atlassian.pageobjects.elements.query.webdriver;

import com.atlassian.pageobjects.elements.query.AbstractTimedQuery;
import com.atlassian.pageobjects.elements.query.ExpirationHandler;
import com.atlassian.pageobjects.elements.query.util.Clock;
import com.atlassian.pageobjects.elements.query.util.SystemClock;
import com.google.common.base.Supplier;

import javax.annotation.concurrent.NotThreadSafe;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Generic, WebDriver-based implementation of {@link com.atlassian.pageobjects.elements.query.TimedQuery}.
 *
 * <p>
 * It accepts a supplier of the target value and uses it to retrieve current value od the query. The functions are
 * supposed to throw {@link com.atlassian.pageobjects.elements.query.webdriver.GenericWebDriverTimedQuery.InvalidValue}
 * to indicate that the current value should not be accepted as valid and returned by the query.
 *
 */
@NotThreadSafe
public class GenericWebDriverTimedQuery<T> extends AbstractTimedQuery<T>
{
    protected final Supplier<T> valueSupplier;
    private boolean invalidValue;

    public GenericWebDriverTimedQuery(Supplier<T> supplier, Clock clock, long defTimeout,
            long interval, ExpirationHandler eh)
    {
        super(clock, defTimeout, interval, eh);
        this.valueSupplier = checkNotNull(supplier);
    }

    public GenericWebDriverTimedQuery(Supplier<T> supplier, long defTimeout, long interval,
            ExpirationHandler eh)
    {
        this(supplier, SystemClock.INSTANCE, defTimeout, interval, eh);
    }

    public GenericWebDriverTimedQuery(Supplier<T> supplier, long defTimeout, long interval)
    {
        this(supplier, SystemClock.INSTANCE, defTimeout, interval, ExpirationHandler.RETURN_CURRENT);
    }

    public GenericWebDriverTimedQuery(Supplier<T> supplier, long defTimeout)
    {
        this(supplier, SystemClock.INSTANCE, defTimeout, DEFAULT_INTERVAL, ExpirationHandler.RETURN_CURRENT);
    }

    public GenericWebDriverTimedQuery(GenericWebDriverTimedQuery<T> origin, long timeout)
    {
        this(origin.valueSupplier, origin.clock(), timeout, origin.interval(), origin.expirationHandler());
    }

    @Override
    protected final boolean shouldReturn(final T currentEval)
    {
        return !invalidValue;
    }

    @Override
    protected final T currentValue()
    {
        invalidValue = false;
        try
        {
            return valueSupplier.get();
        }
        catch (InvalidValue e)
        {
            invalidValue = true;
            return (T)e.value;
        }
    }

    public static final class InvalidValue extends RuntimeException
    {
        private static final long serialVersionUID = -4972134972343443297L;

        private final Object value;

        public InvalidValue(final Object value)
        {
            this.value = value;
        }

        @Override
        public Throwable fillInStackTrace()
        {
            return this;
        }
        
    }
}
