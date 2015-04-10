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
            FieldEditor fieldEditor = pluginPreference.createFieldEditor(getFieldEditorParent());
            fieldEditors.put(pluginPreference, fieldEditor);
            addField(fieldEditor);
        }

        // XXX fix me : at initialization, does not enable/disable the caching fields preferences
        // getHashCodeCachingField().setEnabled(getCacheHashCodeField().getBooleanValue(), getFieldEditorParent());
        // getToStringCachingField().setEnabled(getCacheToStringField().getBooleanValue(), getFieldEditorParent());
    }

    @Override
    protected void checkState() {
        super.checkState();

        if (!isValid())
            return;

        if (!isFieldStringValueValid(getHashCodeCachingField().getStringValue())) {
            return;
        }

        if (!isFieldStringValueValid(getToStringCachingField().getStringValue())) {
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

    private boolean isFieldStringValueValid(String value) {
        IStatus status = JavaConventions.validateIdentifier(value);
        if (status.isOK()) {
            return true;
        }
        setErrorMessage(status.getMessage());
        setValid(false);
        return false;
    }
}
