package com.atlassian.webdriver.utils.element;

import com.atlassian.webdriver.utils.Check;
import org.apache.commons.lang.Validate;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Condition to wait for an element's presence.
 *
 */
abstract class ElementLocationCondition implements ExpectedCondition<Boolean>
{
    enum Locatable
    {
        LOCATED,
        NOTLOCATED
    }

    private final By by;
    private final SearchContext at;
    private final Locatable locatable;

    ElementLocationCondition(By by, Locatable locatable)
    {
        this(by, null, locatable);
    }

    ElementLocationCondition(By by, SearchContext at, Locatable locatable)
    {
        Validate.notNull(by, "by cannot be null.");

        this.by = by;
        this.at = at;
        this.locatable = locatable;
    }

    public Boolean apply(WebDriver webDriver)
    {
        if (locatable.equals(Locatable.LOCATED))
        {
            return Check.elementExists(by, at == null ? webDriver : at);
        }
        else
        {
            return !Check.elementExists(by, at == null ? webDriver : at);
        }
    }
}
