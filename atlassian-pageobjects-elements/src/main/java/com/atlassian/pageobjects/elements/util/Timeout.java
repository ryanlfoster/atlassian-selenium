package com.atlassian.pageobjects.elements.util;

import java.util.concurrent.TimeUnit;

/**
 * Utility for quick timeout specification.
 *
 */
public final class Timeout
{
    private Timeout()
    {
        throw new AssertionError("Don't hack with me");
    }

    public static TimeoutBuilder of(long count)
    {
        return new TimeoutBuilder(count);
    }

    public static WaitTimeBuilder waitFor(long count)
    {
        return new WaitTimeBuilder(count);
    }

    public static final class TimeoutBuilder
    {
        private final long count;


        TimeoutBuilder(final long count)
        {
            assert count >= 0;
            this.count = count;
        }

        public long milliseconds()
        {
            return count;
        }

        public long seconds()
        {
            return TimeUnit.SECONDS.toMillis(count);
        }
    }

    public static final class WaitTimeBuilder
    {
        private final TimeoutBuilder builder;


        WaitTimeBuilder(long waitTime)
        {
            this.builder = new TimeoutBuilder(waitTime);
        }

        public void milliseconds()
        {
            waitFor(builder.milliseconds());
        }

        public void seconds()
        {
            waitFor(builder.seconds());
        }

        private void waitFor(long millis)
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(millis);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
