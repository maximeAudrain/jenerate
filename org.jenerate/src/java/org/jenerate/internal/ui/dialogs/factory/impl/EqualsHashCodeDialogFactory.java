package org.jenerate.internal.ui.dialogs.factory.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.impl.EqualsHashCodeDialog;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;

public class EqualsHashCodeDialogFactory extends AbstractDialogFactory<EqualsHashCodeDialog, EqualsHashCodeGenerationData> {

    public EqualsHashCodeDialogFactory(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public EqualsHashCodeDialog createDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods)
            throws Exception {
        IField[] fields = generatorsCommonMethodsDelegate.getObjectClassFields(objectClass, preferencesManager);
        boolean disableAppendSuper = getDisableAppendSuper(objectClass);
        return new EqualsHashCodeDialog(parentShell, "Generate Equals and HashCode Methods", objectClass, fields,
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

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.EQUALS_HASH_CODE;
    }

}
