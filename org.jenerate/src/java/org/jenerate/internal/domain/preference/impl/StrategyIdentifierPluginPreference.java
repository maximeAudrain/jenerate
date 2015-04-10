package org.jenerate.internal.domain.preference.impl;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.PluginPreference;

/**
 * Default implementation of the {@link PluginPreference} for StrategyIdentifier preferences
 * 
 * @author maudrain
 */
public class StrategyIdentifierPluginPreference extends AbstractPluginPreference<StrategyIdentifier> {

    /**
     * Constructor
     * 
     * @param key the key for this preference
     * @param description the description for this preference
     * @param defaultValue the default value of this preference
     */
    public StrategyIdentifierPluginPreference(String key, String description, StrategyIdentifier defaultValue) {
        super(key, description, defaultValue);
    }

    @Override
    public StrategyIdentifier getCurrentPreferenceValue(IPreferenceStore preferenceStore) {
        return MethodContentStrategyIdentifier.valueOf(preferenceStore.getString(this.getKey()));
    }

    @Override
    public FieldEditor createFieldEditor(Composite parent) {
        MethodContentStrategyIdentifier[] values = MethodContentStrategyIdentifier.values();
        String[][] comboValues = new String[values.length][2];
        for (int i = 0; i < values.length; i++) {
            comboValues[i] = new String[] { values[i].name(), values[i].name() };
        }
        return new ComboFieldEditor(this.getKey(), this.getDescription(), comboValues, parent);
    }

    @Override
    public void putDefaultValue(IEclipsePreferences preferences) {
        preferences.put(this.getKey(), this.getDefaultValue().getName());
    }

}
