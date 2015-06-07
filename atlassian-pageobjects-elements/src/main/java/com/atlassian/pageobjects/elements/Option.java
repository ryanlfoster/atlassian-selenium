package com.atlassian.pageobjects.elements;

/**
 * An option in select and multi-select components. Depending on the context, not all
 * of the option properties may be set, but at least one of: id/value/text should not be <code>null</code>.
 *
 */
public interface Option
{
    /**
     * Option ID.
     *
     * @return id
     */
    String id();

    /**
     * Option value, which is actually its unique key in the collection of options.
     *
     * @return value
     */
    String value();

    /**
     * Text between the option tags
     *
     * @return text
     */
    String text();
}
