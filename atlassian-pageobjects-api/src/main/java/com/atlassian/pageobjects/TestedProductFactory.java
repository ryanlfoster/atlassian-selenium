package com.atlassian.pageobjects;

import com.google.common.base.Supplier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Constructs a {@link TestedProduct}.  The {@link TestedProduct} instance is created by calling the constructor
 * with the following method signature:
 * <pre>
 *   TestedProduct(TesterFactory, ProductInstance)
 * </pre>
 */
public class TestedProductFactory
{
    /**
     * A factory for {@link Tester} instances
     * @param <T> The tester type
     */
    public static interface TesterFactory<T>
    {
        /**
         * @return The tester instance to be used through the tested product
         */
        T create();
    }

    /**
     * A factory that always returns the same {@link Tester}
     * @param <T> The tester type
     */
    public static class SingletonTesterFactory<T extends Tester> implements TesterFactory<T>
    {
        private final T tester;

        public SingletonTesterFactory(T tester)
        {
            this.tester = tester;
        }

        public T create()
        {
            return tester;
        }
    }

    public static <T extends TestedProduct<?>> Supplier<T> fromFactory(final Class<T> productClass)
    {
        return new Supplier<T>()
        {
            @Override
            public T get()
            {
                return create(productClass);
            }
        };
    }

    /**
     * Creates a tested product, allowing the instance to choose its own default {@link Tester} and instance id
     * @param testedProductClass The tested product class name
     * @return The created tested product
     */
    @SuppressWarnings("unchecked")
    public static TestedProduct<?> create(String testedProductClass)
    {
        Class<TestedProduct<?>> clazz;
        try
        {
            clazz = (Class<TestedProduct<?>>) TestedProductFactory.class.getClassLoader().loadClass(testedProductClass);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Cannot find tested product class: " + testedProductClass);
        }
        return create(clazz);
    }

    /**
     * Creates a tested product, allowing the instance to choose its own default {@link Tester} and instance id
     * @param testedProductClass The tested product class
     * @param <P> The tested product type
     * @return The created tested product
     */
    public static <P extends TestedProduct<?>> P create(Class<P> testedProductClass)
    {
        return create(testedProductClass, getDefaultInstanceId(testedProductClass), null);
    }

    /**
     * Creates a tested product using the passed tester factory and instance id.
     * @param testedProductClass The tested product class
     * @param instanceId The instance id
     * @param testerFactory The tester factory to use to pass to the tested product
     * @param <P> The tested product type
     * @return The created tested product
     */
    public static <P extends TestedProduct<?>> P create(Class<P> testedProductClass, String instanceId, TesterFactory<?> testerFactory)
    {
        final String contextPath, baseUrl;
        final int httpPort;

        final String ampsBaseUrl = System.getProperty("baseurl." + instanceId);
        final ProductInstance instance;
        if (ampsBaseUrl != null)    // running within an AMPS IntegrationTestMojo invocation - read sys props for env vars
        {
            httpPort = Integer.getInteger("http." + instanceId + ".port");
            contextPath = System.getProperty("context." + instanceId + ".path");
            baseUrl = ampsBaseUrl;
        }
        else
        {
            Defaults defaults = getDefaultsAnnotation(testedProductClass);
            httpPort = defaults.httpPort();
            contextPath = defaults.contextPath();
            baseUrl = "http://" + getLocalHostName() + ":" + httpPort + contextPath;
        }
        instance = new DefaultProductInstance(baseUrl, instanceId, httpPort, contextPath);
        return create(testedProductClass, instance, testerFactory);
    }

    private static String getDefaultInstanceId(Class<?> testedProductClass)
    {
        Defaults annotation = getDefaultsAnnotation(testedProductClass);
        return annotation.instanceId();
    }

    private static Defaults getDefaultsAnnotation(Class<?> testedProductClass)
    {
        Defaults annotation = testedProductClass.getAnnotation(Defaults.class);
        if (annotation == null)
        {
            throw new IllegalArgumentException("The tested product class '" + testedProductClass.getName() + "' is missing the @Defaults annotation");
        }
        return annotation;
    }

    /**
     * Creates a tested product using the passed tester factory and product instance.
     * @param testedProductClass The tested product class
     * @param productInstance The product instance
     * @param testerFactory The tester factory to use to pass to the tested product
     * @param <P> The tested product type
     * @return The created tested product
     */
    public static <P extends TestedProduct<?>> P create(Class<P> testedProductClass, ProductInstance productInstance, TesterFactory<?> testerFactory) {
        try
        {
            Constructor<P> c = testedProductClass.getConstructor(TesterFactory.class, ProductInstance.class);
            return c.newInstance(testerFactory, productInstance);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String getLocalHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
    }

}