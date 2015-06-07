package com.atlassian.pageobjects.elements;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Maps element interfaces to web driver implementations.
 *
 */
public class WebDriverElementMappings
{
    private static final Map<Class<? extends PageElement>, Class<? extends PageElement>> MAPPINGS = ImmutableMap.<Class<? extends PageElement>, Class<? extends PageElement>>builder()
            .put(PageElement.class, WebDriverElement.class)
            .put(SelectElement.class, WebDriverSelectElement.class)
            .put(MultiSelectElement.class, WebDriverMultiSelectElement.class)
            .put(CheckboxElement.class, WebDriverCheckboxElement.class)
            .build();


    @SuppressWarnings({"unchecked"})
    public static <T extends PageElement> Class<T> findMapping(Class<T> input)
    {
        if (!input.isInterface())
        {
            // concrete class - just return it
            return input;
        }
        Class<T> answer = (Class<T>) MAPPINGS.get(input);
        if (answer == null)
        {
            answer = (Class<T>) WebDriverElement.class;
        }
        return answer;
    }
}
