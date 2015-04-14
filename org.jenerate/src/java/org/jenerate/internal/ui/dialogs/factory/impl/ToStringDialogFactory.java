package org.jenerate.internal.ui.dialogs.factory.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.DialogStrategyManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;
import org.jenerate.internal.ui.dialogs.impl.OrderableFieldDialogImpl;

/**
 * {@link DialogFactory} implementation for the {@link ToStringDialog}
 * 
 * @author maudrain
 */
public class ToStringDialogFactory extends AbstractDialogFactory<ToStringGenerationData> {

    /**
     * Constructor
     * 
     * @param dialogFactoryHelper the dialog factory helper
     * @param preferencesManager the preference manager
     */
    public ToStringDialogFactory(DialogStrategyManager dialogStrategyManager, DialogFactoryHelper dialogFactoryHelper,
            PreferencesManager preferencesManager) {
        super(dialogStrategyManager, dialogFactoryHelper, preferencesManager);
    }

    @Override
    public FieldDialog<ToStringGenerationData> createDialog(Shell parentShell, IType objectClass,
            Set<IMethod> excludedMethods, LinkedHashSet<StrategyIdentifier> possibleStrategies) throws Exception {
        IField[] fields = getObjectClassFields(objectClass);
        boolean disableAppendSuper = getDisableAppendSuper(objectClass);
        LinkedHashMap<String, IJavaElement> insertPositions = dialogFactoryHelper.getInsertPositions(objectClass,
                excludedMethods);
        return new OrderableFieldDialogImpl<ToStringGenerationData>(MethodsGenerationCommandIdentifier.TO_STRING,
                parentShell, "Generate ToString Method", fields, possibleStrategies, disableAppendSuper,
                preferencesManager, dialogFactoryHelper.getDialogSettings(), insertPositions, dialogStrategyManager);
    }

    @Override
    public CommandIdentifier getCommandIdentifier() {
        return MethodsGenerationCommandIdentifier.TO_STRING;
    }

    private boolean getDisableAppendSuper(IType objectClass) throws JavaModelException {
        return !isToStringConcreteInSuperclass(objectClass);
    }

    private boolean isToStringConcreteInSuperclass(final IType objectClass) throws JavaModelException {
        return dialogFactoryHelper.isOverriddenInSuperclass(objectClass, "toString", new String[0], null);
    }
}
