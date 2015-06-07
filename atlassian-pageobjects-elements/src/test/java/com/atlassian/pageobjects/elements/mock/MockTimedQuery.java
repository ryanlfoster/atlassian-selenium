package com.atlassian.pageobjects.elements.mock;

import com.atlassian.pageobjects.elements.query.AbstractTimedQuery;
import com.atlassian.pageobjects.elements.query.ExpirationHandler;
import com.atlassian.pageobjects.elements.query.util.Clock;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Mock implementation of {@link com.atlassian.pageobjects.elements.query.TimedQuery}.
 *
 */
public class MockTimedQuery<T> extends AbstractTimedQuery<T>
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MockTimedQuery.class);

    private LinkedList<T> returnValues;
    private boolean returnLast = true;
    private boolean returnNull = false;
    private boolean returnAll = false;

    private int currentEval = 0;

    public MockTimedQuery(long defTimeout, long interval, ExpirationHandler expirationHandler)
    {
        super(defTimeout, interval, expirationHandler);
    }

    public MockTimedQuery(Clock clock, long defTimeout, long interval, ExpirationHandler expirationHandler)
    {
        super(clock, defTimeout, interval, expirationHandler);
    }


    public MockTimedQuery<T> returnValues(T... returnValues)
    {
        this.returnValues = new LinkedList<T>(Arrays.asList(returnValues));
        return this;
    }

    public MockTimedQuery<T> returnNull()
    {
        this.returnNull = true;
        this.returnLast = false;
        this.returnAll = false;
        return this;
    }

    public MockTimedQuery<T> returnLast()
    {
        this.returnLast = true;
        this.returnNull = false;
        this.returnAll = false;
        return this;
    }

    public MockTimedQuery<T> returnAll()
    {
        this.returnAll = true;
        this.returnNull = false;
        this.returnLast = false;
        return this;
    }

    @Override
    protected boolean shouldReturn(T currentEval)
    {
        if (returnNull && currentEval == null)
        {
            return true;
        }
        if (returnLast && returnValues.getLast().equals(currentEval))
        {
            return true;
        }
        if (returnAll)
        {
            return true;
        }
        return false;
    }

    @Override
    protected T currentValue()
    {
        log.debug("#currentValue: return values=" + returnValues + ",currentEval=" + currentEval);        
        if (returnNull)
        {
            return null;
        }
        return returnValues.get(currentEval++);
    }
}
