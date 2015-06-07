package com.atlassian.selenium.visualcomparison.utils;

import com.atlassian.annotations.Internal;

import java.util.ArrayList;
import java.util.List;

@Internal
public class PageDifference
{
    private final BoundingBox box;
    private PageDifferenceImages images;
    private final List<PageElementInfo> pageElements;

    public PageDifference(BoundingBox box)
    {
        this.box = box;
        this.pageElements = new ArrayList<PageElementInfo>();
    }

    public BoundingBox getBoundingBox()
    {
        return this.box;
    }

    public void setImages(PageDifferenceImages images)
    {
        this.images = images;
    }

    public PageDifferenceImages getImages()
    {
        return images;
    }

    public void addPageElement(PageElementInfo el)
    {
        getPageElements().add(el);
    }

    public List<PageElementInfo> getPageElements()
    {
        return this.pageElements;
    }
}
