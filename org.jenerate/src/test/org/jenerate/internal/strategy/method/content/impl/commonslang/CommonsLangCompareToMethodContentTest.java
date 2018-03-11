package org.jenerate.internal.strategy.method.content.impl.commonslang;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Junit test for the {@link CommonsLangCompareToMethodContent}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class CommonsLangCompareToMethodContentTest extends
        AbstractMethodContentTest<CommonsLangCompareToMethodContent, CompareToMethodSkeleton, CompareToGenerationData> {

    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    @Override
    public CompareToGenerationData getConcreteData() {
        return mock(CompareToGenerationData.class);
    }

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockSpecificManagers(false);
        methodContent = new CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager, javaInterfaceCodeAppender);
    }

    @Test
    public void testGetMethodContentStrategyIdentifier() {
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG, methodContent.getStrategyIdentifier());
    }

    @Test
    public void testGetRelatedMethodSkeletonClass() {
        assertEquals(CompareToMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());
    }

    @Test
    public void testGetLibrariesToImportWithCommonsLang() {
        Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
        assertEquals(1, librariesToImport.size());
        assertEquals(CommonsLangMethodContentLibraries.getCompareToBuilderLibrary(
                MethodContentStrategyIdentifier.USE_COMMONS_LANG), librariesToImport.iterator().next());
    }

    @Test
    public void testGetLibrariesToImportWithCommonsLang3() {
        methodContent = new CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager, javaInterfaceCodeAppender);
        Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
        assertEquals(1, librariesToImport.size());
        assertEquals(CommonsLangMethodContentLibraries.getCompareToBuilderLibrary(
                MethodContentStrategyIdentifier.USE_COMMONS_LANG3), librariesToImport.iterator().next());
    }

    @Test
    public void testGetMethodContentDefault() throws Exception {
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals(
                "Test castOther = (Test) other;\nreturn new CompareToBuilder()"
                        + ".append(field1, castOther.field1).append(field2, castOther.field2).toComparison();\n",
                content);
    }

    @Test
    public void testGetMethodContentWithGenerify() throws Exception {
        mockSpecificManagers(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return new CompareToBuilder().append(field1, other.field1)"
                + ".append(field2, other.field2).toComparison();\n", content);
    }

    @Test
    public void testGetMethodContentWithAppendSuper() throws Exception {
        when(data.appendSuper()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("Test castOther = (Test) other;\nreturn new CompareToBuilder()"
                + ".appendSuper(super.compareTo(other)).append(field1, castOther.field1)"
                + ".append(field2, castOther.field2).toComparison();\n", content);
    }

    private void mockSpecificManagers(boolean generify) throws Exception {
        mockIsSourceLevelAbove5(generify);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.GENERIFY_COMPARETO)).thenReturn(generify);
        when(javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable"))
                .thenReturn(!generify);
    }

}
