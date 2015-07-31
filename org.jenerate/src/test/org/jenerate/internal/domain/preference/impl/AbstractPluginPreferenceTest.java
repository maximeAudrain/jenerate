package org.jenerate.internal.domain.preference.impl;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.jenerate.internal.domain.preference.PluginPreference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Abstract unit tests for the {@link PluginPreference}s
 * 
 * @author maudrain
 * @param <T> the type handled by the preference
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractPluginPreferenceTest<T> {

    protected static final String KEY = "KEY";
    protected static final String DESCRIPTION = "DESCRIPTION";

    @Mock
    protected IPreferenceStore preferenceStore;
    @Mock
    protected Composite parent;
    @Mock
    protected IEclipsePreferences preferences;

    protected PluginPreference<T> pluginPreference;

    @Before
    public final void setUp() {
        pluginPreference = getConcrete();
    }

    @Test
    public void testGetKey() {
        assertEquals(KEY, pluginPreference.getKey());
    }

    @Test
    public void testGetDescription() {
        assertEquals(DESCRIPTION, pluginPreference.getDescription());
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals(getDefaultValue(), pluginPreference.getDefaultValue());
    }

    public abstract PluginPreference<T> getConcrete();

    public abstract T getDefaultValue();

}
