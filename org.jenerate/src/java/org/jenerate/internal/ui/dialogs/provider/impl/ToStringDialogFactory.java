package org.jenerate.internal.ui.dialogs.provider.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.data.ToStringDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.dialogs.ToStringDialog;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class ToStringDialogFactory extends AbstractDialogFactory<ToStringDialog, ToStringDialogData> {

    public ToStringDialogFactory(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public ToStringDialog createDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods)
            throws Exception {
        IField[] fields = generatorsCommonMethodsDelegate.getObjectClassFields(objectClass, preferencesManager);

        boolean disableAppendSuper = getDisableAppendSuper(objectClass);
        return new ToStringDialog(parentShell, "Generate ToString Method", objectClass, fields, excludedMethods,
                disableAppendSuper, preferencesManager);
    }

    private boolean getDisableAppendSuper(IType objectClass) throws JavaModelException {
        return !isToStringConcreteInSuperclass(objectClass);
    }

    public boolean isToStringConcreteInSuperclass(final IType objectClass) throws JavaModelException {
        return generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "toString", new String[0], null);
    }

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.TO_STRING;
    }
}
