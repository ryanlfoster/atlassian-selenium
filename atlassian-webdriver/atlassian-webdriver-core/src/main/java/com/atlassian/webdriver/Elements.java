package com.atlassian.webdriver;

import com.atlassian.annotations.PublicApi;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utilities related to manipulating/querying of web elements (of any sort).
 * <p/>
 * NOTE: this class does not directly depend on any flavour of elements (e.g. WebDriver's
 * {@link org.openqa.selenium.WebElement}).
 *
 * @since 2.3
 */
@PublicApi
public final class Elements
{
    public static final String TAG_BODY = "body";
    public static final String TAG_DIV = "div";
    public static final String TAG_TR = "tr";
    public static final String TAG_TD = "td";
    public static final String TAG_UL = "ul";
    public static final String TAG_LI = "li";

    public static final String ATTRIBUTE_CLASS = "class";
    public static final String ATTRIBUTE_ID = "id";

    private Elements()
    {
        throw new AssertionError("Do not instantiate " + getClass().getSimpleName());
    }

    /**
     * Extract CSS class names from the CSS "class" attribute value.
     *
     * @param rawClassValue the CSS "class" attribute value
     * @return set extracted CSS class names, or an empty set, if {@code rawClassValue} is {@code null},
     * or a blank string; all extracted class names will be lower cased
     */
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static Set<String> getCssClasses(@Nullable String rawClassValue)
    {
        if (StringUtils.isBlank(rawClassValue))
        {
            return Collections.emptySet();
        }

        return FluentIterable.from(ImmutableSet.copyOf(rawClassValue.split("\\s+")))
                .transform(new Function<String, String>()
                {
                    @Nullable
                    @Override
                    public String apply(@Nullable String cssClass)
                    {
                        return StringUtils.isBlank(cssClass) ? null : cssClass.toLowerCase();
                    }
                })
                .filter(Predicates.notNull())
                .toSet();
    }

    /**
     * Checks to see if a raw {@code rawClassValue} contains a specific class of not. The check is case-insensitive.
     *
     * @param expectedClassName CSS class name to check for
     * @param rawClassValue raw value of the "class" attribute
     * @return {@code true}, if {@code rawClassValue} contains CSS {@code expectedClassName} (case-insensitive),
     * {@code false} otherwise
     */
    public static boolean hasCssClass(@Nonnull String expectedClassName, @Nullable String rawClassValue)
    {
        checkNotNull(expectedClassName, "className");

        return rawClassValue != null && getCssClasses(rawClassValue).contains(expectedClassName.toLowerCase());
    }
}
