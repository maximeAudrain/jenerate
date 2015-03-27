package org.jenerate.internal.ui.dialogs.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.data.CompareToDialogData;
import org.jenerate.internal.data.impl.CompareToDialogDataImpl;
import org.jenerate.internal.ui.preferences.PreferencesManager;

/**
 * @author maudrain
 */
public class CompareToDialog extends AbstractOrderableFieldDialog<CompareToDialogData> {

    public CompareToDialog(Shell parentShell, String dialogTitle, IType objectClass, IField[] fields,
            Set<IMethod> excludedMethods, boolean disableAppendSuper, PreferencesManager preferencesManager)
            throws JavaModelException {
        super(parentShell, dialogTitle, objectClass, fields, excludedMethods, disableAppendSuper, preferencesManager);
    }

    @Override
    public CompareToDialogData getData() {
        //@formatter:off
        return new CompareToDialogDataImpl.Builder()
                .withCheckedFields(getCheckedFields())
                .withElementPosition(getElementPosition())
                .withAppendSuper(getAppendSuper())
                .withGenerateComment(getGenerateComment())
                .withUseBlockInIfStatements(getUseBlockInIfStatements())
                .withUseGettersInsteadOfFields(getUseGettersInsteadOfFields())
                .build();
        //@formatter:on
    }

    @Override
    public Dialog getDialog() {
        return this;
    }
}
