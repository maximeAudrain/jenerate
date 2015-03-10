package org.jenerate.internal.lang;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.impl.CompareToMethodGenerationData;
import org.jenerate.internal.data.impl.EqualsMethodGenerationData;
import org.jenerate.internal.data.impl.HashCodeMethodGenerationData;
import org.jenerate.internal.data.impl.ToStringMethodGenerationData;
import org.jenerate.internal.lang.generators.IInitMultNumbers;
import org.jenerate.internal.util.JavaUtils;

/**
 * Utility class that generates the method strings given certain parameters
 * 
 * @author maudrain
 */
public final class MethodGenerations {

    public MethodGenerations() {
        /* Only static helper methods */
    }

    public static String createCompareToMethod(CompareToMethodGenerationData compareToMethodGenerationData) {

        StringBuffer content = new StringBuffer();
        if (compareToMethodGenerationData.isGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Comparable#compareTo(java.lang.Object)\n");
            content.append(" */\n");
        }
        String other;
        String className = compareToMethodGenerationData.getObjectClass().getElementName();
        if (compareToMethodGenerationData.isGenerify()) {
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
        if (compareToMethodGenerationData.isAppendSuper()) {
            content.append(".appendSuper(super.compareTo(other))");
        }
        for (int i = 0; i < compareToMethodGenerationData.getFields().length; i++) {
            String fieldName = compareToMethodGenerationData.getFields()[i].getElementName();
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

    public static String createEqualsMethod(EqualsMethodGenerationData equalsMethodGenerationData)
            throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (equalsMethodGenerationData.isGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#equals(java.lang.Object)\n");
            content.append(" */\n");
        }
        if (equalsMethodGenerationData.isAddOverride()) {
            content.append("@Override\n");
        }
        content.append("public boolean equals(final Object other) {\n");
        if (equalsMethodGenerationData.isCompareReferences()) {
            content.append("if (this == other)");
            content.append(equalsMethodGenerationData.isUseBlocksInIfStatements() ? "{\n" : "");
            content.append(" return true;");
            content.append(equalsMethodGenerationData.isUseBlocksInIfStatements() ? "\n}\n" : "");
        }
        content.append("if ( !(other instanceof ");
        content.append(equalsMethodGenerationData.getObjectClass().getElementName());
        content.append(") )");
        content.append(equalsMethodGenerationData.isUseBlocksInIfStatements() ? "{\n" : "");
        content.append(" return false;");
        content.append(equalsMethodGenerationData.isUseBlocksInIfStatements() ? "\n}\n" : "");
        content.append(equalsMethodGenerationData.getObjectClass().getElementName());
        content.append(" castOther = (");
        content.append(equalsMethodGenerationData.getObjectClass().getElementName());
        content.append(") other;\n");
        content.append("return new EqualsBuilder()");
        if (equalsMethodGenerationData.isAppendSuper()) {
            content.append(".appendSuper(super.equals(other))");
        }
        for (int i = 0; i < equalsMethodGenerationData.getFields().length; i++) {
            content.append(".append(");
            String fieldName = JavaUtils.generateFieldAccessor(equalsMethodGenerationData.getFields()[i],
                    equalsMethodGenerationData.isUseGettersInsteadOfFields());
            content.append(fieldName);
            content.append(", castOther.");
            content.append(fieldName);
            content.append(")");
        }
        content.append(".isEquals();\n");
        content.append("}\n\n");

        return content.toString();
    }

    public static String createHashCodeMethod(HashCodeMethodGenerationData hashCodeMethodGenerationData)
            throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (hashCodeMethodGenerationData.isGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#hashCode()\n");
            content.append(" */\n");
        }
        if (hashCodeMethodGenerationData.isAddOverride()) {
            content.append("@Override\n");
        }
        content.append("public int hashCode() {\n");

        String hashCodeBuilderString = createHashCodeBuilderString(hashCodeMethodGenerationData.getFields(),
                hashCodeMethodGenerationData.isAppendSuper(), hashCodeMethodGenerationData.getImNumbers(),
                hashCodeMethodGenerationData.isUseGettersInsteadOfFields());

        if (hashCodeMethodGenerationData.getCachingField().isEmpty()) {
            content.append("return ");
            content.append(hashCodeBuilderString);
        } else {
            content.append("if (" + hashCodeMethodGenerationData.getCachingField() + "== 0) {\n");
            content.append(hashCodeMethodGenerationData.getCachingField() + " = ");
            content.append(hashCodeBuilderString);
            content.append("}\n");
            content.append("return " + hashCodeMethodGenerationData.getCachingField() + ";\n");
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

    public static String createToStringMethod(ToStringMethodGenerationData toStringMethodGenerationData)
            throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (toStringMethodGenerationData.isGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#toString()\n");
            content.append(" */\n");
        }
        if (toStringMethodGenerationData.isAddOverride()) {
            content.append("@Override\n");
        }
        content.append("public String toString() {\n");
        String toStringBuilderString = createToStringBuilderString(toStringMethodGenerationData.getFields(),
                toStringMethodGenerationData.isAppendSuper(), toStringMethodGenerationData.getStyleConstant(),
                toStringMethodGenerationData.isUseGettersInsteadOfFields());
        
        if (toStringMethodGenerationData.getCachingField().isEmpty()) {
            content.append("return ");
            content.append(toStringBuilderString);

        } else {
            content.append("if (" + toStringMethodGenerationData.getCachingField() + "== null) {\n");
            content.append(toStringMethodGenerationData.getCachingField() + " = ");
            content.append(toStringBuilderString);
            content.append("}\n");
            content.append("return " + toStringMethodGenerationData.getCachingField() + ";\n");
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
