package org.jenerate.internal.strategy.method.skeleton.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit test for {@link HashCodeMethodSkeleton}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class HashCodeMethodSkeletonTest
        extends AbstractMethodSkeletonTest<HashCodeMethodSkeleton, EqualsHashCodeGenerationData> {

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockSpecificManagers(false);
    }

    @Override
    public HashCodeMethodSkeleton getConcreteClassUnderTest() {
        return new HashCodeMethodSkeleton(preferencesManager);
    }

    @Override
    public EqualsHashCodeGenerationData getConcreteData() {
        return mock(EqualsHashCodeGenerationData.class);
    }

    @Test
    public void testGetUserActionIdentifier() {
        assertEquals(MethodsGenerationCommandIdentifier.EQUALS_HASH_CODE, methodSkeleton.getCommandIdentifier());
    }

    @Test
    public void testGetMethodName() {
        assertEquals(HashCodeMethodSkeleton.HASH_CODE_METHOD_NAME, methodSkeleton.getMethodName());
    }

    @Test
    public void testGetMethodArguments() throws Exception {
        String[] methodArguments = methodSkeleton.getMethodArguments(objectClass);
        assertEquals(0, methodArguments.length);
    }

    @Test
    public void testGetMethodDefault() throws Exception {
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        assertEquals("public int hashCode() {\nCONTENT}\n\n", method);
    }

    @Test
    public void testGetMethodGenerateComment() throws Exception {
        when(data.generateComment()).thenReturn(true);
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        assertEquals("/**\n * {@inheritDoc}\n */\n" + "public int hashCode() {\nCONTENT}\n\n", method);
    }

    @Test
    public void testGetMethodAddOverride() throws Exception {
        mockSpecificManagers(true);
        String method = methodSkeleton.getMethod(objectClass, data, METHOD_CONTENT);
        assertEquals("@Override\npublic int hashCode() {\nCONTENT}\n\n", method);
    }

    private void mockSpecificManagers(boolean addOverride) throws Exception {
        mockIsSourceLevelAbove5(addOverride);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.ADD_OVERRIDE_ANNOTATION))
                .thenReturn(addOverride);
    }

}
