package org.jenerate.internal.ui.dialogs.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for the {@link TypeMethodFinder}
 *
 * @author helospark
 */
@RunWith(MockitoJUnitRunner.class)
public class TypeMethodFinderTest {
    private IMethod nonResolvedSignature;
    private IMethod nonResolvedFullyQualifiedSignature;
    private IMethod resolvedSignature;
    private IMethod overloadedWithObject;
    private IMethod overloadedWithString;
    @Mock
    private IType type;

    private TypeMethodFinder typeMethodFinder;

    @Before
    public void setUp() throws JavaModelException {
        nonResolvedSignature = createMockMethod("nonResolvedSignature", new String[] { "QObject;" });
        nonResolvedFullyQualifiedSignature = createMockMethod("nonResolvedFullyQualifiedSignature", new String[] { "Qjava.lang.Object;" });
        resolvedSignature = createMockMethod("resolvedSignature", new String[] { "Ljava.lang.Object;" });

        overloadedWithObject = createMockMethod("overloaded", new String[] { "Ljava.lang.Object;" });
        overloadedWithString = createMockMethod("overloaded", new String[] { "Ljava.lang.String;" });

        when(type.getMethods()).thenReturn(new IMethod[] {
                nonResolvedSignature,
                nonResolvedFullyQualifiedSignature,
                resolvedSignature,
                overloadedWithObject,
                overloadedWithString });

        typeMethodFinder = new TypeMethodFinder();
    }

    @Test
    public void testFindMethodWithNameAndParametersWithNonResolvedSignature() {
        IMethod result = typeMethodFinder.findMethodWithNameAndParameters(type, "nonResolvedSignature", new String[] { "java.lang.Object" });

        assertEquals(result, nonResolvedSignature);
    }

    @Test
    public void testFindMethodWithNameAndParametersWithNonResolvedFullyQualifiedSignature() {

        IMethod result = typeMethodFinder.findMethodWithNameAndParameters(type, "nonResolvedFullyQualifiedSignature", new String[] { "java.lang.Object" });

        assertEquals(result, nonResolvedFullyQualifiedSignature);
    }

    @Test
    public void testFindMethodWithNameAndParametersWithResolvedSignature() {
        IMethod result = typeMethodFinder.findMethodWithNameAndParameters(type, "resolvedSignature", new String[] { "java.lang.Object" });

        assertEquals(result, resolvedSignature);
    }

    @Test
    public void testFindMethodWithNameAndParametersWithOverloadedObject() {
        IMethod result = typeMethodFinder.findMethodWithNameAndParameters(type, "overloaded", new String[] { "java.lang.Object" });

        assertEquals(result, overloadedWithObject);
    }

    @Test
    public void testFindMethodWithNameAndParametersWithOverloadedString() {
        IMethod result = typeMethodFinder.findMethodWithNameAndParameters(type, "overloaded", new String[] { "java.lang.String" });

        assertEquals(result, overloadedWithString);
    }

    @Test
    public void testFindMethodWithNameAndParametersWhenMethodNameMatchButParameterNumberIsNot() {
        IMethod result = typeMethodFinder.findMethodWithNameAndParameters(type, "resolvedSignature", new String[0]);

        assertEquals(result, null);
    }

    @Test
    public void testFindMethodWithNameWhenParameterWhenParameterTypesDoNotMatch() {

        IMethod result = typeMethodFinder.findMethodWithNameAndParameters(type, "resolvedSignature", new String[] { "java.lang.String" });

        assertEquals(result, null);
    }

    @Test
    public void testFindMethodWithNameShouldReturnNullWhenGetMethodsThrow() throws JavaModelException {
        JavaModelException exception = mock(JavaModelException.class);
        when(type.getMethods()).thenThrow(exception);

        IMethod result = typeMethodFinder.findMethodWithNameAndParameters(type, "resolvedSignature", new String[] { "java.lang.String" });

        assertEquals(result, null);
    }

    private IMethod createMockMethod(String name, String[] parameters) {
        IMethod method = mock(IMethod.class);
        when(method.getElementName()).thenReturn(name);
        when(method.getParameterTypes()).thenReturn(parameters);
        return method;
    }
}
