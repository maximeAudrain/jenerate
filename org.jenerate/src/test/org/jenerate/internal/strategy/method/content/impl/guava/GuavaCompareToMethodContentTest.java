package org.jenerate.internal.strategy.method.content.impl.guava;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.strategy.method.content.impl.commonslang.AbstractMethodContentTest;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Junit tests for the {@link GuavaCompareToMethodContent}
 * 
 * @author maudrain
 */
public class GuavaCompareToMethodContentTest extends
        AbstractMethodContentTest<GuavaCompareToMethodContent, CompareToMethodSkeleton, CompareToGenerationData> {

    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    @Override
    public CompareToGenerationData getConcreteData() {
        return mock(CompareToGenerationData.class);
    }

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockSpecificManagers(false);
        methodContent = new GuavaCompareToMethodContent(preferencesManager, javaInterfaceCodeAppender);
    }

    @Test
    public void testGetStrategyIdentifier() {
        assertEquals(MethodContentStrategyIdentifier.USE_GUAVA, methodContent.getStrategyIdentifier());
    }

    @Test
    public void testGetRelatedMethodSkeletonClass() {
        assertEquals(CompareToMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());
    }

    @Test
    public void testGetLibrariesToImport() {
        Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
        assertEquals(1, librariesToImport.size());
        assertEquals(GuavaCompareToMethodContent.LIBRARY_TO_IMPORT, librariesToImport.iterator().next());
    }

    @Test
    public void testGetMethodContentDefault() throws Exception {
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("Test castOther = (Test) other;\nreturn ComparisonChain.start()"
                + ".compare(field1, castOther.field1).compare(field2, castOther.field2).result();\n", content);
    }

    @Test
    public void testGetMethodContentWithGenerify() throws Exception {
        mockSpecificManagers(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return ComparisonChain.start().compare(field1, other.field1)"
                + ".compare(field2, other.field2).result();\n", content);
    }

    @Test
    public void testGetMethodContentWithGettersInsteadOfFields() throws Exception {
        when(data.useGettersInsteadOfFields()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("Test castOther = (Test) other;\nreturn ComparisonChain.start()"
                + ".compare(isField1(), castOther.isField1())"
                + ".compare(getField2(), castOther.getField2()).result();\n", content);
    }

    private void mockSpecificManagers(boolean generify) throws Exception {
        mockIsSourceLevelAbove5(generify);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.GENERIFY_COMPARETO)).thenReturn(generify);
        when(javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable"))
                .thenReturn(!generify);
    }

}
