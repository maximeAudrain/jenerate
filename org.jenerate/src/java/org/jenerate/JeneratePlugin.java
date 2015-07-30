// $Id$
package org.jenerate;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class JeneratePlugin extends AbstractUIPlugin {

    private static final String JENERATE_PLUGIN_RESOURCES_ID = "org.jenerate.JeneratePluginResources";

    // The shared instance.
    private static JeneratePlugin plugin;

    // Resource bundle.
    private ResourceBundle resourceBundle;

    /**
     * The constructor.
     */
    public JeneratePlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle(JENERATE_PLUGIN_RESOURCES_ID);
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }

    /**
     * Returns the shared instance.
     */
    public static JeneratePlugin getDefault() {
        return plugin;
    }

    /**
     * Returns the string from the plugin's resource bundle, or 'key' if not found.
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = JeneratePlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Returns the plugin's resource bundle,
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
