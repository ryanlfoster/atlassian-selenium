package com.atlassian.pageobjects.elements;

import java.util.List;

/**
 * Represents a standard multi-select HTML element.
 *
 */
public interface MultiSelectElement extends PageElement
{
    /**
     * All options
     *
     * @return all options of this multi-select
     */
    List<Option> getAllOptions();

    /**
     * Selected options.
     *
     * @return selected options of this multi-select
     */
    List<Option> getSelected();

    /**
     * Add given option to the current selection.
     *
     * @param option option to add
     * @return this multi-select instance
     */
    MultiSelectElement select(Option option);

    /**
     * Remove given option from the current selection
     *
     * @param option option to remove
     * @return this multi-select instance
     */
    MultiSelectElement unselect(Option option);

    /**
     * Add all options to the current selection.
     *
     * @return this multi-select instance
     */
    MultiSelectElement selectAll();

    /**
     * Remove all options from the current selection
     *
     * @return this multi-select instance
     */
    MultiSelectElement unselectAll();
}
