// $Id$
package org.jenerate;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PluginPreference;

/**
 * @author jiayun
 */
public class JeneratePreferenceInitializer extends AbstractPreferenceInitializer {

    private static final String JENERATE_PLUGIN_ID = "org.jenerate";

    @Override
    public void initializeDefaultPreferences() {
        initializeDefaultValues(DefaultScope.INSTANCE.getNode(JENERATE_PLUGIN_ID));
    }

    /**
     * Package private for testing purpose
     */
    void initializeDefaultValues(IEclipsePreferences iEclipsePreferences) {
        for (PluginPreference<?> jeneratePreference : JeneratePreference.values()) {
            putPreference(iEclipsePreferences, jeneratePreference);
        }
    }

    /**
     * Package private for testing purpose
     */
    void putPreference(IEclipsePreferences iEclipsePreferences, PluginPreference<?> pluginPreference) {
        Class<?> type = pluginPreference.getType();
        String key = pluginPreference.getKey();
        Object defaultValue = pluginPreference.getDefaultValue();
        if (Boolean.class.isAssignableFrom(type)) {
            iEclipsePreferences.putBoolean(key, ((Boolean) defaultValue).booleanValue());
        } else if (String.class.isAssignableFrom(type)) {
            iEclipsePreferences.put(key, ((String) defaultValue));
        } else {
            throw new UnsupportedOperationException("The preference type '" + type + "' is not currently handled. ");
        }
    }
}
