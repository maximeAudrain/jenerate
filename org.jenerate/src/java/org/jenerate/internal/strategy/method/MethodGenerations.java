package org.jenerate.internal.strategy.method;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.util.impl.CompilerSourceUtils;

/**
 * Utility class that contains common methods for the methods code generation
 * 
 * @author maudrain
 */
public final class MethodGenerations {

    public MethodGenerations() {
        /* Only static helper methods */
    }

    /**
     * @param objectClass the object class to check if the source level is greater than 1.5
     * @param implementedOrExtendedInSuperType {@code true} if compareTo is implemented or extended in the super type,
     *            {@code false} otherwise.
     * @param preferencesManager the preference manager to extract the generify preference from
     * @return {@code true} if compareTo can be generified, {@code false} otherwise
     */
    public static boolean generifyCompareTo(IType objectClass, boolean implementedOrExtendedInSuperType,
            PreferencesManager preferencesManager) {
        boolean generifyPreference = preferencesManager.getCurrentPreferenceValue(
                JeneratePreferences.GENERIFY_COMPARETO).booleanValue();
        return generifyPreference && CompilerSourceUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass)
                && !implementedOrExtendedInSuperType;
    }
}
