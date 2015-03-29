package org.jenerate.internal.manage;

import org.jenerate.internal.domain.preference.PluginPreference;

/**
 * The preference manager for the plugin. Retrieves the current value of a certain {@link PluginPreference}
 * 
 * @author maudrain
 */
public interface PreferencesManager {

    /**
     * Gets the current value for a specific preference
     * 
     * @param preference the preference to get the value from
     * @return the value for this preference
     */
    <T> T getCurrentPreferenceValue(PluginPreference<T> preference);

}
