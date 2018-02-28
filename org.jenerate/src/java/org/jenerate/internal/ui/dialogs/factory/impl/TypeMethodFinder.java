package org.jenerate.internal.ui.dialogs.factory.impl;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

public class TypeMethodFinder {

    /**
     * Find a method in the given type using name and parameters.
     * @param type to find the method in
     * @param methodName name of the method
     * @param methodParameters list of parameters, all parameters should be given using fully qualified name
     * @return found method, or null if it does not exist
     */
    public IMethod findMethodWithNameAndParameters(IType type, String methodName, String[] methodParameters) {
        IMethod[] methods;
        try {
            methods = type.getMethods();
        } catch (JavaModelException e) {
            return null;
        }
        for (int i = 0; i < methods.length; ++i) {
            IMethod method = methods[i];
            if (doesMethodNameMatch(method, methodName) && doesMethodParametersMatch(method, methodParameters)) {
                return method;
            }
        }
        return null;
    }

    private boolean doesMethodNameMatch(IMethod method, String methodName) {
        return method.getElementName().equals(methodName);
    }

    private boolean doesMethodParametersMatch(IMethod method, String[] expectedMethodParameters) {
        String[] actualMethodParameters = method.getParameterTypes();
        if (actualMethodParameters.length != expectedMethodParameters.length) {
            return false;
        }
        for (int i = 0; i < actualMethodParameters.length; ++i) {
            if (!doesParameterMatch(actualMethodParameters[i], expectedMethodParameters[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean doesParameterMatch(String signature, String fqn) {
        String parameterType = Signature.toString(signature);
        return fullyQualifiedNameMatch(parameterType, fqn) || simpleNameMatch(parameterType, fqn);
    }

    private boolean fullyQualifiedNameMatch(String name, String fqn) {
        return name.equals(fqn);
    }

    private boolean simpleNameMatch(String signature, String fqn) {
        String[] parts = fqn.split("\\.");
        return signature.equals(parts[parts.length - 1]);
    }

}
