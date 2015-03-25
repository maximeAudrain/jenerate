package org.jenerate.internal.lang;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.jenerate.internal.data.CompareToDialogData;
import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.data.ToStringDialogData;

/**
 * Utility class that generates the method strings given certain parameters
 * 
 * @author maudrain
 */
public final class MethodGenerations {

    public MethodGenerations() {
        /* Only static helper methods */
    }

    public static String createCompareToMethod(IType objectClass, CompareToDialogData data, boolean generify) {

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Comparable#compareTo(java.lang.Object)\n");
            content.append(" */\n");
        }
        String other;
        String className = objectClass.getElementName();
        if (generify) {
            content.append("public int compareTo(final " + className + " other) {\n");

            other = "other";
        } else {
            content.append("public int compareTo(final Object other) {\n");
            content.append(className);
            content.append(" castOther = (");
            content.append(className);
            content.append(") other;\n");

            other = "castOther";
        }
        content.append("return new CompareToBuilder()");
        if (data.getAppendSuper()) {
            content.append(".appendSuper(super.compareTo(other))");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            String fieldName = checkedFields[i].getElementName();
            content.append(".append(");
            content.append(fieldName);
            content.append(", " + other + ".");
            content.append(fieldName);
            content.append(")");
        }
        content.append(".toComparison();\n");
        content.append("}\n\n");

        return content.toString();
    }

    public static String createEqualsMethod(IType objectClass, EqualsHashCodeDialogData data, boolean addOverride)
            throws JavaModelException {

        boolean useBlockInIfStatements = data.getUseBlockInIfStatements();
        String elementName = objectClass.getElementName();

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#equals(java.lang.Object)\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public boolean equals(final Object other) {\n");
        if (data.getCompareReferences()) {
            content.append("if (this == other)");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return true;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
        }
        content.append("if ( !(other instanceof ");
        content.append(elementName);
        content.append(") )");
        content.append(useBlockInIfStatements ? "{\n" : "");
        content.append(" return false;");
        content.append(useBlockInIfStatements ? "\n}\n" : "");
        content.append(elementName);
        content.append(" castOther = (");
        content.append(elementName);
        content.append(") other;\n");
        content.append("return new EqualsBuilder()");
        if (data.getAppendSuper()) {
            content.append(".appendSuper(super.equals(other))");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            String fieldName = generateFieldAccessor(checkedFields[i], data.getUseGettersInsteadOfFields());
            content.append(fieldName);
            content.append(", castOther.");
            content.append(fieldName);
            content.append(")");
        }
        content.append(".isEquals();\n");
        content.append("}\n\n");

        return content.toString();
    }

    public static String createHashCodeMethod(EqualsHashCodeDialogData data, String cachingField, boolean addOverride)
            throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#hashCode()\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public int hashCode() {\n");

        String hashCodeBuilderString = createHashCodeBuilderString(data);

        if (cachingField.isEmpty()) {
            content.append("return ");
            content.append(hashCodeBuilderString);
        } else {
            content.append("if (" + cachingField + "== 0) {\n");
            content.append(cachingField + " = ");
            content.append(hashCodeBuilderString);
            content.append("}\n");
            content.append("return " + cachingField + ";\n");
        }
        content.append("}\n\n");

        return content.toString();
    }

    private static String createHashCodeBuilderString(EqualsHashCodeDialogData data) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        content.append("new HashCodeBuilder(");
        content.append(data.getInitMultNumbers().getValue());
        content.append(")");
        if (data.getAppendSuper()) {
            content.append(".appendSuper(super.hashCode())");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(generateFieldAccessor(checkedFields[i], data.getUseGettersInsteadOfFields()));
            content.append(")");
        }
        content.append(".toHashCode();\n");

        return content.toString();
    }

    public static String createToStringMethod(ToStringDialogData data, String styleConstant, String cachingField,
            boolean addOverride) throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#toString()\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public String toString() {\n");
        String toStringBuilderString = createToStringBuilderString(data, styleConstant);

        if (cachingField.isEmpty()) {
            content.append("return ");
            content.append(toStringBuilderString);

        } else {
            content.append("if (" + cachingField + "== null) {\n");
            content.append(cachingField + " = ");
            content.append(toStringBuilderString);
            content.append("}\n");
            content.append("return " + cachingField + ";\n");
        }
        content.append("}\n\n");

        return content.toString();
    }

    private static String createToStringBuilderString(ToStringDialogData data, String styleConstant)
            throws JavaModelException {
        StringBuffer content = new StringBuffer();
        if (styleConstant == null) {
            content.append("new ToStringBuilder(this)");
        } else {
            content.append("new ToStringBuilder(this, ");
            content.append(styleConstant);
            content.append(")");
        }
        if (data.getAppendSuper()) {
            content.append(".appendSuper(super.toString())");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(\"");
            content.append(checkedFields[i].getElementName());
            content.append("\", ");
            content.append(generateFieldAccessor(checkedFields[i], data.getUseGettersInsteadOfFields()));
            content.append(")");
        }
        content.append(".toString();\n");

        return content.toString();
    }

    private static String generateFieldAccessor(final IField field, final boolean useGettersInsteadOfFields)
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
