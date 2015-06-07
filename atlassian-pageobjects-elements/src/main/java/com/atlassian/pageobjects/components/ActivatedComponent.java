package com.atlassian.pageobjects.components;

import com.atlassian.pageobjects.elements.PageElement;

/**
 * Represents a components that needs to be activated to interact with.
 */
public interface ActivatedComponent<T>
{
    /**
     * Gets the element that when activated opens the view of this components.
     *
     * @return Element
     */
    PageElement getTrigger();

    /**
     * The view Element, hidden or not present until activated.
     *
     * @return Element
     */
    PageElement getView();

    /**
     * Opens the view and waits until UI is ready for interaction.
     *
     * @return T
     */
    T open();

    /**
     * Whether the view is currently opened.
     *
     * @return true is components is open/activated, false otherwise.
     */
    boolean isOpen();
}
