package com.atlassian.pageobjects.elements.query;


import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * <p>
 * Represents a repeatable query over the state of the test that results in an object instance of a particular type
 * <tt>T</tt>. The query may be evaluated immediately ({@link #now()}, or within some timeout
 * ({@link #byDefaultTimeout()}, {@link #by(long)}, {@link #by(long, java.util.concurrent.TimeUnit)}).
 * Usually there is a specific value of <tt>T</tt> expected by the query and it will not return until the
 * underlying query evaluates to this expected value, or the timeout expires.
 *
 * <p>
 * The specific semantics of how the polling is performed are up to implementations of this interface and should be
 * documented appropriately. E.g. a particular implementation may be waiting for a specific value <i>t</i> of <tt>T</tt>,
 * and returning the real value <i>s</i>, if the query does not return <i>t</i> within the given timeout. Other
 * implementations may throw exceptions, or return <code>null</code>, if an expected query result value is not returned
 * within timeout, etc.
 *
 *
 * @param <T> type of the query result
 */
@NotThreadSafe
public interface TimedQuery<T> extends PollingQuery
{

    /**
     * Evaluate this query by a timeout deemed default by this query.
     *
     * @return expected value of <tt>T</tt>, or any suitable value, if the expected value was not returned before
     * the default timeout expired
     * @see #defaultTimeout() 
     */
    T byDefaultTimeout();

    /**
     * Evaluate this query by given timeout. That is, return the expected <tt>T</tt> as soon as the query evaluates
     * to the expected value, otherwise perform any appropriate operation when the <tt>timeout</tt> expires (e.g.
     * return real value, <code>null</code>, or throw exception
     *
     * @param timeoutInMillis timeout in milliseconds (must be greater than 0)
     * @return expected value of <tt>T</tt>, or any suitable value, if the expected value was not returned before
     * <tt>timeout</tt> expired
     */
    T by(long timeoutInMillis);

    /**
     * Evaluate this query by given timeout. That is, return the expected <tt>T</tt> as soon as the query evaluates
     * to the expected value, otherwise perform any appropriate operation when the <tt>timeout</tt> expires (e.g.
     * return real value, <code>null</code>, or throw exception
     *
     * @param timeout timeout (must be greater than 0)
     * @param unit the unit that the timeout is in
     * @return expected value of <tt>T</tt>, or any suitable value, if the expected value was not returned before
     * <tt>timeout</tt> expired
     */
    T by(long timeout, TimeUnit unit);

    /**
     * Evaluate this query immediately.
     *
     * @return current evaluation of the underlying query.
     */
    T now();
}
