package com.atlassian.webdriver.utils.element;

import com.atlassian.webdriver.utils.Check;
import org.apache.commons.lang.Validate;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Condition to wait for an element's visibility.
 *
 * @since 2.0
 */
abstract class ElementVisibilityCondition implements ExpectedCondition<Boolean>
{

    enum Visibility
    {
        VISIBLE,
        NOTVISIBLE
    }

    private final By by;
    private final SearchContext at;
    private final Visibility visibility;

    ElementVisibilityCondition(By by, Visibility visibility)
    {
        this(by, null, visibility);
    }

    ElementVisibilityCondition(By by, SearchContext el, Visibility visibility)
    {
        Validate.notNull(by, "by cannot be null.");

        this.by = by;
        this.at = el;
        this.visibility = visibility;
    }

    final public Boolean apply(WebDriver webDriver)
    {
        if (visibility.equals(Visibility.VISIBLE))
        {
            return Check.elementIsVisible(by, at == null ? webDriver : at);
        }
        else
        {
            return !Check.elementIsVisible(by, at == null ? webDriver : at);
        }
    }

    @Override
    public String toString() {
        return String.format("Condition: [state=%s,what=%s,where=%s]", visibility, by, at);
    }
}
