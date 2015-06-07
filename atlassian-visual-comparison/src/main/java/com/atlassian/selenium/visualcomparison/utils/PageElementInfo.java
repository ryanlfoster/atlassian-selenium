package com.atlassian.selenium.visualcomparison.utils;

import com.atlassian.annotations.Internal;
import org.apache.commons.lang.StringEscapeUtils;

import java.awt.*;

@Internal
public class PageElementInfo
{
    public String htmlContent;
    public Dimension size;
    public Point position;

    public String getHtmlContent()
    {
        return htmlContent;
    }

    public String getEscapedHtmlString()
    {
        return StringEscapeUtils.escapeHtml(htmlContent);
    }

    public Dimension getSize()
    {
        return size;
    }

    public int getOffsetLeft()
    {
        return (null == position) ? -1 : position.x;
    }

    public int getOffsetTop()
    {
        return (null == position) ? -1 : position.y;
    }

    public int getElementWidth()
    {
        return (null == size) ? -1 : size.width;
    }

    public int getElementHeight()
    {
        return (null == size) ? -1 : size.height;
    }
}
