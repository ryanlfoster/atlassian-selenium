package com.atlassian.webtest.ui.keys;

import com.atlassian.selenium.SeleniumClient;
import com.atlassian.selenium.keyboard.SeleniumTypeWriter;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.atlassian.webtest.ui.keys.KeyEventType.KEYDOWN;
import static com.atlassian.webtest.ui.keys.KeyEventType.KEYPRESS;
import static com.atlassian.webtest.ui.keys.KeyEventType.KEYUP;
import static com.atlassian.webtest.ui.keys.Sequences.chars;
import static com.atlassian.webtest.ui.keys.Sequences.charsBuilder;
import static com.atlassian.webtest.ui.keys.Sequences.keysBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyChar;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.atlassian.selenium.keyboard.SeleniumTypeWriter}.
 *
 * @since v1.21
 */
public class TestSeleniumTypeWriter
{
    private static final String TEST_LOCATOR = "id=test";
    private static final String TEST_LOCATOR_WITH_QUOTES = "css=input[name='inquotes']";
    private static final int CORRECT_ENTER = 13;

    private KeyPressVerifyingMock keyPressMock;
    private SeleniumTypeWriter writer;


    @Before
    public void setUpMocks()
    {
        keyPressMock = new KeyPressVerifyingMock();
        writer = new SeleniumTypeWriter(keyPressMock.mockClient(), TEST_LOCATOR, TypeMode.TYPE);
    }

    @Test
    public void shouldInsertWithEvent()
    {
        writer.type(charsBuilder("blah").typeMode(TypeMode.INSERT_WITH_EVENT).build());
        keyPressMock.verifyTyped(TEST_LOCATOR, "bla");
        keyPressMock.verifyChars(TEST_LOCATOR, "h", KeyEventType.ALL);
        keyPressMock.verifyNoMore();
    }


    @Test
    public void fullTypeShouldUseAllKeyEventsByDefault()
    {
        writer.type(chars("blah"));
        keyPressMock.verifyChars(TEST_LOCATOR, "blah", KeyEventType.ALL);
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldSupportCustomKeySequenceImplementations()
    {
        writer.type(new CustomKeySequence(chars("test")));
        keyPressMock.verifyChars(TEST_LOCATOR, "test", KeyEventType.ALL);
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldHandleNonEditableElements()
    {
        when(keyPressMock.mockClient().getValue(TEST_LOCATOR)).thenThrow(new IllegalStateException("I'm not editable"));
        when(keyPressMock.mockClient().isElementPresent(TEST_LOCATOR)).thenReturn(true);
        when(keyPressMock.mockClient().getEval("this.getTagName('" + TEST_LOCATOR + "')")).thenReturn("body");
        writer.type(chars("test"));
        keyPressMock.verifyChars(TEST_LOCATOR, "test", EnumSet.allOf(KeyEventType.class));
        keyPressMock.verifyNoMore();
        verify(keyPressMock.mockClient()).getEval("this.getTagName('" + TEST_LOCATOR + "')");
    }

    @Test
    public void shouldTypeSpecifiedCharEventsInFullTypeMode()
    {
        writer.type(charsBuilder("blah").keyEvents(KEYDOWN, KEYPRESS).build());
        keyPressMock.verifyChars(TEST_LOCATOR, "blah", EnumSet.of(KEYDOWN, KEYPRESS));
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeSpecifiedKeyEventsInFullTypeMode()
    {
        writer.type(SpecialKeys.ARROW_DOWN.withEvents(KEYDOWN, KEYPRESS));
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYPRESS), KeyEvent.VK_DOWN);
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeSpecifiedKeyEventsInMixedSequenceAndFullTypeMode()
    {
        KeySequence input = new KeySequenceBuilder("abc").append(SpecialKeys.ENTER).append("def")
                .keyEvents(KEYDOWN, KEYUP).build();
        writer.type(input);
        keyPressMock.verifyChars(TEST_LOCATOR, "abc", EnumSet.of(KEYDOWN, KEYUP));
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYUP), CORRECT_ENTER);
        keyPressMock.verifyChars(TEST_LOCATOR, "def", EnumSet.of(KEYDOWN, KEYUP));
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeCharsInInsertTypeMode()
    {
        writer.type(charsBuilder("blah").keyEvents(KEYDOWN, KEYPRESS).typeMode(TypeMode.INSERT)
                .build());
        keyPressMock.verifyTyped(TEST_LOCATOR, "blah");
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeSpecifiedKeyEventsInInsertTypeMode()
    {
        writer.type(keysBuilder(SpecialKeys.ARROW_DOWN).keyEvents(KEYDOWN, KEYPRESS)
                .typeMode(TypeMode.INSERT).build());
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYPRESS), KeyEvent.VK_DOWN);
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeSpecifiedKeyEventsInMixedSequenceAndInsertTypeMode()
    {
        KeySequence input = new KeySequenceBuilder("abc").append(SpecialKeys.ENTER).append("def")
                .keyEvents(KEYDOWN, KEYUP).typeMode(TypeMode.INSERT).build();
        writer.type(input);
        keyPressMock.verifyTyped(TEST_LOCATOR, "abc");
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYUP), CORRECT_ENTER);
        keyPressMock.verifyTyped(TEST_LOCATOR, "def");
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeCharsInInsertWithEventTypeMode()
    {
        writer.type(charsBuilder("blah").keyEvents(KEYDOWN, KEYPRESS).typeMode(TypeMode.INSERT_WITH_EVENT)
                .build());
        keyPressMock.verifyTyped(TEST_LOCATOR, "bla");
        keyPressMock.verifyChars(TEST_LOCATOR, "h", EnumSet.of(KEYDOWN, KEYPRESS));
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeSpecifiedKeyEventsInInsertWithEventTypeMode()
    {
        writer.type(keysBuilder(SpecialKeys.ARROW_DOWN).keyEvents(KEYDOWN, KEYPRESS).typeMode(TypeMode.INSERT_WITH_EVENT)
                .build());
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYPRESS), KeyEvent.VK_DOWN);
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeSpecifiedKeyEventsInMixedSequenceAndInsertWithEventTypeMode()
    {
        KeySequence input = new KeySequenceBuilder("abc").append(SpecialKeys.ENTER).append("def")
                .keyEvents(KEYDOWN, KEYUP).typeMode(TypeMode.INSERT_WITH_EVENT).build();
        writer.type(input);
        keyPressMock.verifyTyped(TEST_LOCATOR, "abc");
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYUP), CORRECT_ENTER);
        keyPressMock.verifyTyped(TEST_LOCATOR, "de");
        keyPressMock.verifyChars(TEST_LOCATOR, "f", EnumSet.of(KEYDOWN, KEYUP));
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldUserCorrectKeyForEnter()
    {
        writer.type(SpecialKeys.ENTER);
        keyPressMock.verifyKeys(TEST_LOCATOR, KeyEventType.ALL, CORRECT_ENTER);
        keyPressMock.verifyNoMore();
    }


    @Test
    public void shouldTypeSpecifiedKeyEventsInMixedSequenceAndInsertWithEventTypeModeWithKeyAtTheEndHAHAHA()
    {
        KeySequence input = new KeySequenceBuilder("abc").append(SpecialKeys.ENTER).append("def")
                .append(SpecialKeys.ESC).keyEvents(KEYDOWN, KEYPRESS).typeMode(TypeMode.INSERT_WITH_EVENT)
                .build();
        writer.type(input);
        keyPressMock.verifyTyped(TEST_LOCATOR, "abc");
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYPRESS), CORRECT_ENTER);
        keyPressMock.verifyTyped(TEST_LOCATOR, "def");
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYPRESS), KeyEvent.VK_ESCAPE);
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldTypeAllSpecialKeys()
    {
        KeySequence input = new KeySequenceBuilder().append(allSpecialKeys()).keyEvents(KEYDOWN, KEYPRESS)
                .typeMode(TypeMode.INSERT_WITH_EVENT).build();
        writer.type(input);
        keyPressMock.verifyKeys(TEST_LOCATOR, EnumSet.of(KEYDOWN, KEYPRESS),
                CORRECT_ENTER,
                KeyEvent.VK_ESCAPE,
                KeyEvent.VK_BACK_SPACE,
                KeyEvent.VK_DELETE,
                KeyEvent.VK_SPACE,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_UP,
                KeyEvent.VK_DOWN);
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldHandleZeroLengthSequenceInInsertMode()
    {
        writer.type(charsBuilder("").typeMode(TypeMode.INSERT).build());
        keyPressMock.verifyTyped(TEST_LOCATOR, "");
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldHandleZeroLengthSequenceInInsertWithEventMode()
    {
        writer.type(charsBuilder("").typeMode(TypeMode.INSERT_WITH_EVENT).build());
        keyPressMock.verifyTyped(TEST_LOCATOR, "");
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldHandleZeroLengthSequenceInFullEventTypeMode()
    {
        writer.type(charsBuilder("").typeMode(TypeMode.TYPE).build());
        keyPressMock.verifyNoMore();
    }

    @Test
    public void shouldPreserveExistingText()
    {
        when(keyPressMock.mockClient().getValue(TEST_LOCATOR)).thenReturn("existing");
        when(keyPressMock.mockClient().isElementPresent(TEST_LOCATOR)).thenReturn(true);
        when(keyPressMock.mockClient().getEval("this.getTagName('" + TEST_LOCATOR + "')")).thenReturn("input");
        writer.type(charsBuilder("").typeMode(TypeMode.INSERT).build());
        keyPressMock.verifyTyped(TEST_LOCATOR, "existing");
        keyPressMock.verifyNoMore();

    }

    @Test
    public void shouldNotPreserveExistingTextOnClear()
    {
        when(keyPressMock.mockClient().getValue(TEST_LOCATOR)).thenReturn("existing");
        when(keyPressMock.mockClient().isElementPresent(TEST_LOCATOR)).thenReturn(true);
        when(keyPressMock.mockClient().getEval("this.getTagName('" + TEST_LOCATOR + "')")).thenReturn("input");
        writer.clear();
        keyPressMock.verifyTyped(TEST_LOCATOR, "");
        keyPressMock.verifyNoMore();

    }

    private static Collection<Key> allSpecialKeys()
    {
        return Collections2.transform(EnumSet.allOf(SpecialKeys.class), new Function<SpecialKeys, Key>()
        {
            public Key apply(SpecialKeys input)
            {
                return input;
            }
        });
    }

    private static class CustomKeySequence implements KeySequence
    {
        private final KeySequence real;

        public CustomKeySequence(final KeySequence real)
        {
            this.real = real;
        }

        public List<Key> keys()
        {
            return real.keys();
        }

        public Set<ModifierKey> withPressed()
        {
            return real.withPressed();
        }
    }

    private static class KeyPressVerifyingMock
    {
        private final SeleniumClient mockClient;
        private final List<KeyInteraction> keyPresses = new LinkedList<KeyInteraction>();
        private Iterator<KeyInteraction> current;

        public KeyPressVerifyingMock()
        {
            this.mockClient = mock(SeleniumClient.class);
            doAnswer(new Answer<Void>()
            {
                public Void answer(InvocationOnMock invocation) throws Throwable
                {
                    checkNoVerification();
                    keyPresses.add(new KeyInteraction(targetArg(invocation), charArg(invocation), eventsArg(invocation)));
                    return null;
                }
            }).when(mockClient).simulateKeyPressForCharacter(anyString(), anyChar(), anyCollectionOf(KeyEventType.class));
            doAnswer(new Answer<Void>()
            {
                public Void answer(InvocationOnMock invocation) throws Throwable
                {
                    checkNoVerification();
                    keyPresses.add(new KeyInteraction(targetArg(invocation), keyArg(invocation), eventsArg(invocation)));
                    return null;
                }
            }).when(mockClient).simulateKeyPressForSpecialKey(anyString(), anyInt(), anyCollectionOf(KeyEventType.class));
            doAnswer(new Answer<Void>()
            {
                public Void answer(InvocationOnMock invocation) throws Throwable
                {
                    checkNoVerification();
                    keyPresses.add(new KeyInteraction(targetArg(invocation), typedArg(invocation)));
                    return null;
                }
            }).when(mockClient).type(anyString(), anyString());
        }

        private void checkNoVerification()
        {
            if (isVerifying())
            {
                throw new IllegalStateException("Already verifying");
            }
        }

        static String targetArg(InvocationOnMock invocation)
        {
            return (String) invocation.getArguments()[0];
        }

        static char charArg(InvocationOnMock invocation)
        {
            return (Character) invocation.getArguments()[1];
        }

        static int keyArg(InvocationOnMock invocation)
        {
            return (Integer) invocation.getArguments()[1];
        }

        static String typedArg(InvocationOnMock invocation)
        {
            return (String) invocation.getArguments()[1];
        }

        @SuppressWarnings ({ "unchecked" })
        static Collection<KeyEventType> eventsArg(InvocationOnMock invocation)
        {
            return (Collection<KeyEventType>) invocation.getArguments()[2];
        }

        SeleniumClient mockClient()
        {
            return mockClient;
        }

        private void startVerification()
        {
            if (current == null)
            {
                current = keyPresses.iterator();
            }
        }

        boolean isVerifying()
        {
            return current != null;
        }

        void verifyChars(String target, String chars, Set<KeyEventType> events)
        {
            startVerification();
            for (char character : chars.toCharArray())
            {
                nextInteraction().verify(target, character, events);
            }
        }

        void verifyKeys(String target, Set<KeyEventType> events, int... keys)
        {
            startVerification();
            for (int key : keys)
            {
                nextInteraction().verify(target, key, events);
            }
        }

        void verifyTyped(String target, String typed)
        {
            startVerification();
            nextInteraction().verify(target, typed);
        }

        KeyInteraction nextInteraction()
        {
            if (current.hasNext())
            {
                return current.next();
            }
            else
            {
                throw new IllegalStateException("No more recorded key presses");
            }
        }

        void verifyNoMore()
        {
            startVerification();
            if (current.hasNext())
            {
                throw new AssertionError("More events available:" + Iterators.toString(current));
            }
        }

    }

    private static class KeyInteraction
    {
        private final String target;
        private final String typed;
        private final char character;
        private final int key;
        private final EnumSet<KeyEventType> keyEvents;

        KeyInteraction(String target, char character, Collection<KeyEventType> events)
        {
            this.target = target;
            this.typed = null;
            this.character = character;
            this.key = -1;
            this.keyEvents = EnumSet.copyOf(events);
        }

        KeyInteraction(String target, int key, Collection<KeyEventType> events)
        {
            Preconditions.checkArgument(key > 0);
            this.target = target;
            this.typed = null;
            this.character = 0;
            this.key = key;
            this.keyEvents = EnumSet.copyOf(events);
        }

        KeyInteraction(String target, String typed)
        {
            Preconditions.checkNotNull(typed);
            this.target = target;
            this.typed = typed;
            this.character = 0;
            this.key = -1;
            this.keyEvents = null;
        }

        boolean isTyped()
        {
            return !isKey() && typed != null;
        }

        boolean isChar()
        {
            return !isKey() && !isTyped();
        }

        boolean isKey()
        {
            return key > 0;
        }

        void verify(String target, String typed)
        {
            assertTrue("Not a type interaction:" + this, isTyped());
            assertEquals(target, this.target);
            assertEquals(typed, this.typed);
        }

        void verify(String target, char character, Set<KeyEventType> events)
        {
            assertTrue("Not a char event: " + this, isChar());
            assertEquals(target, this.target);
            assertEquals(character, this.character);
            assertEquals(events, this.keyEvents);
        }

        void verify(String target, int key, Set<KeyEventType> events)
        {
            assertTrue("Not a key event: " + this, isKey());
            assertEquals(target, this.target);
            assertEquals(key, this.key);
            assertEquals(events, this.keyEvents);
        }

        @Override
        public String toString()
        {
            if (isChar())
            {
                return "Character Key Press [target=" + target + ",character=" + character + ",events=" + keyEvents + "]";
            }
            if (isTyped())
            {
                return "Type Interaction [target=" + target + ",typed=" + typed + "]";
            }
            if (isKey())
            {
                return "Special Key Press [target=" + target + ",key=" + key + ",events=" + keyEvents + "]";
            }
            throw new AssertionError("???");
        }
    }
}
