package org.jenerate.internal.ui.dialogs.provider.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.data.CompareToDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.dialogs.CompareToDialog;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class CompareToDialogProvider extends AbstractDialogProvider<CompareToDialog, CompareToDialogData> {

    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CompareToDialogProvider(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public CompareToDialog getDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods)
            throws Exception {
        IField[] fields = generatorsCommonMethodsDelegate.getObjectClassFields(objectClass, preferencesManager);
        boolean disableAppendSuper = getDisableAppendSuper(objectClass);
        return new CompareToDialog(parentShell, "Generate CompareTo MethodSkeleton", objectClass, fields, excludedMethods,
                disableAppendSuper, preferencesManager);
    }

    private boolean getDisableAppendSuper(IType objectClass) throws JavaModelException {
        return !(javaInterfaceCodeAppender.isImplementedInSupertype(objectClass, "Comparable") && isCompareToImplementedInSuperclass(objectClass));
    }

    public boolean isCompareToImplementedInSuperclass(final IType objectClass) throws JavaModelException {
        return generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "compareTo",
                new String[] { "QObject;" }, null);
    }
    
    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.COMPARE_TO;
    }

}
