package com.atlassian.pageobjects.elements.query;

import com.atlassian.annotations.PublicApi;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Utility class to poll and wait for a particular states of timeout-based queries inheriting from {@link PollingQuery}.
 *
 * @see PollingQuery
 * @see TimedQuery
 * @see TimedCondition
 */
@PublicApi
public final class Poller
{
    private Poller() {
        throw new AssertionError("Don't instantiate me");
    }

    /**
     * Wait until given <tt>condition</tt> is <code>true</code> by default timeout
     *
     * @param condition condition to exercise
     */
    public static void waitUntilTrue(TimedQuery<Boolean> condition)
    {
        waitUntil(condition, is(true));
    }

    /**
     * Wait until given <tt>condition</tt> is <code>true</code> by default timeout, with custom error message.
     *
     * @param message error message
     * @param condition condition to exercise
     */
    public static void waitUntilTrue(String message, TimedQuery<Boolean> condition)
    {
        waitUntil(message, condition, is(true));
    }

    /**
     * Wait until given <tt>condition</tt> is <code>false</code> by default timeout
     *
     * @param condition condition to exercise
     */
    public static void waitUntilFalse(TimedQuery<Boolean> condition)
    {
        waitUntil(condition, is(false));
    }

    /**
     * Wait until given <tt>condition</tt> is <code>false</code> by default timeout, with custom error message.
     *
     * @param message error message
     * @param condition condition to exercise
     */
    public static void waitUntilFalse(String message, TimedQuery<Boolean> condition)
    {
        waitUntil(message, condition, is(false));
    }

    /**
     * Wait until given <tt>query</tt> evaluates to actual value that is equal to <tt>expectedValue</tt>
     * by default timeout.
     *
     * @param expectedValue expected value
     * @param query query to evaluate
     * @return last value from the query, satisfying the expected value
     */
    public static <T> T waitUntilEquals(T expectedValue, TimedQuery<T> query)
    {
        return waitUntil(query, equalTo(expectedValue));
    }

    /**
     * Wait until given <tt>query</tt> evaluates to actual value that is equal to <tt>expectedValue</tt> by default timeout,
     * with custom error message.
     *
     * @param message error message
     * @param expectedValue expected value
     * @param query query to evaluate
     * @return last value from the query, satisfying the expected value
     */
    public static <T> T waitUntilEquals(String message, T expectedValue, TimedQuery<T> query)
    {
        return waitUntil(message, query, equalTo(expectedValue));
    }

    /**
     * <p>
     * Wait until given <tt>query</tt> fulfils certain condition specified by the <tt>matcher</tt>, by
     * default timeout of the <tt>query</tt>.
     *
     * <p>
     * Use any matcher from the available libraries (e.g. Hamcrest, JUnit etc.), or a custom one.
     *
     * @param query timed query to evaluate
     * @param matcher a matcher representing the assertion condition
     * @see Matcher
     * @see org.hamcrest.Matchers
     * @return last value from the query, satisfying the matcher
     */
    public static <T> T waitUntil(TimedQuery<T> query, Matcher<? super T> matcher)
    {
        return waitUntil(null, query, matcher, byDefaultTimeout());
    }

    /**
     * <p>
     * Wait until given <tt>query</tt> fulfils certain condition specified by the <tt>matcher</tt>, by
     * default timeout of the <tt>query</tt>.
     *
     * <p>
     * Use any matcher from the available libraries (e.g. Hamcrest, JUnit etc.), or a custom one.
     *
     * @param message message displayed in case of failure
     * @param query timed query to evaluate
     * @param matcher a matcher representing the assertion condition
     * @see Matcher
     * @see org.hamcrest.Matchers
     * @return last value from the query, satisfying the matcher
     */
    public static <T> T waitUntil(String message, TimedQuery<T> query, Matcher<? super T> matcher)
    {
        return waitUntil(message, query, matcher, byDefaultTimeout());
    }

    /**
     * <p>
     * Wait until given <tt>query</tt> fulfils certain condition specified by given the <tt>matcher</tt>, by
     * given <tt>timeout</tt>.
     *
     * <p>
     * Use any matcher from the available libraries (e.g. Hamcrest, JUnit etc.), or a custom one.
     *
     * <p>
     * To specify desired timeout, use one of four provided timeouts: {@link #now()}, {@link #byDefaultTimeout()},
     * #{@link #by(long)}, {@link #by(long, java.util.concurrent.TimeUnit)}.
     *
     * @param query timed query to evaluate
     * @param matcher a matcher representing the assertion condition
     * @param timeout time limit for this wait
     * @see Matcher
     * @see org.hamcrest.Matchers
     * @see #now()
     * @see #by(long)
     * @see #by(long, java.util.concurrent.TimeUnit)
     * @see #byDefaultTimeout()
     * @return last value from the query, satisfying the matcher
     */
    public static <T> T waitUntil(TimedQuery<T> query, Matcher<? super T> matcher, WaitTimeout timeout)
    {
        return waitUntil(null, query, matcher, timeout);
    }


    /**
     * <p>
     * Wait until given <tt>query</tt> fulfils certain condition specified by given the <tt>matcher</tt>, by
     * given <tt>timeout</tt>.
     *
     * <p>
     * Use any matcher from the libraries available (e.g. Hamcrest, JUnit etc.), or a custom one.
     *
     * <p>
     * To specify desired timeout, use one of four provided timeouts: {@link #now()}, {@link #byDefaultTimeout()},
     * #{@link #by(long)}, {@link #by(long, java.util.concurrent.TimeUnit)}.
     *
     * @param message message displayed for failed assertion
     * @param query timed query to verify
     * @param matcher a matcher representing the assertion condition
     * @param timeout timeout of the assertion
     * @see Matcher
     * @see org.hamcrest.Matchers
     * @see #now()
     * @see #by(long)
     * @see #by(long, java.util.concurrent.TimeUnit)
     * @see #byDefaultTimeout()
     * @return last value from the query, satisfying the matcher
     */
    public static <T> T waitUntil(String message, TimedQuery<T> query, Matcher<? super T> matcher, WaitTimeout timeout)
    {
        checkNotNull(timeout);
        final Conditions.MatchingCondition<T> assertion = new Conditions.MatchingCondition<T>(query, matcher);
        if (!timeout.evaluate(assertion))
        {
            throw new AssertionError(buildMessage(message, assertion, matcher, timeout));
        }
        return assertion.lastValue;
    }

    private static <T> String buildMessage(String message, Conditions.MatchingCondition<T> assertion,
                                           Matcher<? super T> matcher, WaitTimeout timeout)
    {
        final Description answer = new StringDescription();
        if (StringUtils.isNotEmpty(message))
        {
            answer.appendText(message).appendText(":\n");
        }
        return answer.appendText("Query ").appendValue(assertion.query).appendText("\nExpected: ")
                .appendDescriptionOf(matcher).appendText(timeout.msgTimeoutSuffix(assertion))
                .appendText("\nGot (last value): ").appendValue(assertion.lastValue).toString();
    }




    public static abstract class WaitTimeout
    {
        // we don't want this to be instantiated, or extended by anybody
        private WaitTimeout() {}

        abstract boolean evaluate(TimedCondition condition);

        abstract String msgTimeoutSuffix(TimedCondition condition);
    }


    /**
     * Timeout indicating that the assertion method will evaluate the assertion condition immediately, without waiting.
     *
     * @return new immediate assertion timeout
     */
    public static WaitTimeout now()
    {
        return new WaitTimeout()
        {
            @Override
            boolean evaluate(final TimedCondition condition)
            {
                return condition.now();
            }
            @Override
            String msgTimeoutSuffix(final TimedCondition condition)
            {
                return " immediately";
            }
        };
    }

    /**
     * Timeout indicating that the assertion method will wait for the default timeout of exercised timed query for
     * the condition to become <code>true</code>.
     *
     * @return new default assertion timeout
     */
    public static WaitTimeout byDefaultTimeout()
    {
        return new WaitTimeout()
        {
            @Override
            boolean evaluate(final TimedCondition condition)
            {
                return condition.byDefaultTimeout();
            }
            @Override
            String msgTimeoutSuffix(final TimedCondition condition)
            {
                return " by " + condition.defaultTimeout() + "ms (default timeout)";
            }
        };
    }

    /**
     * Custom assertion timeout expressed in milliseconds. E.g. <code>TimedAssertions.by(500L);</code>
     * passed to an assertion method will make it wait 500 milliseconds for given condition to become <code>true</code>.
     *
     * @param timeoutInMillis number of milliseconds to wait for the assertion condition
     * @return new custom timeout assertion
     */
    public static WaitTimeout by(final long timeoutInMillis)
    {
        return new WaitTimeout()
        {
            @Override
            boolean evaluate(final TimedCondition condition)
            {
                return condition.by(timeoutInMillis);
            }
            @Override
            String msgTimeoutSuffix(final TimedCondition condition)
            {
                return " by " + timeoutInMillis + "ms";
            }
        };
    }

    /**
     * Custom assertion timeout expressed in a number of time units. E.g. <code>TimedAssertions.by(5, TimeUnit.SECONDS);</code>
     * passed to an assertion method will make it wait 5 seconds for given condition to become <code>true</code>.
     *
     * @param timeout timeout count
     * @param unit unit of the timeout
     * @return new custom timeout assertion
     */
    public static WaitTimeout by(final long timeout, final TimeUnit unit)
    {
        return new WaitTimeout()
        {
            @Override
            boolean evaluate(final TimedCondition condition)
            {
                return condition.by(timeout, unit);
            }
            @Override
            String msgTimeoutSuffix(final TimedCondition condition)
            {
                return " by " + unit.toMillis(timeout) + "ms";
            }
        };
    }

}
