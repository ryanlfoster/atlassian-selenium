package com.atlassian.performance;

import com.google.inject.AbstractModule;

import static com.google.inject.matcher.Matchers.any;

public class TimeMethodModule extends AbstractModule
{
    @Override
    protected void configure()
    {
	bindInterceptor(any(), any(), new TimeMethodInterceptor());
    }
}
