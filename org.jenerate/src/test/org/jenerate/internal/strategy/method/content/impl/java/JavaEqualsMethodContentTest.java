package org.jenerate.internal.strategy.method.content.impl.java;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.impl.commonslang.AbstractMethodContentTest;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Junit tests for the {@link JavaEqualsMethodContent}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class JavaEqualsMethodContentTest
        extends AbstractMethodContentTest<JavaEqualsMethodContent, EqualsMethodSkeleton, EqualsHashCodeGenerationData> {

    @Override
    public EqualsHashCodeGenerationData getConcreteData() {
        return mock(EqualsHashCodeGenerationData.class);
    }

    @Override
    public void callbackAfterSetUp() throws Exception {
        methodContent = new JavaEqualsMethodContent(preferencesManager);
    }

    @Test
    public void testGetStrategyIdentifier() {
        assertEquals(MethodContentStrategyIdentifier.USE_JAVA, methodContent.getStrategyIdentifier());
    }

    @Test
    public void testGetRelatedMethodSkeletonClass() {
        assertEquals(EqualsMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());
    }

    @Test
    public void testGetLibrariesToImport() {
        Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
        assertEquals(2, librariesToImport.size());
        assertArrayEquals(JavaEqualsMethodContent.LIBRARIES_TO_IMPORT, librariesToImport.toArray());
    }

    @Test
    public void testGetMethodContentDefault() throws Exception {
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals(
                "if ( !(other instanceof Test) ) return false;Test castOther = (Test) other;\n"
                        + "return Objects.equals(field1, castOther.field1) && Objects.equals(field2, castOther.field2);\n",
                content);
    }

    @Test
    public void testGetMethodContentWithAppendSuper() throws Exception {
        when(data.appendSuper()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals(
                "if ( !(other instanceof Test) ) return false;"
                        + "if (!super.equals(other)) return false;Test castOther = (Test) other;\n"
                        + "return Objects.equals(field1, castOther.field1) && Objects.equals(field2, castOther.field2);\n",
                content);
    }

    @Test
    public void testGetMethodContentWithUseGettersInsteadOfFields() throws Exception {
        when(data.useGettersInsteadOfFields()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("if ( !(other instanceof Test) ) return false;Test castOther = (Test) other;\n"
                + "return Objects.equals(isField1(), castOther.isField1()) && "
                + "Objects.equals(getField2(), castOther.getField2());\n", content);
    }

    @Test
    public void testGetMethodContentWithUseBlocksInIfStatements() throws Exception {
        when(data.useBlockInIfStatements()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("if ( !(other instanceof Test) ){\n return false;\n}\n"
                + "Test castOther = (Test) other;\nreturn Objects.equals(field1, castOther.field1) && "
                + "Objects.equals(field2, castOther.field2);\n", content);
    }

    @Test
    public void testGetMethodContentWithUseBlocksInIfStatementsAndAppendSuper() throws Exception {
        when(data.useBlockInIfStatements()).thenReturn(true);
        when(data.appendSuper()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("if ( !(other instanceof Test) ){\n return false;\n}\n"
                + "if (!super.equals(other)){\n return false;\n}\nTest castOther = (Test) other;\n"
                + "return Objects.equals(field1, castOther.field1) && " + "Objects.equals(field2, castOther.field2);\n",
                content);
    }

    @Test
    public void testGetMethodContentWithCompareReferences() throws Exception {
        when(data.compareReferences()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("if (this == other) return true;if ( !(other instanceof Test) ) return false;"
                + "Test castOther = (Test) other;\nreturn Objects.equals(field1, castOther.field1) && "
                + "Objects.equals(field2, castOther.field2);\n", content);
    }

    @Test
    public void testGetMethodContentWithClassComparison() throws Exception {
        when(data.useClassComparison()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("if (other == null) return false;if ( !getClass().equals(other.getClass())) return false;"
                + "Test castOther = (Test) other;\nreturn Objects.equals(field1, castOther.field1) && "
                + "Objects.equals(field2, castOther.field2);\n", content);
    }

    @Test
    public void testGetMethodContentWithCompareReferencesAndUseBlocksInIfStatements() throws Exception {
        when(data.compareReferences()).thenReturn(true);
        when(data.useBlockInIfStatements()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("if (this == other){\n return true;\n}\nif ( !(other instanceof Test) ){\n return false;\n}\n"
                + "Test castOther = (Test) other;\nreturn Objects.equals(field1, castOther.field1) && "
                + "Objects.equals(field2, castOther.field2);\n", content);
    }

    @Test
    public void testGetMethodContentWithClassComparisonAndUseBlocksInIfStatements() throws Exception {
        when(data.useClassComparison()).thenReturn(true);
        when(data.useBlockInIfStatements()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals(
                "if (other == null){\n return false;\n}\nif ( !getClass().equals(other.getClass()))"
                        + "{\n return false;\n}\nTest castOther = (Test) other;\nreturn "
                        + "Objects.equals(field1, castOther.field1) && Objects.equals(field2, castOther.field2);\n",
                content);
    }

    @Test
    public void testGetMethodContentWithClassCast() throws Exception {
        when(data.useClassCast()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("if ( !Test.class.isInstance(other)) return false;"
                + "Test castOther = Test.class.cast(other);\nreturn Objects.equals(field1, castOther.field1) && "
                + "Objects.equals(field2, castOther.field2);\n", content);
    }

    @Test
    public void testGetMethodContentWithClassCastAndUseBlocksInIfStatements() throws Exception {
        when(data.useClassCast()).thenReturn(true);
        when(data.useBlockInIfStatements()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals(
                "if ( !Test.class.isInstance(other))"
                        + "{\n return false;\n}\nTest castOther = Test.class.cast(other);\nreturn "
                        + "Objects.equals(field1, castOther.field1) && Objects.equals(field2, castOther.field2);\n",
                content);
    }

    @Test
    public void testGetMethodContentWithClassComparisonAndUseSimplePrimitiveComparison() throws Exception {
        when(data.useClassComparison()).thenReturn(true);
        when(data.useBlockInIfStatements()).thenReturn(true);
        when(data.useSimplePrimitiveComparison()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals(
                "if (other == null){\n return false;\n}\nif ( !getClass().equals(other.getClass()))"
                        + "{\n return false;\n}\nTest castOther = (Test) other;\nreturn "
                        + "(field1 == castOther.field1) && Objects.equals(field2, castOther.field2);\n",
                content);
    }

    @Test
    public void testGetMethodContentWithClassComparisonAndUseDeepArrayComparison() throws Exception {
        when(data.useClassComparison()).thenReturn(true);
        when(data.useBlockInIfStatements()).thenReturn(true);
        when(data.useDeepArrayComparison()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals(
                "if (other == null){\n return false;\n}\nif ( !getClass().equals(other.getClass()))"
                        + "{\n return false;\n}\nTest castOther = (Test) other;\nreturn "
                        + "Objects.equals(field1, castOther.field1) && Arrays.equals(field2, castOther.field2);\n",
                content);
    }
}
