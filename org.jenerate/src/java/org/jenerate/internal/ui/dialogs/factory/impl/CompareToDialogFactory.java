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
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.DialogStrategyManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;
import org.jenerate.internal.ui.dialogs.impl.OrderableFieldDialogImpl;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

/**
 * {@link DialogFactory} implementation for the {@link CompareToDialog}
 *
 * @author maudrain
 */
public class CompareToDialogFactory extends AbstractDialogFactory<CompareToGenerationData> {

    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    /**
     * Constructor
     *
     * @param dialogFactoryHelper the dialog factory helper
     * @param preferencesManager the preference manager
     * @param javaInterfaceCodeAppender the java interface code appender
     */
    public CompareToDialogFactory(DialogStrategyManager dialogStrategyManager, DialogFactoryHelper dialogFactoryHelper,
            PreferencesManager preferencesManager, JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(dialogStrategyManager, dialogFactoryHelper, preferencesManager);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public OrderableFieldDialogImpl<CompareToGenerationData> createDialog(Shell parentShell, IType objectClass,
            Set<IMethod> excludedMethods, LinkedHashSet<StrategyIdentifier> possibleStrategies) throws Exception {
        IField[] fields = getObjectClassFields(objectClass);
        boolean disableAppendSuper = getDisableAppendSuper(objectClass);
        LinkedHashMap<String, IJavaElement> insertPositions = dialogFactoryHelper.getInsertPositions(objectClass,
                excludedMethods);
        return new OrderableFieldDialogImpl<CompareToGenerationData>(MethodsGenerationCommandIdentifier.COMPARE_TO,
                parentShell, "Generate CompareTo Method", fields, possibleStrategies, disableAppendSuper,
                preferencesManager, dialogFactoryHelper.getDialogSettings(), insertPositions, dialogStrategyManager);
    }

    @Override
    public CommandIdentifier getCommandIdentifier() {
        return MethodsGenerationCommandIdentifier.COMPARE_TO;
    }

    private boolean getDisableAppendSuper(IType objectClass) throws JavaModelException {
        return !(javaInterfaceCodeAppender.isImplementedInSupertype(objectClass, "Comparable")
                && isCompareToImplementedInSuperclass(objectClass));
    }

    private boolean isCompareToImplementedInSuperclass(final IType objectClass) throws JavaModelException {
        return dialogFactoryHelper.isOverriddenInSuperclass(objectClass, "compareTo", new String[] { "java.lang.Object" },
                null);
    }
}
