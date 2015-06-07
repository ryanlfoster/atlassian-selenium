package com.atlassian.selenium;

import com.atlassian.webtest.ui.keys.KeyEventType;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Encapsulates complex key events handling.
 *
 * @since v1.21
 */
public final class SeleniumKeyHandler extends AbstractSeleniumDriver
{
    private final String locator;
    private final Set<KeyEventType> keyEvents;
    private final boolean reset;
    private final SeleniumTagInspector tagInspector;

    public SeleniumKeyHandler(SeleniumClient client, String locator, Set<KeyEventType> keyEvents, boolean reset)
    {
        super(client);
        this.locator = checkNotNull(locator, "locator");
        this.keyEvents = checkNotNull(keyEvents, "keyEvents");
        this.reset = checkNotNull(reset, "reset");
        this.tagInspector = new SeleniumTagInspector(client);
    }

    public void typeWithFullKeyEvents(String text)
    {
        client.focus(locator);
        if (reset)
        {
            client.type(locator, "");
        }

        char[] chars = text.toCharArray();
        StringBuilder typedTo = new StringBuilder();
        if(!reset && tagInspector.isInput(locator))
        {
            typedTo.append(client.getValue(locator));
        }
        for (char aChar : chars)
        {
            if (Browser.IE.equals(client.getBrowser()))
            {
                typedTo.append(aChar);
                client.type(locator, typedTo.toString());
            }
            client.simulateKeyPressForCharacter(locator, aChar, keyEvents);
        }
    }

    
}
