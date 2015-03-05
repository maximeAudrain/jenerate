// $Id$
package org.jenerate.internal.ui.preferences;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

/**
 * @author jiayun
 * @author maudrain
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
        IEclipsePreferences iEclipsePreferences = DefaultScope.INSTANCE.getNode("org.jenerate");
        iEclipsePreferences.putBoolean(USE_COMMONS_LANG3, false);
        iEclipsePreferences.putBoolean(CACHE_HASHCODE, true);
        iEclipsePreferences.put(HASHCODE_CACHING_FIELD, "hashCode");
        iEclipsePreferences.putBoolean(CACHE_TOSTRING, true);
        iEclipsePreferences.put(TOSTRING_CACHING_FIELD, "toString");
        iEclipsePreferences.putBoolean(ADD_OVERRIDE_ANNOTATION, true);
        iEclipsePreferences.putBoolean(GENERIFY_COMPARETO, true);
        iEclipsePreferences.putBoolean(DISPLAY_FIELDS_OF_SUPERCLASSES, false);
        iEclipsePreferences.putBoolean(USE_GETTERS_INSTEAD_OF_FIELDS, false);
        iEclipsePreferences.putBoolean(USE_BLOCKS_IN_IF_STATEMENTS, false);
    }
}
