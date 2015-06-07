package com.atlassian.pageobjects.browser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that should not be called if the current test browser matches any of the given browsers.
 *
 * @since 2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE })
public @interface IgnoreBrowser
{
    Browser[] value() default {Browser.ALL};

    String reason() default ("No reason provided");
}
