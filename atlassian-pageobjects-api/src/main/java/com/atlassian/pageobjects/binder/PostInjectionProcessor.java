package com.atlassian.pageobjects.binder;

public interface PostInjectionProcessor
{
    <T> T process(T pageObject);
}
