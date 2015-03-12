package org.jenerate.internal.ui.dialogs.provider.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.ui.dialogs.OrderableFieldDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class CompareToDialogProvider implements DialogProvider<OrderableFieldDialog>{

    @Override
    public OrderableFieldDialog getDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods,
            IField[] fields, boolean disableAppendSuper, PreferencesManager preferencesManager) throws JavaModelException {
        return new OrderableFieldDialog(parentShell, "Generate CompareTo Method",
                objectClass, fields, excludedMethods, disableAppendSuper, preferencesManager);
    }

}
