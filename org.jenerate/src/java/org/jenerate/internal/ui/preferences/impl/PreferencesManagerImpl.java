package org.jenerate.internal.ui.preferences.impl;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class PreferencesManagerImpl implements PreferencesManager {

    @Override
    public Object getCurrentPreferenceValue(JeneratePreference preference) {
        Class<?> type = preference.getType();
        String key = preference.getKey();
        IPreferenceStore preferenceStore = JeneratePlugin.getDefault().getPreferenceStore();
        if (Boolean.class.isAssignableFrom(type)) {
            return preferenceStore.getBoolean(key);
        } else if (String.class.isAssignableFrom(type)) {
            return preferenceStore.getString(key);
        } else {
            throw new UnsupportedOperationException("The preference type '" + type + "' is not currently handled. ");
        }
    }

}
