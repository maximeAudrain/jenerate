package org.jenerate.internal.strategy.method.content.impl;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.strategy.method.content.MethodContent;

/**
 * Utility class that contains common methods for the {@link MethodContent} code generation
 * 
 * @author maudrain
 */
public final class MethodContentGenerations {

    public MethodContentGenerations() {
        /* Only static helper methods */
    }

    /**
     * Create a transient field with a certain name and type in the {@link IType} objectClass. It checks first if the
     * cache property allows this field to be created. If the field with the same name exists, it is deleted prior to
     * the new field creation.
     * 
     * @param objectClass the {@link IType} where the field will be created
     * @param data the data used for the code generation
     * @param cacheProperty {@code true} if the field caching is allowed, {@code false} otherwise.
     * @param cachingFieldName the name of the field to be created
     * @param cachingFieldType the type of the field to be created
     * @return {@code true} if the field was created, {@code false} otherwise
     * @throws JavaModelException if an problem occurs during the code generation.
     */
    public static boolean createField(IType objectClass, MethodGenerationData data, boolean cacheProperty,
            String cachingFieldName, Class<?> cachingFieldType) throws JavaModelException {
        IField field = objectClass.getField(cachingFieldName);
        if (field.exists()) {
            field.delete(true, null);
        }
        boolean isCacheable = cacheProperty && areAllFinalFields(data.getCheckedFields());
        if (isCacheable) {
            IJavaElement currentPosition = data.getElementPosition();
            String fieldSrc = "private transient " + cachingFieldType.getSimpleName() + " " + cachingFieldName
                    + ";\n\n";
            objectClass.createField(fieldSrc, currentPosition, true, null);
        }
        return isCacheable;
    }

    private static boolean areAllFinalFields(IField[] fields) throws JavaModelException {
        for (int i = 0; i < fields.length; i++) {
            if (!Flags.isFinal(fields[i].getFlags())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the field accessor string if the useGettersInsteadOfFields parameter is {@code true}, otherwise returns
     * the field name directly. This method assumes that the accessor has a standard name e.g 'getField()' in case the
     * field name is 'field'.
     * 
     * @param field the field to extract information from
     * @param useGettersInsteadOfFields {@code true} if the field accessor should be returned, {@code false} if the
     *            field name should be returned
     * @return the field accessor if the useGettersInsteadOfFields parameter is {@code true}, otherwise the field name
     *         directly
     * @throws JavaModelException if an problem occurs while retrieving field information
     */
    public static String getFieldAccessorString(final IField field, final boolean useGettersInsteadOfFields)
            throws JavaModelException {
        if (useGettersInsteadOfFields) {
            return generateGetter(field);
        }
        return field.getElementName();

    }

    private static String generateGetter(final IField field) throws JavaModelException {
        String elementName = field.getElementName();
        if (isFieldABoolean(field)) {
            return "is" + upperCaseFirst(elementName + "()");
        }
        return "get" + upperCaseFirst(elementName + "()");

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
