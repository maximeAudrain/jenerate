package org.jenerate.internal.ui.dialogs.factory;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.manage.PreferencesManager;

public interface DialogFactoryHelper {

    /**
     * Check if the method specified with methodName and methodParameterTypeSignatures parameters is overridden and
     * concrete in a subclass of the original declared class, and that subclass is a superclass of objectClass.
     * 
     * @param objectClass
     * @param methodName
     * @param methodParameterTypeSignatures
     * @param originalClassFullyQualifiedName
     * @return
     * @throws JavaModelException
     */
    public boolean isOverriddenInSuperclass(IType objectClass, String methodName,
            String[] methodParameterTypeSignatures, String originalClassFullyQualifiedName) throws JavaModelException;

    public IField[] getObjectClassFields(IType objectClass, PreferencesManager preferencesManager)
            throws JavaModelException;
}
