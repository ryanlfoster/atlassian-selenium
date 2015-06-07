package com.atlassian.webdriver.jira.component;

import org.openqa.selenium.By;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ClickableLink
{
    String id() default "";
    
    String className() default "";
    
    String linkText() default "";
    
    String partialLinkText() default "";

    Class nextPage();
}
