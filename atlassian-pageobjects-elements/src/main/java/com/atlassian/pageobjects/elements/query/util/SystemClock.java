package com.atlassian.pageobjects.elements.query.util;

/**
 * Real-world clock returning system time.
 *
 */
public final class SystemClock implements Clock
{
    public static final SystemClock INSTANCE = new SystemClock();

    public long currentTime()
    {
        return System.currentTimeMillis();
    }
}
