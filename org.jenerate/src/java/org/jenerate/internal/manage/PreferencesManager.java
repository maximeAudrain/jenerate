package org.jenerate.internal.manage;

import org.jenerate.internal.domain.preference.impl.JeneratePreference;

/**
 * TODO : make this guy generic if possible
 * 
 * @author maudrain
 */
public interface PreferencesManager {

    Object getCurrentPreferenceValue(JeneratePreference preference);

}
