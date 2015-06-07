package com.atlassian.webtest.ui.keys;

/**
 * A type-mode aware component. Exposes its current type mode.
 *
 * @see TypeMode
 * @since 4.3
 */
public interface TypeModeAware
{

    /**
     * Type mode of this component. If not explicitly specified, {@link TypeMode#DEFAULT} should be returned.
     *
     * @return type mode
     */
    TypeMode typeMode();
}
