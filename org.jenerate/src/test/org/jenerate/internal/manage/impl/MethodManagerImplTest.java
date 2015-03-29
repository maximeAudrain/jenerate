package org.jenerate.internal.manage.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangEqualsMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangHashCodeMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringMethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.ToStringMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MethodManagerImplTest {

    @Mock
    private PreferencesManager preferencesManager;
    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;
    @Mock
    private CommandIdentifier unknownCommandIdentifier;

    private MethodManagerImpl methodManager;

    @Before
    public void setUp() {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.USE_COMMONS_LANG3)).thenReturn(
                Boolean.FALSE);
        methodManager = new MethodManagerImpl(preferencesManager, javaInterfaceCodeAppender);
    }

    @Test
    public void testWithUnknownCommandIdentifier() {
        assertTrue(methodManager.getMethods(unknownCommandIdentifier).isEmpty());
    }

    @Test
    public void testWithToStringCommandIdentifierAndCommansLang() {
        LinkedHashSet<Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData>> methods = methodManager
                .getMethods(MethodsGenerationCommandIdentifier.TO_STRING);
        assertEquals(1, methods.size());
        Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData> toStringMethod = methods.iterator().next();
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG, toStringMethod.getMethodContent()
                .getStrategyIdentifier());
        assertEquals(CommonsLangToStringMethodContent.class, toStringMethod.getMethodContent().getClass());
        assertEquals(ToStringMethodSkeleton.class, toStringMethod.getMethodSkeleton().getClass());
    }

    @Test
    public void testWithEqualsHashCodeCommandIdentifierAndCommansLang3() {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.USE_COMMONS_LANG3)).thenReturn(
                Boolean.TRUE);
        LinkedHashSet<Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData>> methods = methodManager
                .getMethods(MethodsGenerationCommandIdentifier.EQUALS_HASH_CODE);
        assertEquals(2, methods.size());
        Iterator<Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData>> iterator = methods.iterator();

        Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData> hashCodeMethod = iterator.next();
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG3, hashCodeMethod.getMethodContent()
                .getStrategyIdentifier());
        assertEquals(CommonsLangHashCodeMethodContent.class, hashCodeMethod.getMethodContent().getClass());
        assertEquals(HashCodeMethodSkeleton.class, hashCodeMethod.getMethodSkeleton().getClass());

        Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData> equalsMethod = iterator.next();
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG3, equalsMethod.getMethodContent()
                .getStrategyIdentifier());
        assertEquals(CommonsLangEqualsMethodContent.class, equalsMethod.getMethodContent().getClass());
        assertEquals(EqualsMethodSkeleton.class, equalsMethod.getMethodSkeleton().getClass());
    }

}
