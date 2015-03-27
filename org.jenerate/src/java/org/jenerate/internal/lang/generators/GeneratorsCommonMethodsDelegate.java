package org.jenerate.internal.lang.generators;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.ui.preferences.PreferencesManager;

/**
 * XXX get rid of me once the code is tested and the strategy pattern in effect
 * 
 * @author maudrain
 */
public interface GeneratorsCommonMethodsDelegate {

    /**
     * Check if the method specified in the methodName and methodParameterTypeSignatures parameters is overridden and
     * concrete in a subclass of the original declared class, and that subclass is a superclass of objectClass.
     * 
     * @param objectClass
     * @param methodName
     * @param methodParameterTypeSignatures
     * @param originalClassFullyQualifiedName
     * @return
     * @throws JavaModelException
     */
    boolean isOverriddenInSuperclass(final IType objectClass, final String methodName,
            final String[] methodParameterTypeSignatures, final String originalClassFullyQualifiedName)
            throws JavaModelException;

    boolean areAllFinalFields(final IField[] fields) throws JavaModelException;

    boolean isSourceLevelGreaterThanOrEqualTo5(IType objectClass);

    IField[] getObjectClassFields(IType objectClass, PreferencesManager preferencesManager) throws JavaModelException;

}
