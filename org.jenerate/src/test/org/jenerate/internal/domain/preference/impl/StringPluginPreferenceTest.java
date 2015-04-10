package org.jenerate.internal.domain.preference.impl;

import org.eclipse.jface.preference.StringFieldEditor;
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
 * Unit tests for the {@link StringPluginPreference}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class StringPluginPreferenceTest extends AbstractPluginPreferenceTest<String> {

    private static final String DEFAULT_VALUE = "DEFAULT_VALUE";

    @Test
    public void testGetCurrentPreferenceValue() {
        String someString = "some_string";
        when(preferenceStore.getString(KEY)).thenReturn(someString);
        assertEquals(someString, pluginPreference.getCurrentPreferenceValue(preferenceStore));
    }

    @Test
    public void testCreateFieldEditor() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                assertEquals(new StringFieldEditor(KEY, DESCRIPTION, parent),
                        pluginPreference.createFieldEditor(parent));
            }
        });
    }

    @Test
    public void testPutDefaultValue() {
        pluginPreference.putDefaultValue(preferences);
        verify(preferences, times(1)).put(KEY, DEFAULT_VALUE);
    }

    @Override
    public PluginPreference<String> getConcrete() {
        return new StringPluginPreference(KEY, DESCRIPTION, DEFAULT_VALUE);
    }

    @Override
    public String getDefaultValue() {
        return DEFAULT_VALUE;
    }

}
