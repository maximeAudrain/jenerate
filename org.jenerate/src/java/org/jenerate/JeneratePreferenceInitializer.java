//$Id$
package org.jenerate;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.jenerate.internal.ui.preferences.PreferenceConstants;

/**
 * @author jiayun
 */
public class JeneratePreferenceInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		PreferenceConstants.initializeDefaultValues();
	}

}
