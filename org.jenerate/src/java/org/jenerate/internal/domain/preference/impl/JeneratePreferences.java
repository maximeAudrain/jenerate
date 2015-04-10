package org.jenerate.internal.domain.preference.impl;

import java.util.LinkedHashSet;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
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

    public static final PluginPreference<MethodContentStrategyIdentifier> PREFERED_COMMON_METHODS_CONTENT_STRATEGY = new AbstractPluginPreference<MethodContentStrategyIdentifier>(
            "preferedContentStrategy", "Prefered common methods content strategy",
            MethodContentStrategyIdentifier.USE_COMMONS_LANG) {
        @Override
        public MethodContentStrategyIdentifier getCurrentPreferenceValue(IPreferenceStore preferenceStore) {
            return MethodContentStrategyIdentifier.valueOf(preferenceStore.getString(this.getKey()));
        }

        @Override
        public FieldEditor createFieldEditor(Composite parent) {
            MethodContentStrategyIdentifier[] values = MethodContentStrategyIdentifier.values();
            String[][] comboValues = new String[values.length][2];
            for (int i = 0; i < values.length; i++) {
                comboValues[i] = new String[] { values[i].name(), values[i].name() };
            }
            return new ComboFieldEditor(this.getKey(), this.getDescription(), comboValues, parent);
        }

        @Override
        public void putDefaultValue(IEclipsePreferences preferences) {
            preferences.put(this.getKey(), this.getDefaultValue().name());
        }
    };

    public static final PluginPreference<Boolean> CACHE_HASHCODE = new BooleanPluginPreference("cacheHashCode",
            "Cache &hashCode when all selected fields are final", Boolean.TRUE);

    public static final PluginPreference<String> HASHCODE_CACHING_FIELD = new StringPluginPreference(
            "hashCodeCachingField", "Hash&Code caching field", "hashCode");

    public static final PluginPreference<Boolean> CACHE_TOSTRING = new BooleanPluginPreference("cacheToString",
            "Cache &toString when all selected fields are final", Boolean.TRUE);

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
            "useBlocksInIfStatements", "&Use blocks in 'if' statments", Boolean.FALSE);

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
