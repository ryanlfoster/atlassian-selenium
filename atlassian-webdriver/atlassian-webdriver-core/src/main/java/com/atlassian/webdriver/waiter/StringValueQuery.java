package com.atlassian.webdriver.waiter;

import com.atlassian.annotations.ExperimentalApi;

/**
 * <strong>WARNING</strong>: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public interface StringValueQuery
{
    ExecutableWaiterQuery contains(String value);
    ExecutableWaiterQuery notContains(String value);

    /**
     * isEqual will evaluate to true if both values are null or equal.
     */
    ExecutableWaiterQuery isEqual(String value);
    ExecutableWaiterQuery notEqual(String value);

    /**
     * isEmpty should evaludate to true if the value is null
     * or is the empty String
     */
    ExecutableWaiterQuery isEmpty();

    /**
     * isNotEmpty should evaluate to true if the value is not null
     * and is not the empty String.
     */
    ExecutableWaiterQuery isNotEmpty();

    ExecutableWaiterQuery endsWith(String value);
    ExecutableWaiterQuery doesNotEndWith(String value);

    ExecutableWaiterQuery matches(String value);
    ExecutableWaiterQuery doesNotMatch(String value);

    ExecutableWaiterQuery startsWith(String value);
    ExecutableWaiterQuery doesNotStartWith(String value);

    ExecutableWaiterQuery equalsIgnoresCase(String value);
}
