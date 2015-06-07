package com.atlassian.pageobjects.elements.query;

/**
 * Strategies for handling expired timeouts of the {@link TimedQuery}.
 *
 */
public interface ExpirationHandler
{

    /**
     * Handle timeout expiration for given query.
     *
     * @param <T> type of the query result
     * @param query timed query, whose timeout has expired
     * @param currentValue current evaluation of the query
     * @param timeout timeout of the query
     * @return result value to be returned by the query ater the timeout expiration
     */
    <T> T expired(TimedQuery<T> query, T currentValue, long timeout);


    
    public static final ExpirationHandler RETURN_CURRENT = new ExpirationHandler()
    {
        public <T> T expired(TimedQuery<T> query, T currentValue, long timeout)
        {
            return currentValue;
        }
    };

    public static final ExpirationHandler RETURN_NULL = new ExpirationHandler()
    {
        public <T> T expired(TimedQuery<T> query, T currentValue, long timeout)
        {
            return null;
        }
    };

    public static final ExpirationHandler THROW_ASSERTION_ERROR = new ExpirationHandler()
    {
        public <T> T expired(TimedQuery<T> query, T currentValue, long timeout)
        {
            throw new AssertionError("Timeout <" + timeout + "> expired for: " + query);
        }
    };

    public static final ExpirationHandler THROW_ILLEGAL_STATE = new ExpirationHandler()
    {
        public <T> T expired(TimedQuery<T> query, T currentValue, long timeout)
        {
            throw new IllegalStateException("Timeout <" + timeout + "> expired for: " + query);
        }
    };

}
