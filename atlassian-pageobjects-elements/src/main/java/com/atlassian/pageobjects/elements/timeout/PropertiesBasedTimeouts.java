package com.atlassian.pageobjects.elements.timeout;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;

import org.apache.commons.io.IOUtils;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.atlassian.pageobjects.elements.util.StringConcat.asString;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * {@link com.atlassian.pageobjects.elements.timeout.Timeouts} implementation based on Java properties.
 *
 * <p>
 * This implementation accepts a {@link java.util.Properties} instance that contains properties in the following
 * form: 'com.atlassian.timeout.&lt;TIMEOUT_TYPE&gt;, where &lt;TIMEOUT_TYPE&gt; corresponds to a particular
 * field name of the {@link com.atlassian.pageobjects.elements.timeout.TimeoutType} enum.
 *
 * <p>
 * At the very least, the properties are supposed to contain the {@link com.atlassian.pageobjects.elements.timeout.TimeoutType#DEFAULT}
 * value (which corresponds to the property key 'com.atlassian.timeout.DEFAULT'). If it is not present, an exception
 * will be raised from the constructor. This value will be used in place of whatever other timeout type that
 * does have corresponding value within the properties.
 *
 */
public class PropertiesBasedTimeouts implements Timeouts
{
    private static final Logger log = LoggerFactory.getLogger(PropertiesBasedTimeouts.class);

    public static final String PROPERTY_PREFIX = asString("com.atlassian.timeout.");
    public static final String DEFAULT_PROPERTY_KEY = propKey(TimeoutType.DEFAULT);

    /**
     * Load instance of <tt>PropertiesBasedTimeouts</tt> based on properties from file on disk.
     *
     * @param path path of the properties file
     * @return new instance of this class
     */
    public static PropertiesBasedTimeouts fromFile(String path)
    {
        return new PropertiesBasedTimeouts(loadFromFile(path));
    }

    /**
     * Load instance of <tt>PropertiesBasedTimeouts</tt> based on properties from a class path resource.
     *
     * @param path path of the resource
     * @param loader class loader to use
     * @return new instance of this class
     */
    public static PropertiesBasedTimeouts fromClassPath(String path, ClassLoader loader)
    {
        InputStream is = loader.getResourceAsStream(path);
        try
        {
            return new PropertiesBasedTimeouts(checkNotNull(is));
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Load instance of <tt>PropertiesBasedTimeouts</tt> based on properties from a class path resource.
     * The class loader that loaded this class will be used.
     *
     * @param path path of the resource
     * @return new instance of this class
     */
    public static PropertiesBasedTimeouts fromClassPath(String path)
    {
        return fromClassPath(path, PropertiesBasedTimeouts.class.getClassLoader());
    }

    private static String propKey(TimeoutType timeoutType)
    {
        return PROPERTY_PREFIX + timeoutType.toString();
    }

    private final Map<String,String> properties;
    private final long defaultValue;

    public PropertiesBasedTimeouts(final Properties properties)
    {
        this.properties = Maps.fromProperties(checkNotNull(properties));
        defaultValue = validateAndGetDefault();
    }

    /**
     * Reads the properties from given <tt>reader</tt>. The reader <b>will not</b> be closed.
     *
     * @param reader reader to load properties from
     */
    public PropertiesBasedTimeouts(final InputStream reader)
    {
        this(loadFromReader(reader));
    }

    private static Properties loadFromReader(InputStream reader)
    {
        try
        {
            Properties answer = new Properties();
            answer.load(reader);
            return answer;
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Unable to read from path <" + reader + ">", e);
        }
    }

    private static Properties loadFromFile(String path)
    {
        InputStream reader = null;
        try
        {
            reader = new FileInputStream(path);
            Properties answer = new Properties();
            answer.load(reader);
            return answer;

        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Unable to read from path <" + path + ">", e);
        }
        finally
        {
            IOUtils.closeQuietly(reader);
        }
    }

    private long validateAndGetDefault()
    {
        checkArgument(properties.containsKey(DEFAULT_PROPERTY_KEY), "Must contain default timeout property with key <"
                + DEFAULT_PROPERTY_KEY + ">");
        return Long.parseLong(properties.get(DEFAULT_PROPERTY_KEY));
    }

    public long timeoutFor(final TimeoutType timeoutType)
    {
        String timeout = properties.get(propKey(timeoutType));
        if (timeout == null)
        {
            return defaultValue(timeoutType);
        }
        try
        {
            return Long.parseLong(timeout);
        }
        catch (NumberFormatException e)
        {
            log.warn(new StringDescription().appendText("Corrupted property value for key ").appendValue(propKey(timeoutType))
                    .appendText(": ").appendValue(timeout).appendText(". Returning default timeout value: ")
                    .appendValue(defaultValue).toString());
            return defaultValue;
        }
    }

    private long defaultValue(final TimeoutType timeoutType)
    {
        if (TimeoutType.EVALUATION_INTERVAL == timeoutType)
        {
            return Timeouts.DEFAULT_INTERVAL;
        }
        return defaultValue;
    }
}
