package com.atlassian.webdriver.testing.annotation;

import com.atlassian.pageobjects.TestedProduct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test class with a 'hint' what tested product class should be used to create context and run that test.
 *
 * @since 2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TestedProductClass
{
    Class<? extends TestedProduct<?>> value();
}
