// $Id$
package org.jenerate.internal.util;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.ui.preferences.PreferenceConstants;

/**
 * @author jiayun
 */
public final class PreferenceUtils {

    private PreferenceUtils() {
    }

    public static boolean getUseCommonsLang3() {
        return JeneratePlugin.getDefault().getPluginPreferences().getBoolean(PreferenceConstants.USE_COMMONS_LANG3);
    }

    public static boolean getCacheHashCode() {
        return JeneratePlugin.getDefault().getPluginPreferences().getBoolean(PreferenceConstants.CACHE_HASHCODE);
    }

    public static String getHashCodeCachingField() {
        return JeneratePlugin.getDefault().getPluginPreferences().getString(PreferenceConstants.HASHCODE_CACHING_FIELD);
    }

    public static boolean getCacheToString() {
        return JeneratePlugin.getDefault().getPluginPreferences().getBoolean(PreferenceConstants.CACHE_TOSTRING);
    }

    public static String getToStringCachingField() {
        return JeneratePlugin.getDefault().getPluginPreferences().getString(PreferenceConstants.TOSTRING_CACHING_FIELD);
    }

    public static boolean getAddOverride() {
        return JeneratePlugin.getDefault().getPluginPreferences()
                .getBoolean(PreferenceConstants.ADD_OVERRIDE_ANNOTATION);
    }

    public static boolean getGenerifyCompareTo() {
        return JeneratePlugin.getDefault().getPluginPreferences().getBoolean(PreferenceConstants.GENERIFY_COMPARETO);
    }

    public static boolean getDisplayFieldsOfSuperclasses() {
        return JeneratePlugin.getDefault().getPluginPreferences()
                .getBoolean(PreferenceConstants.DISPLAY_FIELDS_OF_SUPERCLASSES);
    }

    public static boolean getUseGettersInsteadOfFields() {
        return JeneratePlugin.getDefault().getPluginPreferences()
                .getBoolean(PreferenceConstants.USE_GETTERS_INSTEAD_OF_FIELDS);
    }

    public static boolean getUseBlocksInIfStatements() {
        return JeneratePlugin.getDefault().getPluginPreferences()
                .getBoolean(PreferenceConstants.USE_BLOCKS_IN_IF_STATEMENTS);
    }

    public static boolean isSourceLevelGreaterThanOrEqualTo5(IJavaProject project) {
        float sc = Float.parseFloat(project.getOption(JavaCore.COMPILER_SOURCE, true));
        return sc >= 1.5;
    }
}
