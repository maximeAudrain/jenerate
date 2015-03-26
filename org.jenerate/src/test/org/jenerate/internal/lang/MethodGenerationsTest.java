package org.jenerate.internal.lang;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.CompareToDialogData;
import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.data.IInitMultNumbers;
import org.jenerate.internal.data.ToStringDialogData;
import org.jenerate.internal.data.impl.InitMultNumbersCustom;
import org.jenerate.internal.data.impl.InitMultNumbersDefault;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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

    @Mock
    private CompareToDialogData compareToDialogData;
    @Mock
    private EqualsHashCodeDialogData equalsHashCodeDialogData;
    @Mock
    private ToStringDialogData toStringDialogData;

    private IField[] checkedFields = new IField[2];

    private boolean generify;
    private boolean addOverride;

    @Before
    public void setUp() {
        when(objectClass.getElementName()).thenReturn(CLASS_NAME);
        when(iField1.getElementName()).thenReturn(FIELD_1);
        when(iField2.getElementName()).thenReturn(FIELD_2);

        checkedFields[0] = iField1;
        checkedFields[1] = iField2;

        mockCompareToDialogData();
        mockEqualsHashCodeDialogData();
        mockToStringDialogData();
    }

    private void mockCompareToDialogData() {
        when(compareToDialogData.getCheckedFields()).thenReturn(checkedFields);
        when(compareToDialogData.getGenerateComment()).thenReturn(false);
        when(compareToDialogData.getAppendSuper()).thenReturn(false);
    }

    private void mockEqualsHashCodeDialogData() {
        when(equalsHashCodeDialogData.getCheckedFields()).thenReturn(checkedFields);
        when(equalsHashCodeDialogData.getGenerateComment()).thenReturn(false);
        when(equalsHashCodeDialogData.getAppendSuper()).thenReturn(false);
        when(equalsHashCodeDialogData.getCompareReferences()).thenReturn(false);
        when(equalsHashCodeDialogData.getUseGettersInsteadOfFields()).thenReturn(false);
        when(equalsHashCodeDialogData.getUseBlockInIfStatements()).thenReturn(false);
        IInitMultNumbers imNumbers = new InitMultNumbersCustom();
        imNumbers.setNumbers(54, 12);
        when(equalsHashCodeDialogData.getInitMultNumbers()).thenReturn(imNumbers);
    }

    private void mockToStringDialogData() {
        when(toStringDialogData.getCheckedFields()).thenReturn(checkedFields);
        when(toStringDialogData.getGenerateComment()).thenReturn(false);
        when(toStringDialogData.getAppendSuper()).thenReturn(false);
        when(toStringDialogData.getUseGettersInsteadOfFields()).thenReturn(false);
    }

    @Test
    public void testGenerateCompareToDefault() {
        String compareToMethodContent = MethodGenerations.generateCompareToMethodContent(compareToDialogData, generify,
                objectClass);
        String compareToMethod = MethodGenerations.createCompareToMethod(objectClass, compareToDialogData, generify,
                compareToMethodContent);
        assertEquals("public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;\n"
                + "return new CompareToBuilder().append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareToWithGeneric() {
        generify = true;
        String compareToMethodContent = MethodGenerations.generateCompareToMethodContent(compareToDialogData, generify,
                objectClass);
        String compareToMethod = MethodGenerations.createCompareToMethod(objectClass, compareToDialogData, generify,
                compareToMethodContent);
        assertEquals("public int compareTo(final Clazz other) {\n"
                + "return new CompareToBuilder().append(field1, other.field1)"
                + ".append(field2, other.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareWithComment() {
        when(compareToDialogData.getGenerateComment()).thenReturn(true);
        String compareToMethodContent = MethodGenerations.generateCompareToMethodContent(compareToDialogData, generify,
                objectClass);
        String compareToMethod = MethodGenerations.createCompareToMethod(objectClass, compareToDialogData, generify,
                compareToMethodContent);
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Comparable#compareTo(java.lang.Object)\n */\n"
                + "public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;"
                + "\nreturn new CompareToBuilder().append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareToWithSuper() {
        when(compareToDialogData.getAppendSuper()).thenReturn(true);
        String compareToMethodContent = MethodGenerations.generateCompareToMethodContent(compareToDialogData, generify,
                objectClass);
        String compareToMethod = MethodGenerations.createCompareToMethod(objectClass, compareToDialogData, generify,
                compareToMethodContent);
        assertEquals("public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;"
                + "\nreturn new CompareToBuilder().appendSuper(super.compareTo(other))"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).toComparison();\n}\n\n",
                compareToMethod);
    }

    @Test
    public void testGenerateToStringDefault() throws JavaModelException {
        String toStringMethodContent = MethodGenerations.generateToStringMethodContent(toStringDialogData, null, "");
        String toStringMethod = MethodGenerations.createToStringMethod(toStringDialogData, addOverride,
                toStringMethodContent);
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithAppendSuper() throws JavaModelException {
        when(toStringDialogData.getAppendSuper()).thenReturn(true);
        String toStringMethodContent = MethodGenerations.generateToStringMethodContent(toStringDialogData, null, "");
        String toStringMethod = MethodGenerations.createToStringMethod(toStringDialogData, addOverride,
                toStringMethodContent);
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".appendSuper(super.toString()).append(\"field1\", field1)"
                + ".append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithGenerateComment() throws JavaModelException {
        when(toStringDialogData.getGenerateComment()).thenReturn(true);
        String toStringMethodContent = MethodGenerations.generateToStringMethodContent(toStringDialogData, null, "");
        String toStringMethod = MethodGenerations.createToStringMethod(toStringDialogData, addOverride,
                toStringMethodContent);
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Object#toString()\n */\n"
                + "public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithStyle() throws JavaModelException {
        String toStringMethodContent = MethodGenerations.generateToStringMethodContent(toStringDialogData,
                STYLE_CONSTANT, "");
        String toStringMethod = MethodGenerations.createToStringMethod(toStringDialogData, addOverride,
                toStringMethodContent);
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this, STYLE)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithCache() throws JavaModelException {
        String toStringMethodContent = MethodGenerations.generateToStringMethodContent(toStringDialogData, null,
                TO_STRING_CACHING_FIELD);
        String toStringMethod = MethodGenerations.createToStringMethod(toStringDialogData, addOverride,
                toStringMethodContent);
        assertEquals("public String toString() {\nif (toString== null) {\n"
                + "toString = new ToStringBuilder(this).append(\"field1\", field1)"
                + ".append(\"field2\", field2).toString();\n}\nreturn toString;\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithOverride() throws JavaModelException {
        addOverride = true;
        String toStringMethodContent = MethodGenerations.generateToStringMethodContent(toStringDialogData, null, "");
        String toStringMethod = MethodGenerations.createToStringMethod(toStringDialogData, addOverride,
                toStringMethodContent);
        assertEquals("@Override\npublic String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithUseGetters() throws JavaModelException {
        when(toStringDialogData.getUseGettersInsteadOfFields()).thenReturn(true);
        String toStringMethodContent = MethodGenerations.generateToStringMethodContent(toStringDialogData, null, "");
        String toStringMethod = MethodGenerations.createToStringMethod(toStringDialogData, addOverride,
                toStringMethodContent);
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", getField1()).append(\"field2\", getField2()).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateEqualsDefault() throws JavaModelException {
        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(equalsHashCodeDialogData,
                objectClass);
        String equalsMethod = MethodGenerations.createEqualsMethod(equalsHashCodeDialogData, addOverride,
                equalsMethodContent);
        assertEquals("public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) return false;"
                + "Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithAppendSuper() throws JavaModelException {
        when(equalsHashCodeDialogData.getAppendSuper()).thenReturn(true);
        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(equalsHashCodeDialogData,
                objectClass);
        String equalsMethod = MethodGenerations.createEqualsMethod(equalsHashCodeDialogData, addOverride,
                equalsMethodContent);
        assertEquals("public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) return false;"
                + "Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".appendSuper(super.equals(other)).append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithComment() throws JavaModelException {
        when(equalsHashCodeDialogData.getGenerateComment()).thenReturn(true);
        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(equalsHashCodeDialogData,
                objectClass);
        String equalsMethod = MethodGenerations.createEqualsMethod(equalsHashCodeDialogData, addOverride,
                equalsMethodContent);
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Object#equals(java.lang.Object)\n */\n"
                + "public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) return false;"
                + "Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithCompareReferences() throws JavaModelException {
        when(equalsHashCodeDialogData.getCompareReferences()).thenReturn(true);
        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(equalsHashCodeDialogData,
                objectClass);
        String equalsMethod = MethodGenerations.createEqualsMethod(equalsHashCodeDialogData, addOverride,
                equalsMethodContent);
        assertEquals("public boolean equals(final Object other) {\nif (this == other) return true;"
                + "if ( !(other instanceof Clazz) ) return false;Clazz castOther = (Clazz) other;\n"
                + "return new EqualsBuilder().append(field1, castOther.field1).append(field2, castOther.field2)"
                + ".isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithAddOverride() throws JavaModelException {
        addOverride = true;
        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(equalsHashCodeDialogData,
                objectClass);
        String equalsMethod = MethodGenerations.createEqualsMethod(equalsHashCodeDialogData, addOverride,
                equalsMethodContent);
        assertEquals("@Override\npublic boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) "
                + "return false;Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithGetters() throws JavaModelException {
        when(equalsHashCodeDialogData.getUseGettersInsteadOfFields()).thenReturn(true);
        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(equalsHashCodeDialogData,
                objectClass);
        String equalsMethod = MethodGenerations.createEqualsMethod(equalsHashCodeDialogData, addOverride,
                equalsMethodContent);
        assertEquals("public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ) return false;"
                + "Clazz castOther = (Clazz) other;\nreturn new EqualsBuilder()"
                + ".append(getField1(), castOther.getField1()).append(getField2(), castOther.getField2())"
                + ".isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithBlocksInIfs() throws JavaModelException {
        when(equalsHashCodeDialogData.getUseBlockInIfStatements()).thenReturn(true);
        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(equalsHashCodeDialogData,
                objectClass);
        String equalsMethod = MethodGenerations.createEqualsMethod(equalsHashCodeDialogData, addOverride,
                equalsMethodContent);
        assertEquals(
                "public boolean equals(final Object other) {\nif ( !(other instanceof Clazz) ){\n return false;\n}"
                        + "\nClazz castOther = (Clazz) other;\nreturn new EqualsBuilder().append(field1, castOther.field1)"
                        + ".append(field2, castOther.field2).isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateEqualsWithBlocksInIfsAndCompareReferences() throws JavaModelException {
        when(equalsHashCodeDialogData.getUseBlockInIfStatements()).thenReturn(true);
        when(equalsHashCodeDialogData.getCompareReferences()).thenReturn(true);
        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(equalsHashCodeDialogData,
                objectClass);
        String equalsMethod = MethodGenerations.createEqualsMethod(equalsHashCodeDialogData, addOverride,
                equalsMethodContent);
        assertEquals("public boolean equals(final Object other) {\nif (this == other){\n return true;\n}\n"
                + "if ( !(other instanceof Clazz) ){\n return false;\n}\nClazz castOther = (Clazz) other;\n"
                + "return new EqualsBuilder().append(field1, castOther.field1).append(field2, castOther.field2)"
                + ".isEquals();\n}\n\n", equalsMethod);
    }

    @Test
    public void testGenerateHashCodeDefault() throws JavaModelException {
        String hashCodeMethodContent = MethodGenerations.generateHashCodeMethodContent(equalsHashCodeDialogData, "");
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(equalsHashCodeDialogData, addOverride,
                hashCodeMethodContent);
        assertEquals("public int hashCode() {\nreturn new HashCodeBuilder(54, 12)"
                + ".append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithSuper() throws JavaModelException {
        when(equalsHashCodeDialogData.getAppendSuper()).thenReturn(true);
        String hashCodeMethodContent = MethodGenerations.generateHashCodeMethodContent(equalsHashCodeDialogData, "");
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(equalsHashCodeDialogData, addOverride,
                hashCodeMethodContent);
        assertEquals("public int hashCode() {\nreturn new HashCodeBuilder(54, 12)"
                + ".appendSuper(super.hashCode()).append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithComment() throws JavaModelException {
        when(equalsHashCodeDialogData.getGenerateComment()).thenReturn(true);
        String hashCodeMethodContent = MethodGenerations.generateHashCodeMethodContent(equalsHashCodeDialogData, "");
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(equalsHashCodeDialogData, addOverride,
                hashCodeMethodContent);
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Object#hashCode()\n */\n"
                + "public int hashCode() {\nreturn new HashCodeBuilder(54, 12)"
                + ".append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithDefaultNumbers() throws JavaModelException {
        when(equalsHashCodeDialogData.getInitMultNumbers()).thenReturn(new InitMultNumbersDefault());
        String hashCodeMethodContent = MethodGenerations.generateHashCodeMethodContent(equalsHashCodeDialogData, "");
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(equalsHashCodeDialogData, addOverride,
                hashCodeMethodContent);
        assertEquals("public int hashCode() {\nreturn new HashCodeBuilder()"
                + ".append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithCachingField() throws JavaModelException {
        String hashCodeMethodContent = MethodGenerations.generateHashCodeMethodContent(equalsHashCodeDialogData,
                HASH_CODE_CACHING_FIELD);
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(equalsHashCodeDialogData, addOverride,
                hashCodeMethodContent);
        assertEquals("public int hashCode() {\nif (hashCode== 0) {\nhashCode = new HashCodeBuilder(54, 12)"
                + ".append(field1).append(field2).toHashCode();\n}\nreturn hashCode;\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithOverride() throws JavaModelException {
        addOverride = true;
        String hashCodeMethodContent = MethodGenerations.generateHashCodeMethodContent(equalsHashCodeDialogData, "");
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(equalsHashCodeDialogData, addOverride,
                hashCodeMethodContent);
        assertEquals("@Override\npublic int hashCode() {\nreturn new HashCodeBuilder(54, 12)"
                + ".append(field1).append(field2).toHashCode();\n}\n\n", hashcodeMethod);
    }

    @Test
    public void testGenerateHashCodeWithGetters() throws JavaModelException {
        when(equalsHashCodeDialogData.getUseGettersInsteadOfFields()).thenReturn(true);
        String hashCodeMethodContent = MethodGenerations.generateHashCodeMethodContent(equalsHashCodeDialogData, "");
        String hashcodeMethod = MethodGenerations.createHashCodeMethod(equalsHashCodeDialogData, addOverride,
                hashCodeMethodContent);
        assertEquals("public int hashCode() {\nreturn new HashCodeBuilder(54, 12).append(getField1())"
                + ".append(getField2()).toHashCode();\n}\n\n", hashcodeMethod);
    }

}
