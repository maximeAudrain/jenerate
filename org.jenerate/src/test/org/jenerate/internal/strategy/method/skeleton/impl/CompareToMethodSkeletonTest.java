package org.jenerate.internal.strategy.method.skeleton.impl;

import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link CompareToMethodSkeleton}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class CompareToMethodSkeletonTest extends
        AbstractMethodSkeletonTest<CompareToMethodSkeleton, CompareToGenerationData> {

    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockSpecificManagers(false);
    }

    @Override
    public CompareToMethodSkeleton getConcreteClassUnderTest() {
        return new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender);
    }

    @Override
    public CompareToGenerationData getConcreteData() {
        return mock(CompareToGenerationData.class);
    }

    @Test
    public void testLibrariesToImport() {
        assertTrue(methodSkeleton.getLibrariesToImport().isEmpty());
    }

    @Test
    public void testGetUserActionIdentifier() {
        assertEquals(MethodsGenerationCommandIdentifier.COMPARE_TO, methodSkeleton.getCommandIdentifier());
    }

    @Test
    public void testGetMethodName() {
        assertEquals(CompareToMethodSkeleton.COMPARE_TO_METHOD_NAME, methodSkeleton.getMethodName());
    }

    @Test
    public void testGetMethodArgumentsNoGenerify() throws Exception {
        String[] methodArguments = methodSkeleton.getMethodArguments(objectClass);
        assertEquals(1, methodArguments.length);
        assertEquals(methodSkeleton.createArgument("Object"), methodArguments[0]);
    }

    @Test
    public void testGetMethodArgumentsGenerify() throws Exception {
        mockSpecificManagers(true);
        String[] methodArguments = methodSkeleton.getMethodArguments(objectClass);
        assertEquals(1, methodArguments.length);
        assertEquals(methodSkeleton.createArgument(TEST_ELEMENT_NAME), methodArguments[0]);
    }

    @Test
    public void testGetMethodDefault() throws Exception {
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        verify(javaInterfaceCodeAppender, never()).addSuperInterface(objectClass, "Comparable");
        assertEquals("public int compareTo(final Object other) {\nCONTENT}\n\n", method);
    }

    @Test
    public void testGetMethodGenerateComment() throws Exception {
        when(data.generateComment()).thenReturn(true);
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        verify(javaInterfaceCodeAppender, never()).addSuperInterface(objectClass, "Comparable");
        assertEquals("/**\n * {@inheritDoc}\n */\n" + "public int compareTo(final Object other) {\nCONTENT}\n\n",
                method);
    }

    @Test
    public void testGetMethodGenerify() throws Exception {
        mockSpecificManagers(true);
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        verify(javaInterfaceCodeAppender, times(1)).addSuperInterface(objectClass, "Comparable<Test>");
        assertEquals("public int compareTo(final Test other) {\nCONTENT}\n\n", method);
    }

    @Test
    public void testGetMethodComparableNotImplementedInSupertype() throws Exception {
        when(javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable")).thenReturn(false);
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        verify(javaInterfaceCodeAppender, times(1)).addSuperInterface(objectClass, "Comparable");
        assertEquals("public int compareTo(final Object other) {\nCONTENT}\n\n", method);
    }

    private void mockSpecificManagers(boolean generify) throws Exception {
        mockIsSourceLevelAbove5(generify);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.GENERIFY_COMPARETO)).thenReturn(generify);
        when(javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable")).thenReturn(
                !generify);
    }
}
