package com.atlassian.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.component.Header;

/**
 * The home page for the administration section of this application
 */
public interface AdminHomePage<H extends Header> extends Page
{
    /**
     * @return The top header
     */
    H getHeader();
}
