package com.atlassian.selenium;

import com.thoughtworks.selenium.Selenium;

/**
 * A set of conditions used in waitTill... methods in the @class{SeleniumAssertions}
 *
 * @since v3.12
 */
public class Conditions {

    public static Condition isVisible(final String element)
    {
        return new Condition() {
            public boolean executeTest(Selenium selenium)
            {
                return (selenium.isElementPresent(element) && selenium.isVisible(element));
            }

            public String errorMessage()
            {
                return "Element [" + element + "] is not present or not visible";
            }
        };
    }

    public static Condition isNotVisible(final String element)
    {
        return new Condition() {
            public boolean executeTest(Selenium selenium)
            {
                return (!selenium.isVisible(element));
            }

            public String errorMessage()
            {
                return "Element [" + element + "] is visible";
            }
        };
    }

    public static Condition isPresent(final String element)
    {
        return new Condition() {
            public boolean executeTest(Selenium selenium)
            {
                return (selenium.isElementPresent(element));
            }

            public String errorMessage()
            {
                return "Element [" + element + "] is not present";
            }
        };
    }

    public static Condition isNotPresent(final String element)
    {
        return new Condition() {
            public boolean executeTest(Selenium selenium)
            {
                return (!selenium.isElementPresent(element));
            }
            
            public String errorMessage()
            {
                return "Element [" + element + "] is present";
            }
        };
    }

    public static Condition isTextPresent(final String text)
    {
        return new Condition() {
            public boolean executeTest(Selenium selenium)
            {
                return (selenium.isTextPresent(text));
            }

            public String errorMessage()
            {
                return "Text [" + text + "] is not present";
            }
        };
    }

    public static Condition isTextNotPresent(final String text)
    {
        return new Condition() {
            public boolean executeTest(Selenium selenium)
            {
                return (!selenium.isTextPresent(text));
            }

            public String errorMessage()
            {
                return "Element [" + text + "] is present";
            }
        };
    }

}