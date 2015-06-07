package com.atlassian.selenium.keyboard;

import com.atlassian.selenium.Browser;
import com.atlassian.webtest.ui.keys.KeyEventType;

import java.util.Set;

/**
 * TODO: Document this class / interface here
 *
 */
public class KeyEvent
{
    public KeyEvent()
    {
    }

    public KeyEvent(final KeyEventType eventType, final int code, final Set<Browser> browsers, final boolean shiftKeyDown, final boolean altKeyDown, final boolean ctrlKeyDown, final boolean metaKey, final boolean toKeyCode, final boolean toCharacterCode)
    {
        this.eventType = eventType;
        this.code = code;
        this.shiftKeyDown = shiftKeyDown;
        this.altKeyDown = altKeyDown;
        this.ctrlKeyDown = ctrlKeyDown;
        this.metaKey = metaKey;
        this.toKeyCode = toKeyCode;
        this.toCharacterCode = toCharacterCode;
        this.browsers = browsers;
    }

    private KeyEventType eventType;
    private int code;
    private boolean shiftKeyDown;
    private boolean altKeyDown;
    private boolean ctrlKeyDown;
    private boolean metaKey;
    private boolean toKeyCode;
    private boolean toCharacterCode;
    private Set<Browser> browsers;

    public boolean isToCharacterCode()
    {
        return toCharacterCode;
    }

    public boolean isToKeyCode()
    {
        return toKeyCode;

    }

    public Set<Browser> getBrowsers()
    {

        return browsers;
    }



    public boolean isMetaKey()
    {
        return metaKey;
    }


    public KeyEventType getEventType()
    {
        return eventType;
    }


    public int getCode()
    {
        return code;
    }


    public boolean isShiftKeyDown()
    {
        return shiftKeyDown;
    }


    public boolean isAltKeyDown()
    {
        return altKeyDown;
    }


    public boolean isCtrlKeyDown()
    {
        return ctrlKeyDown;
    }

}
