package com.atlassian.pageobjects.binder;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Adds a common SLF4J logger to the injection context.
 *
 * @since 2.1
 */
public class LoggerModule extends AbstractModule
{
    private final Logger logger;

    public LoggerModule(@Nonnull Logger logger)
    {
        this.logger = checkNotNull(logger, "logger");
    }

    /**
     * @param loggerClass class to get logger for
     * @since 2.2
     */
    public LoggerModule(@Nonnull Class<?> loggerClass)
    {
        this(LoggerFactory.getLogger(checkNotNull(loggerClass, "loggerClass")));
    }

    @Override
    protected void configure()
    {
        bind(Logger.class).toInstance(logger);
    }
}
