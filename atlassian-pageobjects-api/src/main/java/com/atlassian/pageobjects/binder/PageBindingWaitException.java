package com.atlassian.pageobjects.binder;

/**
 * Thrown if a problem occurs when waiting for a page binding
 */
public class PageBindingWaitException extends PageBindingException
{
    public PageBindingWaitException(Object pageObject, Throwable cause)
    {
        super(pageObject, cause);
    }

    public PageBindingWaitException(String message, Object pageObject)
    {
        super(message, pageObject);
    }

    public PageBindingWaitException(String message, Object pageObject, Throwable cause)
    {
        super(message, pageObject, cause);
    }
}
