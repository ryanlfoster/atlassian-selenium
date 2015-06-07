package com.atlassian.pageobjects.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;


/**
 * Helper class for building a query string
 */
public class QueryStringBuilder
{

    private final Map<String, String> params = new HashMap<String, String>();


    public QueryStringBuilder(String... params)
    {
        Validate.isTrue(params.length % 2 == 0, "Must be an even number of parameters");

        for (int i = 0; i < params.length; i += 2)
        {
            this.params.put(params[i], params[i + 1]);
        }
    }

    public QueryStringBuilder add(String key, String value)
    {
        params.put(key, value);
        return this;
    }

    public int size()
    {
        return params.size();
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();

        for (String key : params.keySet())
        {
            if (result.length() > 0)
            {
                result.append("&");
            }

            result.append(key).append("=").append(params.get(key));
        }

        return result.toString();
    }

}
