package com.atlassian.pageobjects.elements.test.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.PageElement;

/**
 * @since 2.2
 */
public class BigPageWithButtons implements Page
{
    @ElementBy(id = "button-right")
    protected PageElement rightButton;

    @ElementBy(id = "button-bottom")
    protected PageElement bottomButton;

    @ElementBy(id = "button-right-clicked")
    protected PageElement rightButtonClickedMessage;

    @ElementBy(id = "button-bottom-clicked")
    protected PageElement bottomButtonClickedMessage;

    public String getUrl()
    {
        return "/html/big-page-with-buttons.html";
    }

    public PageElement getRightButton()
    {
        return rightButton;
    }

    public PageElement getRightButtonClickedMessage()
    {
        return rightButtonClickedMessage;
    }

    public PageElement getBottomButton()
    {
        return bottomButton;
    }

    public PageElement getBottomButtonClickedMessage()
    {
        return bottomButtonClickedMessage;
    }
}
