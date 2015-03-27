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
    public String getKey();

    /**
     * @return the description of this preference
     */
    public String getDescription();

    /**
     * @return the type for the preference default value
     */
    public Class<T> getType();

    /**
     * @return the default value of this preference
     */
    public T getDefaultValue();

}
