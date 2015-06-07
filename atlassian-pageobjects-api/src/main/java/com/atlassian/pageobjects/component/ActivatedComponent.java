package com.atlassian.pageobjects.component;

/**
 * @since 2.1.0
 */
public interface ActivatedComponent<T>
{
    T open();
    T close();
    boolean isOpen();
}
