package org.jenerate.internal.ui.dialogs.provider.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.dialogs.EqualsHashCodeDialog;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class EqualsHashCodeDialogProvider extends AbstractDialogProvider<EqualsHashCodeDialog, EqualsHashCodeDialogData> {

    public EqualsHashCodeDialogProvider(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public EqualsHashCodeDialog getDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods)
            throws Exception {
        IField[] fields = generatorsCommonMethodsDelegate.getObjectClassFields(objectClass, preferencesManager);
        boolean disableAppendSuper = getDisableAppendSuper(objectClass);
        return new EqualsHashCodeDialog(parentShell, "Generate Equals and HashCode", objectClass, fields,
                excludedMethods, disableAppendSuper, preferencesManager);
    }
    
    private boolean getDisableAppendSuper(IType objectClass) throws JavaModelException {
        return isDirectSubclassOfObject(objectClass) || !isEqualsOverriddenInSuperclass(objectClass)
                || !isHashCodeOverriddenInSuperclass(objectClass);
    }
    
    private boolean isDirectSubclassOfObject(final IType objectClass) throws JavaModelException {
        String superclass = objectClass.getSuperclassName();

        if (superclass == null) {
            return true;
        }
        if (superclass.equals("Object") || superclass.equals("java.lang.Object")) {
            return true;
        }

        return false;
    }
    
    public boolean isHashCodeOverriddenInSuperclass(final IType objectClass) throws JavaModelException {
        return generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "hashCode", new String[0],
                "java.lang.Object");
    }

    public boolean isEqualsOverriddenInSuperclass(final IType objectClass) throws JavaModelException {
        return generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "equals",
                new String[] { "QObject;" }, "java.lang.Object");
    }

}
