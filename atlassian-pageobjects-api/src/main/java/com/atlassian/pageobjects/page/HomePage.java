package com.atlassian.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.component.Header;

/**
 * The home page of the application
 */
public interface HomePage<H extends Header> extends Page
{
    /**
     * @return the header
     */
    H getHeader();
}
