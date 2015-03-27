package org.jenerate.internal.lang;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.method.content.tostring.ToStringStyle;

/**
 * Utility class that generates the method strings given certain parameters
 * 
 * @author maudrain
 */
public final class MethodGenerations {

    public MethodGenerations() {
        /* Only static helper methods */
    }

    public static String createCompareToMethod(IType objectClass, CompareToGenerationData data, boolean generify,
            String methodContent) {

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Comparable#compareTo(java.lang.Object)\n");
            content.append(" */\n");
        }

        String className = objectClass.getElementName();
        if (generify) {
            content.append("public int compareTo(final " + className + " other) {\n");
        } else {
            content.append("public int compareTo(final Object other) {\n");
        }
        content.append(methodContent);
        content.append("}\n\n");

        return content.toString();
    }

    public static String generateCompareToMethodContent(CompareToGenerationData data, boolean generify, IType objectClass) {
        String className = objectClass.getElementName();
        StringBuffer content = new StringBuffer();
        String other;
        if (generify) {
            other = "other";
        } else {
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
        return content.toString();
    }

    public static String createEqualsMethod(EqualsHashCodeGenerationData data, boolean addOverride, String methodContent) {

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
        content.append(methodContent);
        content.append("}\n\n");

        return content.toString();
    }

    public static String generateEqualsMethodContent(EqualsHashCodeGenerationData data, IType objectClass)
            throws JavaModelException {
        String elementName = objectClass.getElementName();
        boolean useBlockInIfStatements = data.getUseBlockInIfStatements();
        StringBuffer content = new StringBuffer();
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
        return content.toString();
    }

    public static String createHashCodeMethod(EqualsHashCodeGenerationData data, boolean addOverride, String methodContent) {

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
        content.append(methodContent);
        content.append("}\n\n");

        return content.toString();
    }

    public static String generateHashCodeMethodContent(EqualsHashCodeGenerationData data, String cachingField)
            throws JavaModelException {
        StringBuffer content = new StringBuffer();
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
        return content.toString();
    }

    private static String createHashCodeBuilderString(EqualsHashCodeGenerationData data) throws JavaModelException {
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

    public static String createToStringMethod(ToStringGenerationData data, boolean addOverride, String methodContent) {

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
        content.append(methodContent);
        content.append("}\n\n");

        return content.toString();
    }

    public static String generateToStringMethodContent(ToStringGenerationData data, String cachingField)
            throws JavaModelException {
        StringBuffer content = new StringBuffer();
        String toStringBuilderString = createToStringBuilderString(data);

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
        return content.toString();
    }

    private static String createToStringBuilderString(ToStringGenerationData data) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        ToStringStyle toStringStyle = data.getToStringStyle();
        if (ToStringStyle.NO_STYLE.equals(toStringStyle)) {
            content.append("new ToStringBuilder(this)");
        } else {
            content.append("new ToStringBuilder(this, ");
            content.append(toStringStyle.getFullStyle());
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
