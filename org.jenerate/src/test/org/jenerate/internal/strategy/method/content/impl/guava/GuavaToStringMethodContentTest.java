package org.jenerate.internal.strategy.method.content.impl.guava;

import java.util.Set;

import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.impl.commonslang.AbstractMethodContentTest;
import org.jenerate.internal.strategy.method.skeleton.impl.ToStringMethodSkeleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link GuavaToStringMethodContent}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class GuavaToStringMethodContentTest extends
        AbstractMethodContentTest<GuavaToStringMethodContent, ToStringMethodSkeleton, ToStringGenerationData> {

    @Override
    public ToStringGenerationData getConcreteData() {
        return mock(ToStringGenerationData.class);
    }

    @Override
    public void callbackAfterSetUp() throws Exception {
        methodContent = new GuavaToStringMethodContent(preferencesManager);
    }

    @Test
    public void testGetStrategyIdentifier() {
        assertEquals(MethodContentStrategyIdentifier.USE_GUAVA, methodContent.getStrategyIdentifier());
    }

    @Test
    public void testGetRelatedMethodSkeletonClass() {
        assertEquals(ToStringMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());
    }

    @Test
    public void testGetLibrariesToImport() {
        Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
        assertEquals(1, librariesToImport.size());
        assertEquals(GuavaToStringMethodContent.LIBRARY_TO_IMPORT, librariesToImport.iterator().next());
    }

    @Test
    public void testGetMethodContentDefault() throws Exception {
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return MoreObjects.toStringHelper(this).add(\"field1\", field1)"
                + ".add(\"field2\", field2).toString();\n", content);
    }

    @Test
    public void testGetMethodContentWithAppendSuper() throws Exception {
        when(data.appendSuper()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return MoreObjects.toStringHelper(this).add(\"super\", super.toString())"
                + ".add(\"field1\", field1).add(\"field2\", field2).toString();\n", content);
    }

    @Test
    public void testGetMethodContentWithUseGettersInsteadOfFields() throws Exception {
        when(data.useGettersInsteadOfFields()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return MoreObjects.toStringHelper(this).add(\"field1\", isField1())"
                + ".add(\"field2\", getField2()).toString();\n", content);
    }

}
