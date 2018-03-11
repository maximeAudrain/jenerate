package org.jenerate.internal.ui.dialogs.factory.impl;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

/**
 * Finds a method based on name and parameters.
 * 
 * @author helospark
 */
public class MethodFinder {
    private static final String FULLY_QUALIFIED_NAME_SEPARATOR_PATTERN = "\\.";
    private static final String FULLY_QUALIFIED_NAME_SEPARATOR = ".";

    /**
     * Find a method in the given type using name and parameters.
     * 
     * @param type to find the method in
     * @param methodName name of the method
     * @param methodParameterTypes list of parameters, all parameters should be given using fully qualified name
     * @return found method, or null if it does not exist
     */
    public IMethod findMethodWithNameAndParameters(IType type, String methodName, String[] methodParameterTypes)
            throws JavaModelException {
        IMethod[] methods = type.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            IMethod method = methods[i];
            if (doesMethodNameMatch(method, methodName) && doesMethodParametersMatch(method, methodParameterTypes)) {
                return method;
            }
        }
        return null;
    }

    private boolean doesMethodNameMatch(IMethod method, String methodName) {
        return method.getElementName().equals(methodName);
    }

    private boolean doesMethodParametersMatch(IMethod method, String[] expectedMethodParameterTypes) {
        String[] actualMethodParameters = method.getParameterTypes();
        if (actualMethodParameters.length != expectedMethodParameterTypes.length) {
            return false;
        }
        for (int i = 0; i < actualMethodParameters.length; ++i) {
            if (!doesParameterMatch(actualMethodParameters[i], expectedMethodParameterTypes[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean doesParameterMatch(String signature, String methodTypeFullyQualifiedName) {
        String actualParameterType = Signature.toString(signature);
        return isTypeFullyQualified(actualParameterType)
                ? fullyQualifiedTypeNameMatch(actualParameterType, methodTypeFullyQualifiedName)
                : simpleTypeNameMatch(actualParameterType, methodTypeFullyQualifiedName);
    }

    private boolean isTypeFullyQualified(String parameterType) {
        return parameterType.contains(FULLY_QUALIFIED_NAME_SEPARATOR);
    }

    private boolean fullyQualifiedTypeNameMatch(String actualTypeName, String expectedTypeName) {
        return actualTypeName.equals(expectedTypeName);
    }

    private boolean simpleTypeNameMatch(String actualSimpleTypeName, String expectedFullyQualifiedTypeName) {
        String[] parts = expectedFullyQualifiedTypeName.split(FULLY_QUALIFIED_NAME_SEPARATOR_PATTERN);
        String expectedSimpleTypeName = parts[parts.length - 1];
        return actualSimpleTypeName.equals(expectedSimpleTypeName);
    }

}
