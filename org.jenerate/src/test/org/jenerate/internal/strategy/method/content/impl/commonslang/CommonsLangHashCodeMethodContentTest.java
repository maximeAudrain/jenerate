package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.hashcode.impl.InitMultNumbersCustom;
import org.jenerate.internal.domain.hashcode.impl.InitMultNumbersDefault;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.strategy.method.content.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Junit test for the {@link CommonsLangHashCodeMethodContent}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonsLangHashCodeMethodContentTest
        extends
        AbstractMethodContentTest<CommonsLangHashCodeMethodContent, HashCodeMethodSkeleton, EqualsHashCodeGenerationData> {

    private static final String HASH_CODE_CACHING_FIELD = "hashCode";

    @Mock
    private IField cachingField;

    @Override
    public EqualsHashCodeGenerationData getConcreteData() {
        return mock(EqualsHashCodeGenerationData.class);
    }

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockCacheHashCode(false);
        mockFieldsFinal(false);
        when(data.getInitMultNumbers()).thenReturn(new InitMultNumbersDefault());
        when(objectClass.getField(anyString())).thenReturn(cachingField);
        when(cachingField.exists()).thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.HASHCODE_CACHING_FIELD)).thenReturn(
                HASH_CODE_CACHING_FIELD);
        methodContent = new CommonsLangHashCodeMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager);
    }

    @Test
    public void testGetMethodContentStrategyIdentifier() {
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                methodContent.getMethodContentStrategyIdentifier());
    }

    @Test
    public void testGetRelatedMethodSkeletonClass() {
        assertEquals(HashCodeMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());
    }

    @Test
    public void testGetLibrariesToImportWithCommonsLang() {
        Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
        assertEquals(1, librariesToImport.size());
        assertEquals(CommonsLangMethodContentLibraries.getHashCodeBuilderLibrary(false), librariesToImport.iterator()
                .next());
    }

    @Test
    public void testGetLibrariesToImportWithCommonsLang3() {
        methodContent = new CommonsLangHashCodeMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager);
        Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
        assertEquals(1, librariesToImport.size());
        assertEquals(CommonsLangMethodContentLibraries.getHashCodeBuilderLibrary(true), librariesToImport.iterator()
                .next());
    }

    @Test
    public void testGetMethodContentDefault() throws Exception {
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return new HashCodeBuilder().append(field1).append(field2).toHashCode();\n", content);
    }
    
    @Test
    public void testGetMethodContentWithCachingFieldAllFieldsNotFinal() throws Exception {
        mockCacheHashCode(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return new HashCodeBuilder().append(field1).append(field2).toHashCode();\n", content);
    }

    @Test
    public void testGetMethodWithCachingFieldNotAlreadyPresent() throws Exception {
        mockCacheHashCode(true);
        mockFieldsFinal(true);
        String content = methodContent.getMethodContent(objectClass, data);
        verify(objectClass, times(1)).createField("private transient int " + HASH_CODE_CACHING_FIELD + ";\n\n",
                elementPosition, true, null);
        assertEquals("if (hashCode== 0) {\nhashCode = new HashCodeBuilder().append(field1)"
                + ".append(field2).toHashCode();\n}\nreturn hashCode;\n", content);
    }

    @Test
    public void testGetMethodWithCachingFieldAlreadyPresent() throws Exception {
        when(cachingField.exists()).thenReturn(true);
        mockCacheHashCode(true);
        mockFieldsFinal(true);
        String content = methodContent.getMethodContent(objectClass, data);
        verify(cachingField, times(1)).delete(true, null);
        verify(objectClass, times(1)).createField("private transient int " + HASH_CODE_CACHING_FIELD + ";\n\n",
                elementPosition, true, null);
        assertEquals("if (hashCode== 0) {\nhashCode = new HashCodeBuilder().append(field1)"
                + ".append(field2).toHashCode();\n}\nreturn hashCode;\n", content);
    }

    @Test
    public void testGetMethodContentWithAppendSuper() throws Exception {
        when(data.getAppendSuper()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return new HashCodeBuilder().appendSuper(super.hashCode())"
                + ".append(field1).append(field2).toHashCode();\n", content);
    }

    @Test
    public void testGetMethodContentWithCustomMultNumbers() throws Exception {
        InitMultNumbersCustom initMultNumbersCustom = new InitMultNumbersCustom();
        initMultNumbersCustom.setNumbers(3, 37);
        when(data.getInitMultNumbers()).thenReturn(initMultNumbersCustom);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return new HashCodeBuilder(3, 37).append(field1).append(field2).toHashCode();\n", content);
    }

    @Test
    public void testGetMethodContentWithUseGettersInsteadOfFields() throws Exception {
        when(data.getUseGettersInsteadOfFields()).thenReturn(true);
        String content = methodContent.getMethodContent(objectClass, data);
        assertEquals("return new HashCodeBuilder().append(isField1()).append(getField2()).toHashCode();\n", content);
    }

    private void mockCacheHashCode(boolean cacheHashCode) throws Exception {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).thenReturn(cacheHashCode);
    }
    
    private void mockFieldsFinal(boolean cacheToString) throws JavaModelException {
        when(field1.getFlags()).thenReturn(cacheToString ? 16 : 0);
        when(field2.getFlags()).thenReturn(cacheToString ? 16 : 0);
    }
}
