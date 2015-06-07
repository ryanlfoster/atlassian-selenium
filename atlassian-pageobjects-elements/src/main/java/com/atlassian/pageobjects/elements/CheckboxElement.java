package com.atlassian.pageobjects.elements;

/**
 * Represents an input element with type=checkbox
 */
public interface CheckboxElement extends PageElement
{

    /**
     * @return {@code true}, if this checkbox is currently checked
     * @since 2.3
     */
    boolean isChecked();

    /**
     * Checks this Checkbox
     *
     * @return CheckboxElement
     */
    CheckboxElement check();

    /**
     * Unchecks this Checkbox
     *
     * @return CheckboxElement
     */
    CheckboxElement uncheck();
}
