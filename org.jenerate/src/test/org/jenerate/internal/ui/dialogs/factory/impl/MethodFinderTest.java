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
 * Unit tests for the {@link MethodFinder}
 *
 * @author helospark
 */
@RunWith(MockitoJUnitRunner.class)
public class MethodFinderTest {
    private IMethod nonResolvedMethod;
    private IMethod nonResolvedFullyQualifiedMethod;
    private IMethod resolvedMethod;
    private IMethod overloadedWithObjectMethod;
    private IMethod overloadedWithStringMethod;
    @Mock
    private IType type;

    private MethodFinder methodFinder;

    @Before
    public void setUp() throws JavaModelException {
        nonResolvedMethod = createMockMethod("nonResolvedMethod", new String[] { "QObject;" });
        resolvedMethod = createMockMethod("resolvedMethod", new String[] { "Ljava.lang.Object;" });
        nonResolvedFullyQualifiedMethod = createMockMethod("nonResolvedFullyQualifiedMethod",
                new String[] { "Qjava.lang.Object;" });

        overloadedWithObjectMethod = createMockMethod("overloaded", new String[] { "Ljava.lang.Object;" });
        overloadedWithStringMethod = createMockMethod("overloaded", new String[] { "Ljava.lang.String;" });

        when(type.getMethods()).thenReturn(new IMethod[] { nonResolvedMethod, nonResolvedFullyQualifiedMethod,
                resolvedMethod, overloadedWithObjectMethod, overloadedWithStringMethod });

        methodFinder = new MethodFinder();
    }

    @Test
    public void testFindMethodWithNameAndParametersWithnonResolved() throws JavaModelException {
        IMethod result = methodFinder.findMethodWithNameAndParameters(type, "nonResolvedMethod",
                new String[] { "java.lang.Object" });

        assertEquals(result, nonResolvedMethod);
    }

    @Test
    public void testFindMethodWithNameAndParametersWithnonResolvedFullyQualified() throws JavaModelException {
        IMethod result = methodFinder.findMethodWithNameAndParameters(type, "nonResolvedFullyQualifiedMethod",
                new String[] { "java.lang.Object" });

        assertEquals(result, nonResolvedFullyQualifiedMethod);
    }

    @Test
    public void testFindMethodWithNameAndParametersWithresolved() throws JavaModelException {
        IMethod result = methodFinder.findMethodWithNameAndParameters(type, "resolvedMethod",
                new String[] { "java.lang.Object" });

        assertEquals(result, resolvedMethod);
    }

    @Test
    public void testFindMethodWithNameAndParametersWithOverloadedObject() throws JavaModelException {
        IMethod result = methodFinder.findMethodWithNameAndParameters(type, "overloaded",
                new String[] { "java.lang.Object" });

        assertEquals(result, overloadedWithObjectMethod);
    }

    @Test
    public void testFindMethodWithNameAndParametersWithOverloadedString() throws JavaModelException {
        IMethod result = methodFinder.findMethodWithNameAndParameters(type, "overloaded",
                new String[] { "java.lang.String" });

        assertEquals(overloadedWithStringMethod, result);
    }

    @Test
    public void testFindMethodWithNameAndParametersWhenMethodNameMatchButParameterNumberIsNot()
            throws JavaModelException {
        IMethod result = methodFinder.findMethodWithNameAndParameters(type, "resolvedMethod", new String[0]);

        assertEquals(null, result);
    }

    @Test
    public void testFindMethodWithNameWhenParameterWhenParameterTypesDoNotMatch() throws JavaModelException {
        IMethod result = methodFinder.findMethodWithNameAndParameters(type, "resolvedMethod",
                new String[] { "java.lang.String" });

        assertEquals(null, result);
    }

    @Test(expected = JavaModelException.class)
    public void testFindMethodWithNameShouldThrowExceptionWhenGetMethodsThrow() throws JavaModelException {
        JavaModelException exception = mock(JavaModelException.class);
        when(type.getMethods()).thenThrow(exception);

        methodFinder.findMethodWithNameAndParameters(type, "resolvedMethod", new String[] { "java.lang.String" });
    }

    private IMethod createMockMethod(String name, String[] parameters) {
        IMethod method = mock(IMethod.class);
        when(method.getElementName()).thenReturn(name);
        when(method.getParameterTypes()).thenReturn(parameters);
        return method;
    }
}
