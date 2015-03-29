package org.jenerate.internal.domain.preference.impl;

import org.jenerate.internal.domain.preference.PluginPreference;

/**
 * Default impelementation of the {@link PluginPreference}
 * 
 * @author maudrain
 * @param <T> the type of preference value
 */
public final class PluginPreferenceImpl<T> implements PluginPreference<T> {

    private final String key;
    private final String description;
    private final Class<T> type;
    private final T defaultValue;

    /**
     * Constructor
     * 
     * @param key the key for this preference
     * @param description the description for this preference
     * @param type the type of this preference
     * @param defaultValue the default value of this preference
     */
    public PluginPreferenceImpl(String key, String description, Class<T> type, T defaultValue) {
        this.key = key;
        this.description = description;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public final String getKey() {
        return key;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }
}
