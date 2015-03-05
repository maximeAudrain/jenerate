//$Id$
package org.jenerate.internal.ui.preferences;

import org.eclipse.core.runtime.Preferences;
import org.jenerate.Commons4ePlugin;

/**
 * @author jiayun
 */
public class PreferenceConstants {

    private PreferenceConstants() {
    }
    
    public static final String USE_COMMONS_LANG3 = "useCommonsLang3";

    public static final String CACHE_HASHCODE = "cacheHashCode";

    public static final String HASHCODE_CACHING_FIELD = "hashCodeCachingField";

    public static final String CACHE_TOSTRING = "cacheToString";

    public static final String TOSTRING_CACHING_FIELD = "toStringCachingField";

    public static final String ADD_OVERRIDE_ANNOTATION = "addOverrideAnnotation";

    public static final String GENERIFY_COMPARETO = "generifyCompareTo";

    public static final String DISPLAY_FIELDS_OF_SUPERCLASSES = "displayFieldsOfSuperclasses";

    public static final String USE_GETTERS_INSTEAD_OF_FIELDS = "useGettersInsteadOfFields";
    
    public static final String USE_BLOCKS_IN_IF_STATEMENTS = "useBlocksInIfStatements";

    public static void initializeDefaultValues() {
        Preferences preferences = Commons4ePlugin.getDefault()
                .getPluginPreferences();
        preferences.setDefault(USE_COMMONS_LANG3, false);
        preferences.setDefault(CACHE_HASHCODE, true);
        preferences.setDefault(HASHCODE_CACHING_FIELD, "hashCode");
        preferences.setDefault(CACHE_TOSTRING, true);
        preferences.setDefault(TOSTRING_CACHING_FIELD, "toString");
        preferences.setDefault(ADD_OVERRIDE_ANNOTATION, true);
        preferences.setDefault(GENERIFY_COMPARETO, true);
        preferences.setDefault(DISPLAY_FIELDS_OF_SUPERCLASSES, false);
        preferences.setDefault(USE_GETTERS_INSTEAD_OF_FIELDS, false);
        preferences.setDefault(USE_BLOCKS_IN_IF_STATEMENTS, false);
    }
}
