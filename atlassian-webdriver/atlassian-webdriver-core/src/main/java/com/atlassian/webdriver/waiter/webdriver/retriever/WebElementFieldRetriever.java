package com.atlassian.webdriver.waiter.webdriver.retriever;

import com.google.common.base.Preconditions;

/**
 * @since 2.1.0
 */
public class WebElementFieldRetriever
{

    private final Supplier<String> fieldSupplier;

    private WebElementFieldRetriever(Supplier<String> fieldSupplier)
    {
        this.fieldSupplier = Preconditions.checkNotNull(fieldSupplier, "The field supplier can not be null.");
    }

    public static WebElementFieldRetriever newTextRetriever(final WebElementRetriever webElementRetriever)
    {
        Preconditions.checkNotNull(webElementRetriever, "The web element retriever can not be null.");
        return new WebElementFieldRetriever(new Supplier<String>()
        {
            @Override
            public String supply()
            {
                return webElementRetriever.retrieveElement().getText();
            }
        });
    }

    public static WebElementFieldRetriever newAttributeRetriever(final WebElementRetriever webElementRetriever,
            final String fieldName)
    {
        Preconditions.checkNotNull(webElementRetriever, "The web element retriever can not be null.");
        Preconditions.checkNotNull(fieldName, "The field name can not be null.");
        return new WebElementFieldRetriever(new Supplier<String>()
        {
            @Override
            public String supply()
            {
                return webElementRetriever.retrieveElement().getAttribute(fieldName);
            }
        });
    }

    public String retrieveField()
    {
        return fieldSupplier.supply();
    }
}
