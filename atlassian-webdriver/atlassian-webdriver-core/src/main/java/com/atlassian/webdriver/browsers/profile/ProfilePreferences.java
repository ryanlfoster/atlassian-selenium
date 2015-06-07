package com.atlassian.webdriver.browsers.profile;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.google.common.io.LineReader;
import org.openqa.selenium.WebDriverException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for reading a preferences file for a profile and extracting the
 * properties.
 *
 * The format for preferences is
 * value=key
 *
 * @since 2.1
 */
public class ProfilePreferences
{
    private final Pattern PREFERENCE_PATTERN = Pattern.compile("^([^=]+)=(.*)$");

    private final Map<String, Object> allPreferences = Maps.newHashMap();

    public ProfilePreferences(File preferencesFile)
    {
        FileReader reader = null;
        try {
          reader = new FileReader(preferencesFile);
          readPreferences(reader);
        } catch (IOException e) {
          throw new WebDriverException(e);
        } finally {
          Closeables.closeQuietly(reader);
        }
    }

    public Map<String, Object> getPreferences()
    {
        return ImmutableMap.copyOf(allPreferences);
    }

    private void readPreferences(FileReader reader) throws IOException
    {
        LineReader allLines = new LineReader(reader);
        String line = allLines.readLine();
        while (line != null) {
          Matcher matcher = PREFERENCE_PATTERN.matcher(line);
          if (matcher.matches()) {
            allPreferences.put(matcher.group(1), preferenceAsValue(matcher.group(2)));
          }
          line = allLines.readLine();
        }
    }

    private Object preferenceAsValue(String value)
    {
        if ("false".equals(value) || "true".equals(value)) {
            return Boolean.parseBoolean(value);
        }
        else if (value.matches("^[0-9]+$"))
        {
          return Integer.parseInt(value);
        }
        else
        {
            return value;
        }
    }


}
