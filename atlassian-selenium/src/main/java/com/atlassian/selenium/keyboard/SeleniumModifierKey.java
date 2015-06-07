package com.atlassian.selenium.keyboard;


import com.atlassian.selenium.SeleniumClient;
import com.atlassian.webtest.ui.keys.ModifierKey;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

/**
 * Enumeration of Selenium modifier keys with mappings to the
 * {@link ModifierKey} enumeration.
 *
 * @since 4.3
 */
public enum SeleniumModifierKey
{

    SHIFT(ModifierKey.SHIFT)
            {
                @Override
                public void keyDown(SeleniumClient client)
                {
                    client.shiftKeyDown();
                }
                @Override
                public void keyUp(SeleniumClient client)
                {
                    client.shiftKeyUp();
                }
            },
    ALT(ModifierKey.ALT)
            {
                @Override
                public void keyDown(SeleniumClient client)
                {
                    client.altKeyDown();
                }
                @Override
                public void keyUp(SeleniumClient client)
                {
                    client.altKeyUp();
                }
            },
    CONTROL(ModifierKey.CONTROL)
            {
                @Override
                public void keyDown(SeleniumClient client)
                {
                    client.controlKeyDown();
                }
                @Override
                public void keyUp(SeleniumClient client)
                {
                    client.controlKeyUp();
                }
            },
    META(ModifierKey.META)
            {
                @Override
                public void keyDown(SeleniumClient client)
                {
                    client.metaKeyDown();
                }
                @Override
                public void keyUp(SeleniumClient client)
                {
                    client.metaKeyUp();
                }
            };

    private final ModifierKey modifierKey;

    private SeleniumModifierKey(ModifierKey modifierKey)
    {
        this.modifierKey = modifierKey;
    }

    public static SeleniumModifierKey forKey(ModifierKey modifierKey)
    {
        for (SeleniumModifierKey seleniumModifierKey : values())
        {
            if (seleniumModifierKey.modifierKey == modifierKey)
            {
                return seleniumModifierKey;
            }
        }
        throw new IllegalArgumentException("No Selenium modifier key for " + modifierKey);
    }

    public static Collection<SeleniumModifierKey> forKeys(Collection<ModifierKey> modifierKeys)
    {
        Set<SeleniumModifierKey> answer = EnumSet.noneOf(SeleniumModifierKey.class);
        for (ModifierKey modifierKey : modifierKeys)
        {
            answer.add(forKey(modifierKey));
        }
        return answer;
    }

    public void keyDown(SeleniumClient client)
    {
        throw new AbstractMethodError();
    }

    public void keyUp(SeleniumClient client)
    {
        throw new AbstractMethodError();
    }
}
