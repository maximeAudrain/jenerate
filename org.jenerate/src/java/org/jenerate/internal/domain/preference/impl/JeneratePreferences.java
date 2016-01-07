package org.jenerate.internal.domain.preference.impl;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.identifier.StrategyIdentifier;
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

    public static final PluginPreference<StrategyIdentifier> PREFERED_COMMON_METHODS_CONTENT_STRATEGY = new StrategyIdentifierPluginPreference(
            "preferedContentStrategy", "Prefered common methods content strategy",
            MethodContentStrategyIdentifier.USE_COMMONS_LANG3);

    public static final PluginPreference<Boolean> CACHE_HASHCODE = new BooleanPluginPreference("cacheHashCode",
            "Cache &hashCode when all selected fields are final", Boolean.FALSE);

    public static final PluginPreference<String> HASHCODE_CACHING_FIELD = new StringPluginPreference(
            "hashCodeCachingField", "Hash&Code caching field", "hashCode");

    public static final PluginPreference<Boolean> CACHE_TOSTRING = new BooleanPluginPreference("cacheToString",
            "Cache &toString when all selected fields are final", Boolean.FALSE);

    public static final PluginPreference<String> TOSTRING_CACHING_FIELD = new StringPluginPreference(
            "toStringCachingField", "To&String caching field", "toString");

    public static final PluginPreference<Boolean> ADD_OVERRIDE_ANNOTATION = new BooleanPluginPreference(
            "addOverrideAnnotation", "Add @&Override when the source compatibility is 5.0 or above", Boolean.TRUE);

    public static final PluginPreference<Boolean> GENERIFY_COMPARETO = new BooleanPluginPreference("generifyCompareTo",
            "&Generify compareTo when the source compatibility is 5.0 or above", Boolean.TRUE);

    public static final PluginPreference<Boolean> DISPLAY_FIELDS_OF_SUPERCLASSES = new BooleanPluginPreference(
            "displayFieldsOfSuperclasses", "&Display fields of superclasses", Boolean.FALSE);

    public static final PluginPreference<Boolean> USE_GETTERS_INSTEAD_OF_FIELDS = new BooleanPluginPreference(
            "useGettersInsteadOfFields", "&Use getters instead of fields (for Hibernate)", Boolean.FALSE);

    public static final PluginPreference<Boolean> USE_BLOCKS_IN_IF_STATEMENTS = new BooleanPluginPreference(
            "useBlocksInIfStatements", "&Use blocks in 'if' statements", Boolean.TRUE);

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
