package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.elements.timeout.TimeoutType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injects an Element into the field. Only one of the selector types should be specified.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ElementBy
{
    String id() default "";

    String className() default "";

    String linkText() default "";

    String partialLinkText() default "";

    String cssSelector() default "";

    String name() default "";

    String xpath() default "";

    String tagName() default "";

    /**
     * Timeout type associated with the injected page element.
     *
     * @return timeout of the injected page element
     * @see TimeoutType
     */
    TimeoutType timeoutType() default TimeoutType.DEFAULT;

    /**
     * <p/>
     * Type of the PageElement to inject. Used with Iterator<? extends PageElement>
     * Defaults to the class of the annotated field.
     *
     * <p/>
     * This is not a selector, so you still need to specify a
     * selector.
     *
     * <p/>
     * This attribute doesn't make sense for injecting PageElements fields,
     * but it would still work. It makes sense for erased types:
     * {@code @ElementBy(name="checkbox1", pageElementClass=CheckboxElement.class) 
     * private Iterable<CheckboxElement> checkbox1;
     * }
     *
     * @return target class of the page element
     * @since 2.1
     */
    Class<? extends PageElement> pageElementClass() default PageElement.class;

    /**
     * The name of the instance variable in the same page object class (or superclass) that is
     * also annotated with @ElementBy and is a parent context of the page element annotated with
     * this annotation (i.e. this element on the page is a descendant of the parent element).
     * That parent page element will be used to find this page element withing its scope.
     *
     * @return parent of page element represented by this annotation
     * @since 2.1
     */
    String within() default "";
}
