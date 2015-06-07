package com.atlassian.pageobjects.elements.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringConcatTest
{
    @Test
    public void testAsString()
    {
        assertEquals("", StringConcat.asString());
        assertEquals("x", StringConcat.asString("x"));
        assertEquals("xnullx", StringConcat.asString("x", null, "x"));
        assertEquals("1", StringConcat.asString(1));
        assertEquals("", StringConcat.asString((Object[])null));
    }
}
