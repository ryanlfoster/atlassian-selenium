package com.atlassian.webdriver.waiter;

import com.atlassian.annotations.ExperimentalApi;

/**
 * WARNING: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public interface ExecutableWaiterQuery
{
    /**
     * Executes the built up waiter query.
     */
    void execute();

    /**
     * Allows chaining of queries together and will pass when
     * both queries evaluates to true
     */
    WaiterQuery and();

    /**
     * Allows chaining of queries together and will pass when
     * either query evaluates to true
     */
    WaiterQuery or();
}
