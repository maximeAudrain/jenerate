package org.jenerate.internal.ui.preferences.impl;

import org.eclipse.core.runtime.Preferences;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class PreferencesManagerImpl implements PreferencesManager {

    @Override
    public Object getCurrentPreferenceValue(JeneratePreference preference) {
        Class<?> type = preference.getType();
        String key = preference.getKey();
        Preferences pluginPreferences = JeneratePlugin.getDefault().getPluginPreferences();
        if (Boolean.class.isAssignableFrom(type)) {
            return pluginPreferences.getBoolean(key);
        } else if (String.class.isAssignableFrom(type)) {
            return pluginPreferences.getString(key);
        } else {
            throw new UnsupportedOperationException("The preference type '" + type + "' is not currently handled. ");
        }
    }

}
