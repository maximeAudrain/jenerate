package org.jenerate.internal.ui.dialogs.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.data.ToStringDialogData;
import org.jenerate.internal.data.impl.ToStringDialogDataImpl;
import org.jenerate.internal.domain.method.content.tostring.ToStringStyle;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

/**
 * @author jiayun
 */
public class ToStringDialog extends AbstractOrderableFieldDialog<ToStringDialogData> {

    private Combo styleCombo;

    private String toStringStyle;

    private IDialogSettings settings;

    private static final String SETTINGS_SECTION = "ToStringDialog";

    private static final String SETTINGS_STYLE = "ToStringStyle";

    public ToStringDialog(final Shell parentShell, final String dialogTitle, final IType objectClass,
            final IField[] fields, final Set<IMethod> excludedMethods, final boolean disableAppendSuper,
            PreferencesManager preferencesManager) throws JavaModelException {

        super(parentShell, dialogTitle, objectClass, fields, excludedMethods, disableAppendSuper, preferencesManager);

        IDialogSettings dialogSettings = JeneratePlugin.getDefault().getDialogSettings();
        settings = dialogSettings.getSection(SETTINGS_SECTION);
        if (settings == null) {
            settings = dialogSettings.addNewSection(SETTINGS_SECTION);
        }

        toStringStyle = settings.get(SETTINGS_STYLE);
        if (toStringStyle == null) {
            toStringStyle = ToStringStyle.NO_STYLE.getFullLibraryString((Boolean) getPreferencesManager()
                    .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3));
        }
    }

    @Override
    public boolean close() {
        toStringStyle = styleCombo.getText();
        settings.put(SETTINGS_STYLE, toStringStyle);
        return super.close();
    }

    @Override
    protected Composite createOptionComposite(Composite composite) {
        Composite optionComposite = super.createOptionComposite(composite);
        addStyleChoices(optionComposite);
        return optionComposite;
    }

    private Composite addStyleChoices(final Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText("&ToString style:");

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        label.setLayoutData(data);

        styleCombo = new Combo(composite, SWT.NONE);
        Boolean useCommonsLang3 = (Boolean) getPreferencesManager().getCurrentPreferenceValue(
                JeneratePreference.USE_COMMONS_LANG3);
        List<String> styles = new ArrayList<String>();
        for (ToStringStyle style : ToStringStyle.values()) {
            if (ToStringStyle.NO_STYLE.equals(style)) {
                styles.add(ToStringStyle.NO_STYLE.name());
            } else {
                styles.add(style.getFullLibraryString(useCommonsLang3));
            }
        }
        styleCombo.setItems(styles.toArray(new String[styles.size()]));
        styleCombo.setText(toStringStyle);

        data = new GridData(GridData.FILL_HORIZONTAL);
        styleCombo.setLayoutData(data);

        return composite;
    }

    @Override
    public ToStringDialogData getData() {
        //@formatter:off
        return new ToStringDialogDataImpl.Builder()
                .withCheckedFields(getCheckedFields())
                .withElementPosition(getElementPosition())
                .withAppendSuper(getAppendSuper())
                .withGenerateComment(getGenerateComment())
                .withUseBlockInIfStatements(getUseBlockInIfStatements())
                .withUseGettersInsteadOfFields(getUseGettersInsteadOfFields())
                .withToStringStyle(ToStringStyle.getToStringStyle(toStringStyle))
                .build();
        //@formatter:on
    }

    @Override
    public Dialog getDialog() {
        return this;
    }
}
