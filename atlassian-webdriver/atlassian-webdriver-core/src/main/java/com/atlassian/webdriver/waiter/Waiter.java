package com.atlassian.webdriver.waiter;

import com.atlassian.annotations.ExperimentalApi;

import java.util.concurrent.TimeUnit;

/**
 * Exposes a set of methods to allow for polling elements for particular conditions
 *
 * <strong>WARNING</strong>: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public interface Waiter
{
    WaiterQuery until();
    WaiterQuery until(long timeoutMillis);
    WaiterQuery until(long timeout, TimeUnit unit);
}
