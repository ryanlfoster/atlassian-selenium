package com.atlassian.selenium;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class MethodHandlerCallable implements Callable<Object>
{
    protected final SeleniumClient client;
    protected final Method method;
    protected Object ret = null;
    protected final Object[] args;
    protected Exception ex = null;

    public MethodHandlerCallable(Method method, SeleniumClient client, Object[] args)
    {
        this.client = client;
        this.method = method;
        this.args = args;
    }

    public Object call() throws Exception
    {
        return method.invoke(client, args);
    }


}