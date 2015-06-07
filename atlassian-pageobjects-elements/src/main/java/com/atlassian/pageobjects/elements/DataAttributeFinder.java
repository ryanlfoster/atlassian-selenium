package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.atlassian.pageobjects.elements.query.TimedQuery;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Queries {@link com.atlassian.pageobjects.elements.PageElement page elements} for data attributes.
 *
 * @since 2.1
 */
public final class DataAttributeFinder
{
    private static final String DATA_PREFIX = "data-";

    public static DataAttributeFinder query(PageElement element)
    {
        return new DataAttributeFinder(element);
    }

    private final PageElement pageElement;
    private final Timed timed;

    private DataAttributeFinder(PageElement pageElement)
    {
        this.pageElement = checkNotNull(pageElement);
        this.timed = new Timed();
    }

    public String getDataAttribute(String dataAttributeName)
    {
        return pageElement.getAttribute(DATA_PREFIX + dataAttributeName);
    }

    public boolean hasDataAttribute(String dataAttributeName, String value)
    {
        return pageElement.hasAttribute(DATA_PREFIX + dataAttributeName, value);
    }

    public Timed timed()
    {
        return timed;
    }



    public class Timed
    {
        private final TimedElement timedElement;

        private Timed()
        {
            this.timedElement = pageElement.timed();
        }

        public TimedQuery<String> getDataAttribute(String dataAttrName)
        {
            return timedElement.getAttribute(DATA_PREFIX + dataAttrName);
        }

        public TimedCondition hasDataAttribute(String dataAttrName, String dataAttrValue)
        {
            return timedElement.hasAttribute(DATA_PREFIX + dataAttrName, dataAttrValue);
        }
    }

}
