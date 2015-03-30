package org.jenerate.internal.ui.dialogs.factory.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;
import org.jenerate.internal.ui.dialogs.impl.ToStringDialog;

public class ToStringDialogFactory extends AbstractDialogFactory<ToStringDialog, ToStringGenerationData> {

    public ToStringDialogFactory(DialogFactoryHelper dialogFactoryHelper, PreferencesManager preferencesManager) {
        super(dialogFactoryHelper, preferencesManager);
    }

    @Override
    public ToStringDialog createDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods)
            throws Exception {
        IField[] fields = getObjectClassFields(objectClass);
        boolean disableAppendSuper = getDisableAppendSuper(objectClass);
        return new ToStringDialog(parentShell, "Generate ToString Method", objectClass, fields, excludedMethods,
                disableAppendSuper, preferencesManager, dialogFactoryHelper.getDialogSettings());
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
