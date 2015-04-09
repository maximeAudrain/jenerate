package org.jenerate.internal.domain.preference.impl;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.PluginPreference;

/**
 * Holds all the {@link PluginPreference}s for the Jenerate plugin
 * 
 * @author maudrain
 */
public final class JeneratePreferences {

    private JeneratePreferences() {
        /* Only static constants */
    }

    public static final PluginPreference<MethodContentStrategyIdentifier> PREFERED_COMMON_METHODS_CONTENT_STRATEGY = new PluginPreferenceImpl<MethodContentStrategyIdentifier>(
            "preferedContentStrategy", "Prefered common methods content strategy",
            MethodContentStrategyIdentifier.class, MethodContentStrategyIdentifier.USE_COMMONS_LANG);

    public static final PluginPreference<Boolean> CACHE_HASHCODE = new PluginPreferenceImpl<Boolean>("cacheHashCode",
            "Cache &hashCode when all selected fields are final", Boolean.class, Boolean.TRUE);

    public static final PluginPreference<String> HASHCODE_CACHING_FIELD = new PluginPreferenceImpl<String>(
            "hashCodeCachingField", "Hash&Code caching field", String.class, "hashCode");

    public static final PluginPreference<Boolean> CACHE_TOSTRING = new PluginPreferenceImpl<Boolean>("cacheToString",
            "Cache &toString when all selected fields are final", Boolean.class, Boolean.TRUE);

    public static final PluginPreference<String> TOSTRING_CACHING_FIELD = new PluginPreferenceImpl<String>(
            "toStringCachingField", "To&String caching field", String.class, "toString");

    public static final PluginPreference<Boolean> ADD_OVERRIDE_ANNOTATION = new PluginPreferenceImpl<Boolean>(
            "addOverrideAnnotation", "Add @&Override when the source compatibility is 5.0 or above", Boolean.class,
            Boolean.TRUE);

    public static final PluginPreference<Boolean> GENERIFY_COMPARETO = new PluginPreferenceImpl<Boolean>(
            "generifyCompareTo", "&Generify compareTo when the source compatibility is 5.0 or above", Boolean.class,
            Boolean.TRUE);

    public static final PluginPreference<Boolean> DISPLAY_FIELDS_OF_SUPERCLASSES = new PluginPreferenceImpl<Boolean>(
            "displayFieldsOfSuperclasses", "&Display fields of superclasses", Boolean.class, Boolean.FALSE);

    public static final PluginPreference<Boolean> USE_GETTERS_INSTEAD_OF_FIELDS = new PluginPreferenceImpl<Boolean>(
            "useGettersInsteadOfFields", "&Use getters instead of fields (for Hibernate)", Boolean.class, Boolean.FALSE);

    public static final PluginPreference<Boolean> USE_BLOCKS_IN_IF_STATEMENTS = new PluginPreferenceImpl<Boolean>(
            "useBlocksInIfStatements", "&Use blocks in 'if' statments", Boolean.class, Boolean.FALSE);

    /**
     * @return all preferences of the Jenerate plugin. The ordering of the preferences is important because for example
     *         the preference page uses it to generate the preferences fields.
     */
    public static LinkedHashSet<PluginPreference<?>> getAllPreferences() {
        LinkedHashSet<PluginPreference<?>> allPreferences = new LinkedHashSet<PluginPreference<?>>();
        allPreferences.add(PREFERED_COMMON_METHODS_CONTENT_STRATEGY);
        allPreferences.add(CACHE_HASHCODE);
        allPreferences.add(HASHCODE_CACHING_FIELD);
        allPreferences.add(CACHE_TOSTRING);
        allPreferences.add(TOSTRING_CACHING_FIELD);
        allPreferences.add(ADD_OVERRIDE_ANNOTATION);
        allPreferences.add(GENERIFY_COMPARETO);
        allPreferences.add(DISPLAY_FIELDS_OF_SUPERCLASSES);
        allPreferences.add(USE_GETTERS_INSTEAD_OF_FIELDS);
        allPreferences.add(USE_BLOCKS_IN_IF_STATEMENTS);
        return allPreferences;
    }

}
