package com.atlassian.pageobjects.components.aui;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.pageobjects.components.ActivatedComponent;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.query.Poller;
import org.openqa.selenium.By;

import javax.inject.Inject;

/**
 * Represents an inline dialog created via AUI.
 *
 * This is an example of a reusable components.
 */
public class AuiInlineDialog implements ActivatedComponent<AuiInlineDialog>
{
    @Inject
    protected PageBinder pageBinder;

    @Inject
    protected PageElementFinder elementFinder;

    private final By triggerLocator;
    private final String identifier;
    private PageElement triggerElement;
    private PageElement viewElement;

    public AuiInlineDialog(By triggerLocator, String identifier)
    {
        this.triggerLocator = triggerLocator;
        this.identifier = identifier;
    }

    @Init
    public void initialize()
    {
        triggerElement = elementFinder.find(triggerLocator);
        viewElement = elementFinder.find(By.id("inline-dialog-" + identifier));
    }

    public PageElement getTrigger()
    {
        return triggerElement;
    }

    public PageElement getView()
    {
        return viewElement;
    }

    public AuiInlineDialog open()
    {
        triggerElement.click();
        Poller.waitUntilTrue(viewElement.timed().isVisible());
        return this;
    }

    public boolean isOpen()
    {
        return viewElement.isPresent() && viewElement.isVisible();
    }
}
