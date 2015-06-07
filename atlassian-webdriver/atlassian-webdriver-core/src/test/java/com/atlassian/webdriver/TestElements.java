package com.atlassian.webdriver;

import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class TestElements
{
    @Test
    public void shouldReturnEmptyCssClassesSetForNullClassValue()
    {
        assertTrue(Elements.getCssClasses(null).isEmpty());
    }

    @Test
    public void shouldReturnEmptyCssClassesSetForEmptyClassValue()
    {
        assertTrue(Elements.getCssClasses("").isEmpty());
    }

    @Test
    public void shouldReturnEmptyCssClassesSetForBlankClassValue()
    {
        assertTrue(Elements.getCssClasses("   \t").isEmpty());
    }

    @Test
    public void shouldReturnSingletonSetForSingleClassValue()
    {
        assertThat(Elements.getCssClasses("\tsingle-Class  "), contains("single-class"));
    }

    @Test
    public void shouldReturnSetContainingAllExtractedClasses()
    {
        assertThat(Elements.getCssClasses("\tone-Class\tONE-class  another-class\t  THIRD-class  "),
                contains("one-class", "another-class", "third-class"));
    }

    @Test(expected = NullPointerException.class)
    public void hasClassShouldNoAcceptNullClassName()
    {
        assertFalse(Elements.hasCssClass(null, " some-class another-class"));
    }

    @Test
    public void hasClassShouldBeNullTolerant()
    {
        assertFalse(Elements.hasCssClass("some-class", null));
    }

    @Test
    public void hasClassShouldMatchCaseInsensitive()
    {
        assertTrue(Elements.hasCssClass("third-CLASS", "\tone-Class\tONE-class  another-class\t  THIRD-class  "));
    }
}
