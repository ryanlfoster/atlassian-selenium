package com.atlassian.webdriver.utils;

import com.atlassian.pageobjects.browser.Browser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
@Deprecated
public @interface SkipInBrowser
{
    Browser[] browsers();
}