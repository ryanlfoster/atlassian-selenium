package com.atlassian.webdriver.rule;

import org.junit.runner.Description;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

/**
 * For creating JUnit test descriptions
 *
 * @since 2.1
 */
public final class Descriptions
{

    private Descriptions()
    {
        throw new AssertionError("Don't instantiate me");
    }

    public static Description forSuite(Class<?> clazz)
    {
        try
        {
            return new JUnit4(clazz).getDescription();
        }
        catch (InitializationError initializationError)
        {
            throw new IllegalArgumentException("Provided class is not a valid test class", initializationError);
        }
    }

    public static Description forTest(Class<?> clazz, String methodName)
    {
        for (Description test : forSuite(clazz).getChildren())
        {
            if (test.getMethodName().equals(methodName))
            {
                return test;
            }
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " does not contain a valid test method " + methodName);
    }
}
