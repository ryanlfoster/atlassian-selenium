package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.elements.test.pageobjects.PageElementsTestedProduct;
import com.atlassian.webdriver.testing.annotation.TestedProductClass;
import com.atlassian.webdriver.testing.rule.DefaultProductContextRules;
import com.atlassian.webdriver.testing.simpleserver.SimpleServerRunner;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

@TestedProductClass(PageElementsTestedProduct.class)
@RunWith(SimpleServerRunner.class)
public abstract class AbstractPageElementBrowserTest
{
    @Inject protected static PageElementsTestedProduct product;
    @Inject protected static WebDriver driver;

    @Inject @ClassRule public static DefaultProductContextRules.ForClass classRules;
    @Inject @Rule public DefaultProductContextRules.ForMethod methodRules;
}
