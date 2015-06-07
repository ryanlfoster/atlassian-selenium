package com.atlassian.webdriver.waiter;

import com.atlassian.annotations.ExperimentalApi;

/**
 * WARNING: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public interface FunctionQuery
{
    ExecutableWaiterQuery isTrue();
    ExecutableWaiterQuery isFalse();
}
