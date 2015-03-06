package org.jenerate.internal.ui.dialogs;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.lang.generators.CommonsLangLibraryUtils;

/**
 * @author jiayun
 */
public class ToStringDialog extends OrderableFieldDialog {

    private Combo styleCombo;

    private String toStringStyle;

    private IDialogSettings settings;

    private static final String SETTINGS_SECTION = "ToStringDialog";

    private static final String SETTINGS_STYLE = "ToStringStyle";

    public ToStringDialog(final Shell parentShell, final String dialogTitle, final IType objectClass,
            final IField[] fields, final Set<IMethod> excludedMethods, final boolean disableAppendSuper)
            throws JavaModelException {

        super(parentShell, dialogTitle, objectClass, fields, excludedMethods, disableAppendSuper);

        IDialogSettings dialogSettings = JeneratePlugin.getDefault().getDialogSettings();
        settings = dialogSettings.getSection(SETTINGS_SECTION);
        if (settings == null) {
            settings = dialogSettings.addNewSection(SETTINGS_SECTION);
        }

        toStringStyle = settings.get(SETTINGS_STYLE);
        if (toStringStyle == null) {
            toStringStyle = CommonsLangLibraryUtils.getToStringStyleLibraryDefaultStyle();
        } else {
            String[] splittedToStringStyle = toStringStyle.split("\\.");
            String chosenStyle = splittedToStringStyle[splittedToStringStyle.length - 1];
            toStringStyle = CommonsLangLibraryUtils.getToStringStyleLibrary() + CommonsLangLibraryUtils.DOT_STRING
                    + chosenStyle;
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
        styleCombo.setItems(CommonsLangLibraryUtils.createToStringStyles());
        styleCombo.setText(toStringStyle);

        data = new GridData(GridData.FILL_HORIZONTAL);
        styleCombo.setLayoutData(data);

        return composite;
    }

    public String getToStringStyle() {
        return toStringStyle;
    }
}
