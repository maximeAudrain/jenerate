// $Id$
package org.jenerate;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jenerate.internal.ui.preferences.JeneratePreference;

/**
 * @author jiayun
 */
public class JeneratePreferenceInitializer extends AbstractPreferenceInitializer {

    private static final String JENERATE_PLUGIN_NODE = "org.jenerate";

    @Override
    public void initializeDefaultPreferences() {
        initializeDefaultValues(DefaultScope.INSTANCE.getNode(JENERATE_PLUGIN_NODE));
    }

    /**
     * Package private for testing purpose
     */
    void initializeDefaultValues(IEclipsePreferences iEclipsePreferences) {
        for (JeneratePreference jeneratePreference : JeneratePreference.values()) {
            Class<?> type = jeneratePreference.getType();
            String key = jeneratePreference.getKey();
            Object defaultValue = jeneratePreference.getDefaultValue();
            if (Boolean.class.isAssignableFrom(type)) {
                iEclipsePreferences.putBoolean(key, ((Boolean) defaultValue).booleanValue());
            } else if (String.class.isAssignableFrom(type)) {
                iEclipsePreferences.put(key, ((String) defaultValue));
            } else {
                throw new UnsupportedOperationException("The preference type '" + type + "' is not currently handled. ");
            }
        }
    }
}
