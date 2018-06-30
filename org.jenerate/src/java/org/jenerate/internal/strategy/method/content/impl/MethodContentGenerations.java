package org.jenerate.internal.strategy.method.content.impl;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.strategy.method.content.MethodContent;

/**
 * Utility class that contains common methods for the {@link MethodContent} code
 * generation
 * 
 * @author maudrain
 */
public final class MethodContentGenerations {
    private static final Set<String> PRIMITIVE_SIGNATURES = new HashSet<>(asList(
            Signature.SIG_BOOLEAN, 
            Signature.SIG_BYTE, 
            Signature.SIG_CHAR, 
            Signature.SIG_SHORT,
            Signature.SIG_INT,
            Signature.SIG_LONG,
            Signature.SIG_FLOAT,
            Signature.SIG_DOUBLE));

    public MethodContentGenerations() {
        /* Only static helper methods */
    }

    /**
     * Utility method to create a {@link LinkedHashSet} of a singleton element
     * 
     * @param singletonElement the singleton element to add to the hash set
     * @return the linked hash set containing the singleton element
     */
    public static <T> LinkedHashSet<T> createSingletonLinkedHashSet(T singletonElement) {
        LinkedHashSet<T> toReturn = new LinkedHashSet<>();
        toReturn.add(singletonElement);
        return toReturn;
    }

    /**
     * Creates the equals method content prefix
     * 
     * @param data        the data to extract configuration from
     * @param objectClass the class where the equals method is being generated
     * @return the equals method prefix
     */
    public static String createEqualsContentPrefix(EqualsHashCodeGenerationData data, IType objectClass) {
        StringBuffer content = new StringBuffer();
        boolean useBlockInIfStatements = data.useBlockInIfStatements();
        if (data.compareReferences()) {
            content.append("if (this == other)");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return true;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
        }

        if (data.useClassComparison()) {
            content.append("if (other == null)");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return false;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
            content.append("if ( !getClass().equals(other.getClass()))");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return false;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
        } else if (data.useClassCast()) {
            content.append("if ( !");
            content.append(objectClass.getElementName());
            content.append(".class.isInstance(other)");
            content.append(")");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return false;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
        } else {
            content.append("if ( !(other instanceof ");
            content.append(objectClass.getElementName());
            content.append(") )");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return false;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
        }
        return content.toString();
    }

    /**
     * Creates the equals method content given a certain equals method name for delegation
     * 
     * @param equalsMethodName the equals method name that process the equality
     * @param data the data to extract configuration from
     * @param objectClass the class where the equals method is being generated
     * @return the equals method content
     * @throws JavaModelException if a problem occurs during the code generation.
     */
    public static String createEqualsContent(String equalsMethodName, EqualsHashCodeGenerationData data,
            IType objectClass) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        boolean useBlockInIfStatements = data.useBlockInIfStatements();
        String elementName = objectClass.getElementName();
        if (data.appendSuper()) {
            content.append("if (!super.equals(other))");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return false;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
        }
        if (data.useClassCast()) {
            content.append(elementName);
            content.append(" castOther = ");
            content.append(elementName);
            content.append(".class.cast(other);\n");
        } else {
            content.append(elementName);
            content.append(" castOther = (");
            content.append(elementName);
            content.append(") other;\n");
        }
        content.append("return ");
        String prefix = "";
        IField[] checkedFields = data.getCheckedFields();
        for (IField checkedField : checkedFields) {
            content.append(prefix);
            prefix = " && ";
            String fieldName = MethodContentGenerations.getFieldAccessorString(checkedField,
                    data.useGettersInsteadOfFields());
            
            if(isMultiDimensionalArray(checkedField) && data.useDeepArrayComparison()) {
                content.append("Arrays.deepEquals(");
                content.append(fieldName);
                content.append(", castOther.");
                content.append(fieldName);
                content.append(")");
            } else if(isArray(checkedField) && data.useDeepArrayComparison()) {
                content.append("Arrays.equals(");
                content.append(fieldName);
                content.append(", castOther.");
                content.append(fieldName);
                content.append(")");
            } else if(isPrimitiveType(checkedField) && data.useSimplePrimitiveComparison()) {
                content.append("(");
                content.append(fieldName);
                content.append(" == ");
                content.append("castOther.");
                content.append(fieldName);
                content.append(")");
            } else {
                content.append("Objects.");
                content.append(equalsMethodName);
                content.append("(");
                content.append(fieldName);
                content.append(", castOther.");
                content.append(fieldName);
                content.append(")");
            }
        }
        content.append(";\n");
        return content.toString();
    }

    /**
     * Creates the hashCode method content given a certain hashCode method name for
     * delegation
     * 
     * @param hashCodeMethodName the hashCode method name that process the hash
     * @param data               the data to extract configuration from
     * @return the hashCode method content
     * @throws JavaModelException if a problem occurs during the code generation.
     */
    public static String createHashCodeContent(String hashCodeMethodName, EqualsHashCodeGenerationData data)
            throws JavaModelException {
        StringBuffer content = new StringBuffer();
        content.append("return Objects.");
        content.append(hashCodeMethodName);
        content.append("(");
        if (data.appendSuper()) {
            content.append("super.hashCode(), ");
        }
        IField[] checkedFields = data.getCheckedFields();
        String prefix = "";
        for (IField checkedField : checkedFields) {
            content.append(prefix);
            prefix = ", ";
            String field = MethodContentGenerations.getFieldAccessorString(checkedField, data.useGettersInsteadOfFields());
            if(isMultiDimensionalArray(checkedField) && data.useDeepArrayComparison())
                content.append("Arrays.deepHashCode(").append(field).append(")");
            else if(isArray(checkedField) && data.useDeepArrayComparison())
                content.append("Arrays.hashCode(").append(field).append(")");
            else
                content.append(field);
        }
        content.append(");\n");
        return content.toString();
    }

    /**
     * Create a transient field with a certain name and type in the {@link IType}
     * objectClass. It checks first if the cache property allows this field to be
     * created. If the field with the same name exists, it is deleted prior to the
     * new field creation.
     * 
     * @param objectClass      the {@link IType} where the field will be created
     * @param data             the data used for the code generation
     * @param cacheProperty    {@code true} if the field caching is allowed,
     *                         {@code false} otherwise.
     * @param cachingFieldName the name of the field to be created
     * @param cachingFieldType the type of the field to be created
     * @return {@code true} if the field was created, {@code false} otherwise
     * @throws JavaModelException if a problem occurs during the code generation.
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
     * Returns the field accessor string if the useGettersInsteadOfFields parameter
     * is {@code true}, otherwise returns the field name directly. This method
     * assumes that the accessor has a standard name e.g 'getField()' in case the
     * field name is 'field'.
     * 
     * @param field                     the field to extract information from
     * @param useGettersInsteadOfFields {@code true} if the field accessor should be
     *                                  returned, {@code false} if the field name
     *                                  should be returned
     * @return the field accessor if the useGettersInsteadOfFields parameter is
     *         {@code true}, otherwise the field name directly
     * @throws JavaModelException if an problem occurs while retrieving field
     *                            information
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

    private static boolean isPrimitiveType(IField checkedField) throws JavaModelException {
        return PRIMITIVE_SIGNATURES.contains(checkedField.getTypeSignature());
    }

    private static boolean isArray(IField checkedField) throws JavaModelException {
        return Signature.getArrayCount(checkedField.getTypeSignature()) == 1;
    }

    private static boolean isMultiDimensionalArray(IField checkedField) throws JavaModelException {
        return Signature.getArrayCount(checkedField.getTypeSignature()) > 1;
    }    
}
