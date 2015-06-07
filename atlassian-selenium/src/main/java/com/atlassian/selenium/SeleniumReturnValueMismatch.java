package com.atlassian.selenium;

import com.thoughtworks.selenium.SeleniumException;

public class SeleniumReturnValueMismatch extends SeleniumException
{ 
    SeleniumReturnValueMismatch(Browser b1, Object v1, Browser b2, Object v2)
    {
        super("Mismatch between browser return values. Browser " + b1 + " returned [" + v1 +
                    "] which did not match " + b2 + " [" + v2 + "]");
    }
}
