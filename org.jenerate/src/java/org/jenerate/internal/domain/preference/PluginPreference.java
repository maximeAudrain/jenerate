package org.jenerate.internal.domain.preference;

/**
 * Defines a plugin preference. Contains a key as unique identifier, a description to be shown to the user and a default
 * value of a certain type.
 * 
 * @author maudrain
 * @param <T> the type of default value attached to this preference.
 */
public interface PluginPreference<T> {

    /**
     * @return the key for this preference
     */
    String getKey();

    /**
     * @return the description of this preference
     */
    String getDescription();

    /**
     * @return the type for the preference default value
     */
    Class<T> getType();

    /**
     * @return the default value of this preference
     */
    T getDefaultValue();

}
