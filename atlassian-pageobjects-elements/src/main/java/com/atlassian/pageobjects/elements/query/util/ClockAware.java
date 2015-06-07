package com.atlassian.pageobjects.elements.query.util;

/**
 * Marks classes that perform time computations and are using {@link Clock}s.
 *
 */
public interface ClockAware
{

    /**
     * Clock used by this instance.
     *
     * @return clock
     */
    Clock clock();
}
