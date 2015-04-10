package org.jenerate.internal.domain.preference.impl;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.swt.widgets.Display;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.PluginPreference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link StrategyIdentifierPluginPreference}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class StrategyIdentifierPluginPreferenceTest extends AbstractPluginPreferenceTest<StrategyIdentifier> {

    private static final MethodContentStrategyIdentifier DEFAULT_VALUE = MethodContentStrategyIdentifier.USE_COMMONS_LANG;

    private final String[][] comboValues = {
            { MethodContentStrategyIdentifier.USE_COMMONS_LANG.getName(),
                    MethodContentStrategyIdentifier.USE_COMMONS_LANG.getName() },
            { MethodContentStrategyIdentifier.USE_COMMONS_LANG3.getName(),
                    MethodContentStrategyIdentifier.USE_COMMONS_LANG3.getName() },
            { MethodContentStrategyIdentifier.USE_GUAVA.getName(), MethodContentStrategyIdentifier.USE_GUAVA.getName() } };

    @Test
    public void testGetCurrentPreferenceValue() {
        MethodContentStrategyIdentifier currentStrategy = MethodContentStrategyIdentifier.USE_COMMONS_LANG3;
        when(preferenceStore.getString(KEY)).thenReturn(currentStrategy.getName());
        assertEquals(currentStrategy, pluginPreference.getCurrentPreferenceValue(preferenceStore));
    }

    @Test
    public void testCreateFieldEditor() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                assertEquals(new ComboFieldEditor(KEY, DESCRIPTION, comboValues, parent),
                        pluginPreference.createFieldEditor(parent));
            }
        });
    }

    @Test
    public void testPutDefaultValue() {
        pluginPreference.putDefaultValue(preferences);
        verify(preferences, times(1)).put(KEY, DEFAULT_VALUE.getName());
    }

    @Override
    public PluginPreference<StrategyIdentifier> getConcrete() {
        return new StrategyIdentifierPluginPreference(KEY, DESCRIPTION, DEFAULT_VALUE);
    }

    @Override
    public StrategyIdentifier getDefaultValue() {
        return DEFAULT_VALUE;
    }

}
