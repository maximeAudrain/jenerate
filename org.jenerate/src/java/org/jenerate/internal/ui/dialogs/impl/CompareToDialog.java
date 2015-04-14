package org.jenerate.internal.ui.dialogs.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.data.impl.CompareToGenerationDataImpl;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;

/**
 * Default implementation of the dialog for the generation of the compareTo method. Does not define any additional GUI
 * components than the {@link AbstractOrderableFieldDialog}
 * 
 * @author maudrain
 */
public class CompareToDialog extends AbstractOrderableFieldDialog<CompareToGenerationData> {

    public CompareToDialog(Shell parentShell, String dialogTitle, IField[] fields,
            LinkedHashSet<StrategyIdentifier> possibleStrategies, boolean disableAppendSuper,
            PreferencesManager preferencesManager, IDialogSettings dialogSettings,
            LinkedHashMap<String, IJavaElement> insertPositions) {
        super(parentShell, dialogTitle, fields, possibleStrategies, disableAppendSuper, preferencesManager,
                dialogSettings, insertPositions);
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

    @Override
    public void callbackAfterStrategyChanged(StrategyIdentifier currentStrategy) {
        // TODO Auto-generated method stub
        
    }
}
