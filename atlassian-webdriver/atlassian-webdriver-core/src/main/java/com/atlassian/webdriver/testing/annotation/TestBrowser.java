package com.atlassian.webdriver.testing.annotation;

import com.atlassian.webdriver.testing.rule.TestBrowserRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * This allows tests methods, classes or packages to be annotated with
 * the browser string that will be set if using the  {@link TestBrowserRule}
 * so that when the test runs it runs with a specific browser, e.g. @TestBrowser("firefox").
 *
 * @since 2.1
 * @see {@link TestBrowserRule}
 * @deprecated use {@link com.atlassian.pageobjects.browser.RequireBrowser} instead. Scheduled for removal in 3.0
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ElementType.TYPE, ElementType.PACKAGE, ElementType.METHOD})
@Inherited
@Deprecated
public @interface TestBrowser
{
    String value();
}
