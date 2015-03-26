package org.jenerate.internal.ui.preferences;

@SuppressWarnings("rawtypes")
public enum JeneratePreference implements PluginPreference {

    USE_COMMONS_LANG3("useCommonsLang3", "&Import commons-lang3 for all code generation", Boolean.class, Boolean.FALSE),

    CACHE_HASHCODE("cacheHashCode", "Cache &hashCode when all selected fields are final", Boolean.class, Boolean.TRUE),
    
    HASHCODE_CACHING_FIELD("hashCodeCachingField", "Hash&Code caching field", String.class, "hashCode"),

    CACHE_TOSTRING("cacheToString", "Cache &toString when all selected fields are final", Boolean.class, Boolean.TRUE),

    TOSTRING_CACHING_FIELD("toStringCachingField", "To&String caching field", String.class, "toString"),

    ADD_OVERRIDE_ANNOTATION("addOverrideAnnotation", "Add @&Override when the source compatibility is 5.0 or above",
            Boolean.class, Boolean.TRUE),

    GENERIFY_COMPARETO("generifyCompareTo", "&Generify compareTo when the source compatibility is 5.0 or above",
            Boolean.class, Boolean.TRUE),

    DISPLAY_FIELDS_OF_SUPERCLASSES("displayFieldsOfSuperclasses", "&Display fields of superclasses", Boolean.class,
            Boolean.FALSE),

    USE_GETTERS_INSTEAD_OF_FIELDS("useGettersInsteadOfFields", "&Use getters instead of fields (for Hibernate)",
            Boolean.class, Boolean.FALSE),

    USE_BLOCKS_IN_IF_STATEMENTS("useBlocksInIfStatements", "&Use blocks in 'if' statments", Boolean.class,
            Boolean.FALSE);

    private final String key;
    private final String description;
    private final Class<?> type;
    private final Object defaultValue;

    private JeneratePreference(String key, String description, Class<?> type, Object defaultValue) {
        this.key = key;
        this.description = description;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
