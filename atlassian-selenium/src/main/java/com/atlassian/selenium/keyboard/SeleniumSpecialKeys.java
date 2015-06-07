package com.atlassian.selenium.keyboard;


import com.atlassian.webtest.ui.keys.SpecialKey;
import com.atlassian.webtest.ui.keys.SpecialKeys;

import java.awt.event.KeyEvent;

/**
 * Mappings of special keys into Selenium codes.
 *
 * @see com.atlassian.webtest.ui.keys.SpecialKeys
 * @see com.atlassian.webtest.ui.keys.SpecialKey
 * @author Dariusz Kordonski
 */
public enum SeleniumSpecialKeys
{


    ENTER(SpecialKeys.ENTER, 13), // correct JS code for ENTER event is 13, not 10 assumed by AWT key events!
    ESC(SpecialKeys.ESC, KeyEvent.VK_ESCAPE),
    DELETE(SpecialKeys.DELETE, KeyEvent.VK_DELETE),
    SPACE(SpecialKeys.SPACE, KeyEvent.VK_SPACE),
    BACKSPACE(SpecialKeys.BACKSPACE, KeyEvent.VK_BACK_SPACE),
    ARROW_UP(SpecialKeys.ARROW_UP, KeyEvent.VK_UP),
    ARROW_DOWN(SpecialKeys.ARROW_DOWN, KeyEvent.VK_DOWN),
    ARROW_LEFT(SpecialKeys.ARROW_LEFT, KeyEvent.VK_LEFT),
    ARROW_RIGHT(SpecialKeys.ARROW_RIGHT, KeyEvent.VK_RIGHT);


    private final SpecialKey specialKey;
    private final int keyCode;


    SeleniumSpecialKeys(SpecialKey key, int keyCode)
    {
        this.specialKey = key;
        this.keyCode = keyCode;
    }


    public static SeleniumSpecialKeys forKey(SpecialKey specialKey)
    {
        for (SeleniumSpecialKeys key : values())
        {
            if (key.specialKey == specialKey)
            {
                return key;
            }
        }
        throw new IllegalArgumentException("No Selenium special key for " + specialKey);
    }

    public int keyCode()
    {
        return keyCode;
    }
}
