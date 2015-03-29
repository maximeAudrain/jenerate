// $Id$
package org.jenerate.internal.ui.preference;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.domain.preference.PluginPreference;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;

/**
 * @author jiayun
 * @author maudrain
 */
public class JenerateBasePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private final Map<PluginPreference<?>, FieldEditor> fieldEditors = new HashMap<>();

    public JenerateBasePreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        setPreferenceStore(JeneratePlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        for (PluginPreference<?> pluginPreference : JeneratePreferences.getAllPreferences()) {
            FieldEditor fieldEditor = createFieldEditor(pluginPreference);
            fieldEditors.put(pluginPreference, fieldEditor);
            addField(fieldEditor);
        }

        // getHashCodeCachingField().setEnabled(getCacheHashCodeField().getBooleanValue(), getFieldEditorParent());
        // getToStringCachingField().setEnabled(getCacheToStringField().getBooleanValue(), getFieldEditorParent());
    }

    /**
     * XXX looks like PrefInit method and PrefMngr method.
     */
    private FieldEditor createFieldEditor(PluginPreference<?> pluginPreference) {
        Class<?> type = pluginPreference.getType();
        String key = pluginPreference.getKey();
        String description = pluginPreference.getDescription();
        if (Boolean.class.isAssignableFrom(type)) {
            return new BooleanFieldEditor(key, description, getFieldEditorParent());
        } else if (String.class.isAssignableFrom(type)) {
            return new StringFieldEditor(key, description, getFieldEditorParent());
        } else {
            throw new UnsupportedOperationException("The preference type '" + type + "' for plugin preference '"
                    + pluginPreference + "' is not currently handled. ");
        }
    }

    @Override
    protected void checkState() {
        super.checkState();

        if (!isValid())
            return;

        String hashCodeFieldStringValue = getHashCodeCachingField().getStringValue();
        String toStringFieldStringValue = getToStringCachingField().getStringValue();

        IStatus status = JavaConventions.validateIdentifier(hashCodeFieldStringValue);
        if (!status.isOK()) {
            setErrorMessage(status.getMessage());
            setValid(false);
            return;
        }

        status = JavaConventions.validateIdentifier(toStringFieldStringValue);
        if (!status.isOK()) {
            setErrorMessage(status.getMessage());
            setValid(false);
            return;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);

        if (event.getProperty().equals(FieldEditor.VALUE)) {

            if (event.getSource() == getCacheHashCodeField()) {
                getHashCodeCachingField().setEnabled(getCacheHashCodeField().getBooleanValue(), getFieldEditorParent());

            } else if (event.getSource() == getCacheToStringField()) {
                getToStringCachingField().setEnabled(getCacheToStringField().getBooleanValue(), getFieldEditorParent());

            } else if (event.getSource() == getHashCodeCachingField() || event.getSource() == getToStringCachingField()) {
                checkState();
            }
        }
    }

    @Override
    public void init(IWorkbench workbench) {
        /* Nothing to be done here */
    }

    private StringFieldEditor getToStringCachingField() {
        return (StringFieldEditor) fieldEditors.get(JeneratePreferences.TOSTRING_CACHING_FIELD);
    }

    private StringFieldEditor getHashCodeCachingField() {
        return (StringFieldEditor) fieldEditors.get(JeneratePreferences.HASHCODE_CACHING_FIELD);
    }

    private BooleanFieldEditor getCacheToStringField() {
        return (BooleanFieldEditor) fieldEditors.get(JeneratePreferences.CACHE_TOSTRING);
    }

    private BooleanFieldEditor getCacheHashCodeField() {
        return (BooleanFieldEditor) fieldEditors.get(JeneratePreferences.CACHE_HASHCODE);
    }
}
