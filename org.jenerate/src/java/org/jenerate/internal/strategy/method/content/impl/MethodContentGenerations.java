package org.jenerate.internal.strategy.method.content.impl;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

/**
 * Utility class that generates the method strings given certain parameters
 * 
 * @author maudrain
 */
public final class MethodContentGenerations {

    public MethodContentGenerations() {
        /* Only static helper methods */
    }

    /**
     * @param field
     * @param useGettersInsteadOfFields
     * @return
     * @throws JavaModelException
     */
    public static String generateFieldAccessor(final IField field, final boolean useGettersInsteadOfFields)
            throws JavaModelException {
        if (useGettersInsteadOfFields) {
            return generateGetter(field);
        }
        return field.getElementName();

    }

    private static String generateGetter(final IField field) throws JavaModelException {
        if (isFieldABoolean(field)) {
            return "is" + upperCaseFirst(field.getElementName() + "()");
        }
        return "get" + upperCaseFirst(field.getElementName() + "()");

    }

    private static boolean isFieldABoolean(final IField field) throws JavaModelException {
        return Signature.SIG_BOOLEAN.equals(field.getTypeSignature());
    }

    private static String upperCaseFirst(final String string) {
        String firstChar = string.substring(0, 1);
        String remain = string.substring(1);
        return firstChar.toUpperCase() + remain;
    }

}
