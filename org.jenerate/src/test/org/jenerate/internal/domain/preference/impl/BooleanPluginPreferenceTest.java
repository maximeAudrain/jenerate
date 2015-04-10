package org.jenerate.internal.domain.preference.impl;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Display;
import org.jenerate.internal.domain.preference.PluginPreference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link BooleanPluginPreference}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class BooleanPluginPreferenceTest extends AbstractPluginPreferenceTest<Boolean> {

    private static final Boolean DEFAULT_VALUE = Boolean.TRUE;

    @Test
    public void testGetCurrentPreferenceValue() {
        when(preferenceStore.getBoolean(KEY)).thenReturn(false);
        assertEquals(Boolean.FALSE, pluginPreference.getCurrentPreferenceValue(preferenceStore));
    }

    @Test
    public void testCreateFieldEditor() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                assertEquals(new BooleanFieldEditor(KEY, DESCRIPTION, parent),
                        pluginPreference.createFieldEditor(parent));
            }
        });
    }

    @Test
    public void testPutDefaultValue() {
        pluginPreference.putDefaultValue(preferences);
        verify(preferences, times(1)).putBoolean(KEY, DEFAULT_VALUE.booleanValue());
    }

    @Override
    public PluginPreference<Boolean> getConcrete() {
        return new BooleanPluginPreference(KEY, DESCRIPTION, DEFAULT_VALUE);
    }

    @Override
    public Boolean getDefaultValue() {
        return DEFAULT_VALUE;
    }

}
