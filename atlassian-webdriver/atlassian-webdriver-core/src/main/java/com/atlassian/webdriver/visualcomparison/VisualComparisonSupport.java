package com.atlassian.webdriver.visualcomparison;

import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.selenium.visualcomparison.v2.settings.PagePart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @since 2.3
 */
@ExperimentalApi
public final class VisualComparisonSupport
{
    @Inject
    private WebDriver webDriver;



    @Nonnull
    public PagePart asPagePart(@Nonnull By locator)
    {
        return new LocatablePagePart(checkNotNull(locator, "locator"));
    }

    private final class LocatablePagePart implements PagePart
    {
        private final By by;
        private WebElement element;

        private LocatablePagePart(By by)
        {
            this.by = by;
        }

        @Override
        public int getLeft()
        {
            return getElement().getLocation().getX();
        }

        @Override
        public int getTop()
        {
            return getElement().getLocation().getY();
        }

        @Override
        public int getRight()
        {
            return getLeft() + getElement().getSize().getWidth();
        }

        @Override
        public int getBottom()
        {
            return getTop() + getElement().getSize().getHeight();
        }

        private WebElement getElement()
        {
            if (element == null)
            {
                element = webDriver.findElement(by);
            }
            return element;
        }
    }
}
