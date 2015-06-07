package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.binder.PostInjectionProcessor;
import com.atlassian.pageobjects.util.InjectUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.atlassian.pageobjects.util.InjectUtils.forEachFieldWithAnnotation;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.transform;

/**
 * Find fields marked with @ElementBy annotation and set them to instances of WebDriverElement
 */
public class ElementByPostInjectionProcessor implements PostInjectionProcessor
{
    @Inject
    PageElementFinder finder;

    public <T> T process(T pageObject)
    {
        injectElements(pageObject);
        return pageObject;
    }

    private void injectElements(final Object instance)
    {
        final Map<String,Object> populatedFields = Maps.newHashMap();
        final List<Field> childFields = Lists.newArrayList();
        forEachFieldWithAnnotation(instance, ElementBy.class, new InjectUtils.FieldVisitor<ElementBy>()
        {
            public void visit(Field field, ElementBy annotation)
            {
                // Create the element to inject
                if (StringUtils.isNotEmpty(annotation.within()))
                {
                    childFields.add(field);
                }
                else
                {
                    final Object result = createAndInject(field, annotation, instance, getFinder(instance));
                    populatedFields.put(field.getName(), result);
                }
            }
        });
        int previousChildSize;
        do
        {
            previousChildSize = childFields.size();
            for (Iterator<Field> childFieldIterator = childFields.iterator(); childFieldIterator.hasNext();)
            {
                final Field childField = childFieldIterator.next();
                final ElementBy elementBy = childField.getAnnotation(ElementBy.class);
                final Object parent = populatedFields.get(elementBy.within());
                if (parent != null && parent instanceof PageElementFinder)
                {
                    final Object result = createAndInject(childField, elementBy, instance, (PageElementFinder)parent);
                    populatedFields.put(childField.getName(), result);
                    childFieldIterator.remove();
                }
                else if (parent != null)
                {
                    throw new IllegalStateException("@ElementBy for field " + childField.getName() + " defines parent '"
                     + elementBy.within() + "' whose value is of type '" + parent.getClass().getName() + "'. Parent "
                     + "fields must be strictly instances of '" + PageElementFinder.class.getName() + "' (usually "
                    + " that would be '" + PageElement.class.getName() + "')");
                }

            }
        }
        while (!childFields.isEmpty() && childFields.size() < previousChildSize);
        if (!childFields.isEmpty())
        {
            throw new IllegalStateException("Could not find parents for fields "
                    + transform(childFields, FieldToName.INSTANCE) + " annotated with @ElementBy in <" + instance
                    + ">. Please verify the problematic fields in the page object class");
        }
    }

    /**
     * If the object we're injecting into implements {@link com.atlassian.pageobjects.elements.PageElementFinder},
     * we let it do the job! Otherwise we use global finder.
     *
     * @param instance instance we're injecting into
     * @return page element finder to use
     */
    private PageElementFinder getFinder(Object instance)
    {
        if (instance instanceof PageElementFinder)
        {
            return (PageElementFinder) instance;
        }
        else
        {
            return finder;
        }
    }

    private Object createAndInject(Field field, ElementBy annotation, Object instance, PageElementFinder elementFinder)
    {
        Object value;
        if (isIterable(field))
        {
            value = createIterable(field, annotation, elementFinder);
        } else
        {
            value = createPageElement(field, annotation, elementFinder);
        }

        // Assign the value
        try
        {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        return value;
    }

    private Iterable<? extends PageElement> createIterable(Field field, ElementBy elementBy, PageElementFinder elementFinder)
    {
        By by = getSelector(elementBy);
        Class<? extends PageElement> fieldType = getFieldType(field, elementBy);
        return new PageElementIterableImpl(elementFinder, fieldType, by, elementBy.timeoutType());
    }

    private PageElement createPageElement(Field field, ElementBy elementBy, PageElementFinder elementFinder)
    {
        By by = getSelector(elementBy);
        Class<? extends PageElement> fieldType = getFieldType(field, elementBy);
        return elementFinder.find(by, fieldType, elementBy.timeoutType());
    }

    private By getSelector(ElementBy elementBy)
    {
        By by;

        if (elementBy.className().length() > 0)
        {
            by = By.className(elementBy.className());
        }
        else if (elementBy.id().length() > 0)
        {
            by = By.id(elementBy.id());
        }
        else if (elementBy.linkText().length() > 0)
        {
            by = By.linkText(elementBy.linkText());
        }
        else if (elementBy.partialLinkText().length() > 0)
        {
            by = By.partialLinkText(elementBy.partialLinkText());
        }
        else if(elementBy.cssSelector().length() >0)
        {
            by = By.cssSelector(elementBy.cssSelector());
        }
        else if(elementBy.name().length() > 0)
        {
            by = By.name(elementBy.name());
        }
        else if(elementBy.xpath().length() > 0)
        {
            by = By.xpath(elementBy.xpath());
        }
        else if(elementBy.tagName().length() > 0)
        {
            by = By.tagName(elementBy.tagName());
        }
        else
        {
            throw new IllegalArgumentException("No selector found");
        }
        return by;
    }

    /**
     * Returns the type of requested PageElement: it is the type of the field
     * overridden by the attribute 'pageElementClass' in the annotation.
     * 
     * @param field the field to inject
     * @param annotation the ElementBy annotation on the field 
     * @return the requested PageElement
     * @throws IllegalArgumentException if the field or the annotation don't extend PageElement
     */
    @SuppressWarnings({"unchecked"})
    private Class<? extends PageElement> getFieldType(Field field, ElementBy annotation) {
        Class<?> fieldType = field.getType();

        // Check whether the annotation overrides this type
        Class<? extends PageElement> annotatedType = annotation.pageElementClass();
        // Checks whether annotatedType is more specific than PageElement
        if (Iterable.class.isAssignableFrom(fieldType))
        {
            return annotatedType;
        }
        else if (annotatedType != PageElement.class)
        {
            checkArgument(fieldType.isAssignableFrom(annotatedType), "Field type " + annotatedType.getName()
                    + " does not implement " + fieldType.getName());
            return annotatedType;
        }

        checkArgument(PageElement.class.isAssignableFrom(fieldType), "Field type " + fieldType.getName()
                + " does not implement " + PageElement.class.getName());
        return (Class<? extends PageElement>) fieldType;
    }
    
    
    private boolean isIterable(Field field) {
        return Iterable.class.isAssignableFrom(field.getType());
    }

    private static enum FieldToName implements Function<Field,String>
    {
        INSTANCE;

        public String apply(Field from)
        {
            return from.getName();
        }
    }


}
