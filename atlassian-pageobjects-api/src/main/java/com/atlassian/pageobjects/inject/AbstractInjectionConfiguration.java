package com.atlassian.pageobjects.inject;

import com.atlassian.annotations.ExperimentalApi;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract implementation of {@link com.atlassian.pageobjects.inject.InjectionConfiguration}.
 *
 * @since 2.1
 */
@ExperimentalApi
public abstract class AbstractInjectionConfiguration implements InjectionConfiguration
{
    protected final List<InterfaceToImpl> interfacesToImpls = Lists.newLinkedList();
    protected final List<InterfaceToInstance> interfacesToInstances = Lists.newLinkedList();

    protected static class InterfaceToImpl
    {
        public final Class<?> interfaceType;
        public final Class<?> implementation;

        private InterfaceToImpl(Class<?> interfaceType, Class<?> implementation)
        {
            this.interfaceType = checkNotNull(interfaceType, "interfaceType");
            this.implementation = checkNotNull(implementation, "implementation");
            if (!interfaceType.isAssignableFrom(implementation))
            {
                throw new IllegalArgumentException("Implementation type " + implementation.getName() + " does not "
                        + "implement " + interfaceType.getName());
            }
        }
    }

    protected static class InterfaceToInstance
    {
        public final Class<?> interfaceType;
        public final Object instance;

        private InterfaceToInstance(Class<?> interfaceType, Object instance)
        {
            this.interfaceType = checkNotNull(interfaceType, "interfaceType");
            this.instance = checkNotNull(instance, "instance");
            if (!interfaceType.isInstance(instance))
            {
                throw new IllegalArgumentException("Object " + instance + " does not "
                        + "implement " + interfaceType.getName());
            }
        }
    }

    @Override
    @Nonnull
    public final <I> InjectionConfiguration addImplementation(@Nonnull Class<I> interfaceType, @Nonnull Class<? extends I> implementationType)
    {
        checkNotNull(interfaceType, "interfaceType");
        checkNotNull(implementationType, "implementationType");
        interfacesToImpls.add(new InterfaceToImpl(interfaceType, implementationType));
        return this;
    }

    @Override
    @Nonnull
    public final <C, I extends C> InjectionConfiguration addSingleton(@Nonnull Class<C> type, @Nonnull I instance)
    {
        checkNotNull(type, "type");
        checkNotNull(instance, "instance");
        interfacesToInstances.add(new InterfaceToInstance(type, instance));
        return this;
    }


}
