package com.atlassian.webtest.ui.keys;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract implementation of the {@link com.atlassian.webtest.ui.keys.KeySequence} interface and its accompanying
 * interfaces.
 *
 * @since v1.21
 */
public abstract class AbstractKeySequence implements KeySequence, TypeModeAware, KeyEventAware
{

    private final EnumSet<ModifierKey> toPress;
    private final EnumSet<KeyEventType> keyEvents;
    private final TypeMode typeMode;


    public AbstractKeySequence(TypeMode typeMode, Collection<ModifierKey> toPress, Collection<KeyEventType> events)
    {
        this.toPress = toEnumSet(toPress, ModifierKey.class);
        this.keyEvents = toEnumSet(events, KeyEventType.class);
        this.typeMode = checkNotNull(typeMode, "typeMode");
    }

    private <E extends Enum<E>> EnumSet<E> toEnumSet(Collection<E> toPress, Class<E> enumType)
    {
        if (toPress instanceof EnumSet<?>)
        {
            return EnumSet.copyOf((EnumSet<E>) toPress);
        }
        return toPress.size() == 0 ? EnumSet.noneOf(enumType) : EnumSet.copyOf(toPress);
    }


    public final Set<ModifierKey> withPressed()
    {
        return toPress;
    }

    public final Set<KeyEventType> keyEvents()
    {
        return keyEvents;
    }

    public final TypeMode typeMode()
    {
        return typeMode;
    }
}
