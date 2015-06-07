package com.atlassian.webdriver.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Hamcrest matchers for files.
 *
 * @since 2.1
 */
public final class FileMatchers
{

    private FileMatchers()
    {
        throw new AssertionError("Don't instantiate me");
    }

    public static Matcher<File> hasAbsolutePath(final String absolutePath)
    {
        checkNotNull(absolutePath, "absolutePath");
        return new TypeSafeMatcher<File>()
        {
            @Override
            public boolean matchesSafely(File item)
            {
                return item != null && absolutePath.equals(item.getAbsolutePath());
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("A File with absolute path ").appendValue(absolutePath);
            }
        };
    }

    public static Matcher<File> exists()
    {
        return new TypeSafeMatcher<File>()
        {
            @Override
            public boolean matchesSafely(File item)
            {
                return item.exists();
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("A file or directory that exists");
            }
        };
    }

    public static Matcher<File> isDirectory()
    {
        return new TypeSafeMatcher<File>()
        {
            @Override
            public boolean matchesSafely(File item)
            {
                return item.isDirectory();
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("An existing directory");
            }
        };
    }

    public static Matcher<File> isFile()
    {
        return new TypeSafeMatcher<File>()
        {
            @Override
            public boolean matchesSafely(File item)
            {
                return item.isFile();
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("An existing file");
            }
        };
    }
}
