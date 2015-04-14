package org.jenerate.internal.strategy.method.skeleton.impl;

import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link HashCodeMethodSkeleton}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class ToStringMethodSkeletonTest extends
        AbstractMethodSkeletonTest<ToStringMethodSkeleton, ToStringGenerationData> {

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockSpecificManagers(false);
    }

    @Override
    public ToStringMethodSkeleton getConcreteClassUnderTest() {
        return new ToStringMethodSkeleton(preferencesManager);
    }

    @Override
    public ToStringGenerationData getConcreteData() {
        return mock(ToStringGenerationData.class);
    }

    @Test
    public void testLibrariesToImport() {
        assertTrue(methodSkeleton.getLibrariesToImport().isEmpty());
    }

    @Test
    public void testGetUserActionIdentifier() {
        assertEquals(MethodsGenerationCommandIdentifier.TO_STRING, methodSkeleton.getCommandIdentifier());
    }

    @Test
    public void testGetMethodName() {
        assertEquals(ToStringMethodSkeleton.TO_STRING_METHOD_NAME, methodSkeleton.getMethodName());
    }

    @Test
    public void testGetMethodArguments() throws Exception {
        String[] methodArguments = methodSkeleton.getMethodArguments(objectClass);
        assertEquals(0, methodArguments.length);
    }

    @Test
    public void testGetMethodDefault() throws Exception {
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        assertEquals("public String toString() {\nCONTENT}\n\n", method);
    }

    @Test
    public void testGetMethodGenerateComment() throws Exception {
        when(data.getGenerateComment()).thenReturn(true);
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        assertEquals("/**\n * {@inheritDoc}\n */\n" + "public String toString() {\nCONTENT}\n\n", method);
    }

    @Test
    public void testGetMethodAddOverride() throws Exception {
        mockSpecificManagers(true);
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        assertEquals("@Override\npublic String toString() {\nCONTENT}\n\n", method);
    }

    private void mockSpecificManagers(boolean addOverride) throws Exception {
        mockIsSourceLevelAbove5(addOverride);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.ADD_OVERRIDE_ANNOTATION)).thenReturn(
                addOverride);
    }

}
