package com.atlassian.pageobjects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The default values for a {@link com.atlassian.pageobjects.TestedProduct}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Defaults {

    /**
     * @return the default instance id
     */
    String instanceId();

    /**
     * @return the default context path
     */
    String contextPath();

    /**
     * @return the default HTTP port
     */
    int httpPort();
}
