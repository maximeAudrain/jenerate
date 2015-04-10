package org.jenerate.internal.manage.impl;

import org.jenerate.JeneratePlugin;
import org.jenerate.internal.domain.preference.PluginPreference;
import org.jenerate.internal.manage.PreferencesManager;

/**
 * Default implementation of the {@link PreferencesManager}
 * 
 * @author maudrain
 */
public final class PreferencesManagerImpl implements PreferencesManager {

    @Override
    public <T> T getCurrentPreferenceValue(PluginPreference<T> preference) {
        return preference.getCurrentPreferenceValue(JeneratePlugin.getDefault().getPreferenceStore());
    }
}
