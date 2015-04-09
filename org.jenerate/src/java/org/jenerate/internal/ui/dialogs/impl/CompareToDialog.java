package org.jenerate.internal.ui.dialogs.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.data.impl.CompareToGenerationDataImpl;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;

/**
 * @author maudrain
 */
public class CompareToDialog extends AbstractOrderableFieldDialog<CompareToGenerationData> {

    public CompareToDialog(Shell parentShell, String dialogTitle, IType objectClass, IField[] fields,
            Set<IMethod> excludedMethods, LinkedHashSet<StrategyIdentifier> possibleStrategies,
            boolean disableAppendSuper, PreferencesManager preferencesManager, IDialogSettings dialogSettings)
            throws JavaModelException {
        super(parentShell, dialogTitle, objectClass, fields, excludedMethods, possibleStrategies, disableAppendSuper,
                preferencesManager, dialogSettings);
    }

    @Override
    public CompareToGenerationData getData() {
        //@formatter:off
        return new CompareToGenerationDataImpl.Builder()
                .withCheckedFields(getCheckedFields())
                .withSelectedContentStrategy(getStrategyIdentifier())
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
