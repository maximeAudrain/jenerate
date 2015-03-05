// $Id$
package org.jenerate.internal.ui.preferences;

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

/**
 * @author jiayun
 */
public class JenerateBasePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private BooleanFieldEditor useCommonsLang3;

    private BooleanFieldEditor cacheHashCode;

    private StringFieldEditor hashCodeField;

    private BooleanFieldEditor cacheToString;

    private StringFieldEditor toStringField;

    private BooleanFieldEditor addOverrideAnnotation;

    private BooleanFieldEditor generifyCompareTo;

    private BooleanFieldEditor displayFieldsOfSuperclasses;

    private BooleanFieldEditor useGettersInsteadOfFields;

    private BooleanFieldEditor useBlocksInIfStatements;

    public JenerateBasePreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        setPreferenceStore(JeneratePlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        useCommonsLang3 = new BooleanFieldEditor(PreferenceConstants.USE_COMMONS_LANG3,
                "&Import commons-lang3 for all code generation", getFieldEditorParent());
        addField(useCommonsLang3);

        cacheHashCode = new BooleanFieldEditor(PreferenceConstants.CACHE_HASHCODE,
                "Cache &hashCode when all selected fields are final", getFieldEditorParent());
        addField(cacheHashCode);

        hashCodeField = new StringFieldEditor(PreferenceConstants.HASHCODE_CACHING_FIELD, "Hash&Code caching field",
                getFieldEditorParent());
        addField(hashCodeField);

        cacheToString = new BooleanFieldEditor(PreferenceConstants.CACHE_TOSTRING,
                "Cache &toString when all selected fields are final", getFieldEditorParent());
        addField(cacheToString);

        toStringField = new StringFieldEditor(PreferenceConstants.TOSTRING_CACHING_FIELD, "To&String caching field",
                getFieldEditorParent());
        addField(toStringField);

        addOverrideAnnotation = new BooleanFieldEditor(PreferenceConstants.ADD_OVERRIDE_ANNOTATION,
                "Add @&Override when the source compatibility is 5.0 or above", getFieldEditorParent());
        addField(addOverrideAnnotation);

        generifyCompareTo = new BooleanFieldEditor(PreferenceConstants.GENERIFY_COMPARETO,
                "&Generify compareTo when the source compatibility is 5.0 or above", getFieldEditorParent());
        addField(generifyCompareTo);

        displayFieldsOfSuperclasses = new BooleanFieldEditor(PreferenceConstants.DISPLAY_FIELDS_OF_SUPERCLASSES,
                "&Display fields of superclasses", getFieldEditorParent());
        addField(displayFieldsOfSuperclasses);

        useGettersInsteadOfFields = new BooleanFieldEditor(PreferenceConstants.USE_GETTERS_INSTEAD_OF_FIELDS,
                "&Use getters instead of fields (for Hibernate)", getFieldEditorParent());
        addField(useGettersInsteadOfFields);

        useBlocksInIfStatements = new BooleanFieldEditor(PreferenceConstants.USE_BLOCKS_IN_IF_STATEMENTS,
                "&Use blocks in 'if' statments", getFieldEditorParent());
        addField(useBlocksInIfStatements);
    }

    @Override
    protected void checkState() {
        super.checkState();

        if (!isValid())
            return;

        IStatus status = JavaConventions.validateIdentifier(hashCodeField.getStringValue());
        if (!status.isOK()) {
            setErrorMessage(status.getMessage());
            setValid(false);
            return;
        }

        status = JavaConventions.validateIdentifier(toStringField.getStringValue());
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

            if (event.getSource() == cacheHashCode) {
                hashCodeField.setEnabled(cacheHashCode.getBooleanValue(), getFieldEditorParent());

            } else if (event.getSource() == cacheToString) {
                toStringField.setEnabled(cacheToString.getBooleanValue(), getFieldEditorParent());

            } else if (event.getSource() == hashCodeField || event.getSource() == toStringField) {
                checkState();
            }
        }
    }

    @Override
    public void init(IWorkbench workbench) {
        /* Nothing to be done here */
    }
}
