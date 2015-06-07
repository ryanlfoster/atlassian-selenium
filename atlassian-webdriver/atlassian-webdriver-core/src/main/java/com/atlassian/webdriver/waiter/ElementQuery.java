package com.atlassian.webdriver.waiter;

import com.atlassian.annotations.ExperimentalApi;

/**
 * WARNING: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public interface ElementQuery
{
    ExecutableWaiterQuery isVisible();
    ExecutableWaiterQuery isNotVisible();

    ExecutableWaiterQuery exists();
    ExecutableWaiterQuery doesNotExist();

    StringValueQuery getAttribute(String attributeName);

    ExecutableWaiterQuery isSelected();
    ExecutableWaiterQuery isNotSelected();

    ExecutableWaiterQuery isEnabled();
    ExecutableWaiterQuery isNotEnabled();

    ExecutableWaiterQuery hasClass(String className);
    ExecutableWaiterQuery doesNotHaveClass(String className);

    StringValueQuery getText();
}
