package org.jenerate.internal.lang;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.lang.generators.IInitMultNumbers;
import org.jenerate.internal.util.JavaUtils;
import org.jenerate.internal.util.PreferenceUtils;

/**
 * Utility class that generates the method strings given certain parameters
 * 
 * @author maudrain
 */
public final class MethodGenerations {

    public MethodGenerations() {
        /* Only static helper methods */
    }

    public static String createCompareToMethod(IType objectClass, IField[] checkedFields, boolean appendSuper,
            boolean generateComment, boolean generify) {

        StringBuffer content = new StringBuffer();
        if (generateComment) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Comparable#compareTo(java.lang.Object)\n");
            content.append(" */\n");
        }
        String other;
        if (generify) {
            content.append("public int compareTo(final " + objectClass.getElementName() + " other) {\n");

            other = "other";
        } else {
            content.append("public int compareTo(final Object other) {\n");
            content.append(objectClass.getElementName());
            content.append(" castOther = (");
            content.append(objectClass.getElementName());
            content.append(") other;\n");

            other = "castOther";
        }
        content.append("return new CompareToBuilder()");
        if (appendSuper) {
            content.append(".appendSuper(super.compareTo(other))");
        }
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(checkedFields[i].getElementName());
            content.append(", " + other + ".");
            content.append(checkedFields[i].getElementName());
            content.append(")");
        }
        content.append(".toComparison();\n");
        content.append("}\n\n");

        return content.toString();
    }

    public static String createEqualsMethod(final IType objectClass, final IField[] checkedFields,
            final boolean appendSuper, final boolean generateComment, final boolean compareReferences,
            final boolean addOverride, final boolean useGettersInsteadOfFields, final boolean useBlocksInIfStatements)
            throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (generateComment) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#equals(java.lang.Object)\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public boolean equals(final Object other) {\n");
        if (compareReferences) {
            content.append("if (this == other)");
            content.append(useBlocksInIfStatements ? "{\n" : "");
            content.append(" return true;");
            content.append(useBlocksInIfStatements ? "\n}\n" : "");
        }
        content.append("if ( !(other instanceof ");
        content.append(objectClass.getElementName());
        content.append(") )");
        content.append(useBlocksInIfStatements ? "{\n" : "");
        content.append(" return false;");
        content.append(useBlocksInIfStatements ? "\n}\n" : "");
        content.append(objectClass.getElementName());
        content.append(" castOther = (");
        content.append(objectClass.getElementName());
        content.append(") other;\n");
        content.append("return new EqualsBuilder()");
        if (appendSuper) {
            content.append(".appendSuper(super.equals(other))");
        }
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(JavaUtils.generateFieldAccessor(checkedFields[i], useGettersInsteadOfFields));
            content.append(", castOther.");
            content.append(JavaUtils.generateFieldAccessor(checkedFields[i], useGettersInsteadOfFields));
            content.append(")");
        }
        content.append(".isEquals();\n");
        content.append("}\n\n");

        return content.toString();
    }

    public static String createHashCodeMethod(final IField[] checkedFields, final boolean appendSuper,
            final boolean generateComment, final IInitMultNumbers imNumbers, final boolean isCacheable,
            final boolean addOverride, final boolean useGettersInsteadOfFields)
            throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (generateComment) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#hashCode()\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public int hashCode() {\n");
        if (isCacheable) {
            String cachingField = PreferenceUtils.getHashCodeCachingField();
            content.append("if (" + cachingField + "== 0) {\n");
            content.append(cachingField + " = ");
            content.append(createHashCodeBuilderString(checkedFields, appendSuper, imNumbers, useGettersInsteadOfFields));
            content.append("}\n");
            content.append("return " + cachingField + ";\n");

        } else {
            content.append("return ");
            content.append(createHashCodeBuilderString(checkedFields, appendSuper, imNumbers, useGettersInsteadOfFields));
        }
        content.append("}\n\n");

        return content.toString();
    }

    private static String createHashCodeBuilderString(final IField[] checkedFields, final boolean appendSuper,
            final IInitMultNumbers imNumbers, final boolean useGettersInsteadOfFields) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        content.append("new HashCodeBuilder(");
        content.append(imNumbers.getValue());
        content.append(")");
        if (appendSuper) {
            content.append(".appendSuper(super.hashCode())");
        }
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(JavaUtils.generateFieldAccessor(checkedFields[i], useGettersInsteadOfFields));
            content.append(")");
        }
        content.append(".toHashCode();\n");

        return content.toString();
    }

    public static String createToStringMethod(final IField[] checkedFields, final boolean appendSuper,
            final boolean generateComment, final String styleConstant, final String cachingField,
            final boolean addOverride, final boolean useGettersInsteadOfFields)
            throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (generateComment) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#toString()\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public String toString() {\n");
        if (cachingField.isEmpty()) {
            content.append("return ");
            content.append(createToStringBuilderString(checkedFields, appendSuper, styleConstant,
                    useGettersInsteadOfFields));

        } else {
            content.append("if (" + cachingField + "== null) {\n");
            content.append(cachingField + " = ");
            content.append(createToStringBuilderString(checkedFields, appendSuper, styleConstant,
                    useGettersInsteadOfFields));
            content.append("}\n");
            content.append("return " + cachingField + ";\n");
        }
        content.append("}\n\n");

        return content.toString();
    }

    private static String createToStringBuilderString(final IField[] checkedFields, final boolean appendSuper,
            final String styleConstant, final boolean useGettersInsteadOfFields) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        if (styleConstant == null) {
            content.append("new ToStringBuilder(this)");
        } else {
            content.append("new ToStringBuilder(this, ");
            content.append(styleConstant);
            content.append(")");
        }
        if (appendSuper) {
            content.append(".appendSuper(super.toString())");
        }
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(\"");
            content.append(checkedFields[i].getElementName());
            content.append("\", ");
            content.append(JavaUtils.generateFieldAccessor(checkedFields[i], useGettersInsteadOfFields));
            content.append(")");
        }
        content.append(".toString();\n");

        return content.toString();
    }

}
