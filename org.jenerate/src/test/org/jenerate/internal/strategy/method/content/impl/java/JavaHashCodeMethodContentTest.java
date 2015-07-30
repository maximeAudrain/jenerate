package org.jenerate.internal.strategy.method.content.impl.java;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.impl.commonslang.AbstractMethodContentTest;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for the {@link JavaHashCodeMethodContent}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class JavaHashCodeMethodContentTest extends
        AbstractMethodContentTest<JavaHashCodeMethodContent, HashCodeMethodSkeleton, EqualsHashCodeGenerationData> {

    @Override
    public EqualsHashCodeGenerationData getConcreteData() {
        return mock(EqualsHashCodeGenerationData.class);
    }

    @Override
    public void callbackAfterSetUp() throws Exception {
        methodContent = new JavaHashCodeMethodContent(preferencesManager);
    }

    @Test
    public void testGetStrategyIdentifier() {
        assertEquals(MethodContentStrategyIdentifier.USE_JAVA, methodContent.getStrategyIdentifier());
    }

    @Test
    public void testGetRelatedMethodSkeletonClass() {
        assertEquals(HashCodeMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());
    }

    @Test
    public void testGetLibrariesToImport() {
        Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
        assertEquals(1, librariesToImport.size());
        assertEquals(JavaHashCodeMethodContent.LIBRARY_TO_IMPORT, librariesToImport.iterator().next());
    }

    @Test
    public void testGetMethodContentDefault() throws Exception {
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return Objects.hash(field1, field2);\n", content);
    }

    @Test
    public void testGetMethodContentWithAppendSuper() throws Exception {
        when(data.appendSuper()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return Objects.hash(super.hashCode(), field1, field2);\n", content);
    }

    @Test
    public void testGetMethodContentWithUseGettersInsteadOfFields() throws Exception {
        when(data.useGettersInsteadOfFields()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return Objects.hash(isField1(), getField2());\n", content);
    }

}
