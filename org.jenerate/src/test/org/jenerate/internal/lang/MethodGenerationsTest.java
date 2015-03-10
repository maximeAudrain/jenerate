package org.jenerate.internal.lang;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.impl.CompareToMethodGenerationData;
import org.jenerate.internal.data.impl.EqualsMethodGenerationData;
import org.jenerate.internal.data.impl.HashCodeMethodGenerationData;
import org.jenerate.internal.data.impl.ToStringMethodGenerationData;
import org.jenerate.internal.lang.generators.CustomInitMultNumbers;
import org.jenerate.internal.lang.generators.DefaultInitMultNumbers;
import org.jenerate.internal.lang.generators.IInitMultNumbers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Junit tests for the {@link MethodGenerations} helper class
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class MethodGenerationsTest {

    private static final String CLASS_NAME = "Clazz";
    private static final String FIELD_1 = "field1";
    private static final String FIELD_2 = "field2";
    private static final String STYLE_CONSTANT = "STYLE";
    private static final String TO_STRING_CACHING_FIELD = "toString";
    private static final String HASH_CODE_CACHING_FIELD = "hashCode";

    @Mock
    private IType objectClass;
    @Mock
    private IField iField1;
    @Mock
    private IField iField2;

    private IInitMultNumbers imNumbers;

    private IField[] checkedFields = new IField[2];

    private boolean appendSuper;
    private boolean generateComment;
    private boolean generify;
    private boolean addOverride;
    private boolean useGettersInsteadOfFields;
    private boolean compareReferences;
    private boolean useBlocksInIfStatements;

    @Before
    public void setUp() {
        when(objectClass.getElementName()).thenReturn(CLASS_NAME);
        when(iField1.getElementName()).thenReturn(FIELD_1);
        when(iField2.getElementName()).thenReturn(FIELD_2);
        imNumbers = new CustomInitMultNumbers();
        imNumbers.setNumbers(54, 12);

        checkedFields[0] = iField1;
        checkedFields[1] = iField2;
    }

    @Test
    public void testGenerateCompareToDefault() {
        String compareToMethod = MethodGenerations.createCompareToMethod(new CompareToMethodGenerationData(
                checkedFields, objectClass, appendSuper, generateComment, generify));
        assertEquals("public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;\n"
                + "return new CompareToBuilder().append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareToWithGeneric() {
        generify = true;
        String compareToMethod = MethodGenerations.createCompareToMethod(new CompareToMethodGenerationData(
                checkedFields, objectClass, appendSuper, generateComment, generify));
        assertEquals("public int compareTo(final Clazz other) {\n"
                + "return new CompareToBuilder().append(field1, other.field1)"
                + ".append(field2, other.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareWithComment() {
        generateComment = true;
        String compareToMethod = MethodGenerations.createCompareToMethod(new CompareToMethodGenerationData(
                checkedFields, objectClass, appendSuper, generateComment, generify));
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Comparable#compareTo(java.lang.Object)\n */\n"
                + "public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;"
                + "\nreturn new CompareToBuilder().append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareToWithSuper() {
        appendSuper = true;
        String compareToMethod = MethodGenerations.createCompareToMethod(new CompareToMethodGenerationData(
                checkedFields, objectClass, appendSuper, generateComment, generify));
        assertEquals("public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;"
                + "\nreturn new CompareToBuilder().appendSuper(super.compareTo(other))"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).toComparison();\n}\n\n",
                compareToMethod);
    }

    @Test
    public void testGenerateToStringDefault() throws JavaModelException {
        String toStringMethod = MethodGenerations.createToStringMethod(new ToStringMethodGenerationData(checkedFields,
                appendSuper, generateComment, null, "", addOverride, useGettersInsteadOfFields));
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithAppendSuper() throws JavaModelException {
        appendSuper = true;
        String toStringMethod = MethodGenerations.createToStringMethod(new ToStringMethodGenerationData(checkedFields,
                appendSuper, generateComment, null, "", addOverride, useGettersInsteadOfFields));
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".appendSuper(super.toString()).append(\"field1\", field1)"
                + ".append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithGenerateComment() throws JavaModelException {
        generateComment = true;
        String toStringMethod = MethodGenerations.createToStringMethod(new ToStringMethodGenerationData(checkedFields,
                appendSuper, generateComment, null, "", addOverride, useGettersInsteadOfFields));
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Object#toString()\n */\n"
                + "public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithStyle() throws JavaModelException {
        String toStringMethod = MethodGenerations.createToStringMethod(new ToStringMethodGenerationData(checkedFields,
                appendSuper, generateComment, STYLE_CONSTANT, "", addOverride, useGettersInsteadOfFields));
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this, STYLE)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithCache() throws JavaModelException {
        String toStringMethod = MethodGenerations.createToStringMethod(new ToStringMethodGenerationData(checkedFields,
                appendSuper, generateComment, null, TO_STRING_CACHING_FIELD, addOverride, useGettersInsteadOfFields));
        assertEquals("public String toString() {\nif (toString== null) {\n"
                + "toString = new ToStringBuilder(this).append(\"field1\", field1)"
                + ".append(\"field2\", field2).toString();\n}\nreturn toString;\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithOverride() throws JavaModelException {
        addOverride = true;
        String toStringMethod = MethodGenerations.createToStringMethod(new ToStringMethodGenerationData(checkedFields,
                appendSuper, generateComment, null, "", addOverride, useGettersInsteadOfFields));
        assertEquals("@Override\npublic String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithUseGetters() throws JavaModelException {
        useGettersInsteadOfFields = true;
        String toStringMethod = MethodGenerations.createToStringMethod(new ToStringMethodGenerationData(checkedFields,
                appendSuper, generateComment, null, "", addOverride, useGettersInsteadOfFields));
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", getField1()).append(\"field2\", getField2()).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateEqualsDefault() throws JavaModelException {
        String equalsMethod = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
        assertEquals("public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) return false;"
                + "Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithAppendSuper() throws JavaModelException {
        appendSuper = true;
        String equalsMethod = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
        assertEquals("public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) return false;"
                + "Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".appendSuper(super.equals(other)).append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithComment() throws JavaModelException {
        generateComment = true;
        String equalsMethod = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Object#equals(java.lang.Object)\n */\n"
                + "public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) return false;"
                + "Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithCompareReferences() throws JavaModelException {
        compareReferences = true;
        String equalsMethod = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
        assertEquals("public boolean equals(final Object other) {\nif (this == other) return true;"
                + "if ( !(other instanceof Clazz) ) return false;Clazz castOther = (Clazz) other;\n"
                + "return new EqualsBuilder().append(field1, castOther.field1).append(field2, castOther.field2)"
                + ".isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithAddOverride() throws JavaModelException {
        addOverride = true;
        String equalsMethod = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
        assertEquals("@Override\npublic boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) "
                + "return false;Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithGetters() throws JavaModelException {
        useGettersInsteadOfFields = true;
        String equalsMethod = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
        assertEquals("public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) return false;"
                + "Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".append(getField1(), castOther.getField1()).append(getField2(), castOther.getField2())"
                + ".isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithBlocksInIfs() throws JavaModelException {
        useBlocksInIfStatements = true;
        String equalsMethod = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
        assertEquals(
                "public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ){\n return false;\n}"
                        + "\nClazz castOther = (Clazz) other;\nreturn new EqualsBuilder().append(field1, castOther.field1)"
                        + ".append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithBlocksInIfsAndCompareReferences() throws JavaModelException {
        useBlocksInIfStatements = true;
        compareReferences = true;
        String equalsMethod = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
        assertEquals("public boolean equals(final Object other) {\nif (this == other){\n return true;\n}\n"
                + "if ( !(other instanceof Clazz) ){\n return false;\n}\nClazz castOther = (Clazz) other;\n"
                + "return new EqualsBuilder().append(field1, castOther.field1).append(field2, castOther.field2)"
                + ".isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateHashCodeDefault() throws JavaModelException {
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, "", addOverride, useGettersInsteadOfFields));
        assertEquals("public int hashCode() {\nreturn new HashCodeBuilder(54, 12)"
                + ".append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithSuper() throws JavaModelException {
        appendSuper = true;
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, "", addOverride, useGettersInsteadOfFields));
        assertEquals("public int hashCode() {\nreturn new HashCodeBuilder(54, 12)"
                + ".appendSuper(super.hashCode()).append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithComment() throws JavaModelException {
        generateComment = true;
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, "", addOverride, useGettersInsteadOfFields));
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Object#hashCode()\n */\n"
                + "public int hashCode() {\nreturn new HashCodeBuilder(54, 12)"
                + ".append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithDefaultNumbers() throws JavaModelException {
        imNumbers = new DefaultInitMultNumbers();
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, "", addOverride, useGettersInsteadOfFields));
        assertEquals("public int hashCode() {\nreturn new HashCodeBuilder()"
                + ".append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithCachingField() throws JavaModelException {
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, HASH_CODE_CACHING_FIELD, addOverride,
                useGettersInsteadOfFields));
        assertEquals("public int hashCode() {\nif (hashCode== 0) {\nhashCode = new HashCodeBuilder(54, 12)"
                + ".append(field1).append(field2).toHashCode();\n}\nreturn hashCode;\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithOverride() throws JavaModelException {
        addOverride = true;
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, "", addOverride, useGettersInsteadOfFields));
        assertEquals("@Override\npublic int hashCode() {\nreturn new HashCodeBuilder(54, 12)"
                + ".append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithGetters() throws JavaModelException {
        useGettersInsteadOfFields = true;
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, "", addOverride, useGettersInsteadOfFields));
        assertEquals("public int hashCode() {\nreturn new HashCodeBuilder(54, 12).append(getField1())"
                + ".append(getField2()).toHashCode();\n}\n\n", hashcodeMethod);
    }

}
