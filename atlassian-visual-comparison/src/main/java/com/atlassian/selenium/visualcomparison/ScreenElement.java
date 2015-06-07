package com.atlassian.selenium.visualcomparison;

import com.atlassian.annotations.PublicApi;

import javax.annotation.Nonnull;

/**
 * Represents an HTML element with access to the outer HTML that makes up for that element.
 */
@PublicApi
public interface ScreenElement
{
    /**
     * @return the outer HTML of the element, as string
     */
    @Nonnull
    String getHtml();
}
