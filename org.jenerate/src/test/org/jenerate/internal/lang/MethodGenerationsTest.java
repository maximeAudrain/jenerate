package org.jenerate.internal.lang;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
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

    @Mock
    private IType objectClass;
    @Mock
    private IField iField1;
    @Mock
    private IField iField2;

    private IField[] checkedFields = new IField[2];

    private boolean appendSuper;
    private boolean generateComment;
    private boolean generify;
    private boolean addOverride;
    private boolean useGettersInsteadOfFields;

    @Before
    public void setUp() {
        when(objectClass.getElementName()).thenReturn(CLASS_NAME);
        when(iField1.getElementName()).thenReturn(FIELD_1);
        when(iField2.getElementName()).thenReturn(FIELD_2);

        checkedFields[0] = iField1;
        checkedFields[1] = iField2;
    }

    @Test
    public void testGenerateCompareToNoSuperNoCommentNotGeneric() {
        String compareToMethod = MethodGenerations.createCompareToMethod(objectClass, checkedFields, appendSuper,
                generateComment, generify);
        assertEquals("public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;\n"
                + "return new CompareToBuilder().append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareToNoSuperNoCommentGeneric() {
        generify = true;
        String compareToMethod = MethodGenerations.createCompareToMethod(objectClass, checkedFields, appendSuper,
                generateComment, generify);
        assertEquals("public int compareTo(final Clazz other) {\n"
                + "return new CompareToBuilder().append(field1, other.field1)"
                + ".append(field2, other.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareToNoSuperCommentNotGeneric() {
        generateComment = true;
        String compareToMethod = MethodGenerations.createCompareToMethod(objectClass, checkedFields, appendSuper,
                generateComment, generify);
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Comparable#compareTo(java.lang.Object)\n */\n"
                + "public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;"
                + "\nreturn new CompareToBuilder().append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).toComparison();\n}\n\n", compareToMethod);
    }

    @Test
    public void testGenerateCompareToSuperNoCommentNotGeneric() {
        appendSuper = true;
        String compareToMethod = MethodGenerations.createCompareToMethod(objectClass, checkedFields, appendSuper,
                generateComment, generify);
        assertEquals("public int compareTo(final Object other) {\nClazz castOther = (Clazz) other;"
                + "\nreturn new CompareToBuilder().appendSuper(super.compareTo(other))"
                + ".append(field1, castOther.field1).append(field2, castOther.field2).toComparison();\n}\n\n",
                compareToMethod);
    }

    @Test
    public void testGenerateToStringNoNothing() throws JavaModelException {
        String toStringMethod = MethodGenerations.createToStringMethod(checkedFields, appendSuper, generateComment,
                null, "", addOverride, useGettersInsteadOfFields);
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithAppendSuper() throws JavaModelException {
        appendSuper = true;
        String toStringMethod = MethodGenerations.createToStringMethod(checkedFields, appendSuper, generateComment,
                null, "", addOverride, useGettersInsteadOfFields);
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".appendSuper(super.toString()).append(\"field1\", field1)"
                + ".append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithGenerateComment() throws JavaModelException {
        generateComment = true;
        String toStringMethod = MethodGenerations.createToStringMethod(checkedFields, appendSuper, generateComment,
                null, "", addOverride, useGettersInsteadOfFields);
        assertEquals("/* (non-Javadoc)\n * @see java.lang.Object#toString()\n */\n"
                + "public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithStyle() throws JavaModelException {
        String toStringMethod = MethodGenerations.createToStringMethod(checkedFields, appendSuper, generateComment,
                STYLE_CONSTANT, "", addOverride, useGettersInsteadOfFields);
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this, STYLE)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithCache() throws JavaModelException {
        String toStringMethod = MethodGenerations.createToStringMethod(checkedFields, appendSuper, generateComment,
                null, TO_STRING_CACHING_FIELD, addOverride, useGettersInsteadOfFields);
        assertEquals("public String toString() {\nif (toString== null) {\n"
                + "toString = new ToStringBuilder(this).append(\"field1\", field1)"
                + ".append(\"field2\", field2).toString();\n}\nreturn toString;\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithOverride() throws JavaModelException {
        addOverride = true;
        String toStringMethod = MethodGenerations.createToStringMethod(checkedFields, appendSuper, generateComment,
                null, "", addOverride, useGettersInsteadOfFields);
        assertEquals("@Override\npublic String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", field1).append(\"field2\", field2).toString();\n}\n\n", toStringMethod);
    }

    @Test
    public void testGenerateToStringWithUseGetters() throws JavaModelException {
        useGettersInsteadOfFields = true;
        String toStringMethod = MethodGenerations.createToStringMethod(checkedFields, appendSuper, generateComment,
                null, "", addOverride, useGettersInsteadOfFields);
        assertEquals("public String toString() {\nreturn new ToStringBuilder(this)"
                + ".append(\"field1\", getField1()).append(\"field2\", getField2()).toString();\n}\n\n", toStringMethod);
    }

}
