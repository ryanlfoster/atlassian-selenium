package com.atlassian.pageobjects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default implementation of {@link com.atlassian.pageobjects.ProductInstance}.
 *
 * @since 2.1
 */
public class DefaultProductInstance implements ProductInstance
{
    private final String baseUrl;
    private final String instanceId;
    private final int httpPort;
    private final String contextPath;


    public DefaultProductInstance(final String baseUrl, final String instanceId, final int httpPort, final String contextPath)
    {
        this.baseUrl = checkNotNull(baseUrl, "baseUrl");
        this.instanceId = instanceId;
        this.httpPort = httpPort;
        this.contextPath = contextPath;
    }

    public String getBaseUrl()
    {
        return baseUrl.toLowerCase();
    }

    public int getHttpPort()
    {
        return httpPort;
    }

    public String getContextPath()
    {
        return contextPath;
    }

    public String getInstanceId() {
        return instanceId;
    }
}
