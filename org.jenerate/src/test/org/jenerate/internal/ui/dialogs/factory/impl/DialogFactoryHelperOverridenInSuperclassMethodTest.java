package org.jenerate.internal.ui.dialogs.factory.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for the {@link DialogFactoryHelperImpl}
 *
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class DialogFactoryHelperOverridenInSuperclassMethodTest {

    private static final String PARAMETER = "QObject;";
    private static final String SUPERCLASS_2_NAME = "superclass_2_name";
    private static final String SUPERCLASS_1_NAME = "superclass_1_name";
    private static final String METHOD_1_NAME = "method1";
    @Mock
    private IType objectClass;
    @Mock
    private ITypeHierarchy typeHierarchy;
    @Mock
    private IType superclass1;
    @Mock
    private IType superclass2;
    @Mock
    private IMethod method1;
    @Mock
    private IMethod method2;
    @Mock
    private TypeMethodFinder typeMethodFinder;

    private DialogFactoryHelperImpl dialogFactoryHelper;

    @Before
    public void setUp() {
        mockSuperClass1();
        mockSuperClass2();
        dialogFactoryHelper = new DialogFactoryHelperImpl(typeMethodFinder);
    }

    @Test
    public void testNoSuperClasses() throws JavaModelException {
        mockObjectClass();
        assertFalse(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, METHOD_1_NAME, new String[] { PARAMETER },
                SUPERCLASS_1_NAME));
    }

    @Test
    public void testOneSuperClassFullyQualifiedNameMatch() throws JavaModelException {
        mockObjectClass(superclass1);
        assertFalse(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, METHOD_1_NAME, new String[] { PARAMETER },
                SUPERCLASS_1_NAME));
    }

    @Test
    public void testOneSuperClassMethodDoesNotExist() throws JavaModelException {
        mockObjectClass(superclass1);
        mockTypeMethodFinder(superclass1, null);
        assertFalse(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, METHOD_1_NAME, new String[] { PARAMETER },
                null));
    }

    @Test
    public void testOneSuperClassAbstractMethodExist() throws JavaModelException {
        mockMethod(method1, true);
        mockObjectClass(superclass1);
        mockTypeMethodFinder(superclass1, method1);
        assertFalse(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, METHOD_1_NAME, new String[] { PARAMETER },
                null));
    }

    @Test
    public void testOneSuperClassMethodExist() throws JavaModelException {
        mockMethod(method1, false);
        mockObjectClass(superclass1);
        mockTypeMethodFinder(superclass1, method1);
        assertTrue(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, METHOD_1_NAME, new String[] { PARAMETER },
                null));
    }

    @Test
    public void testTwoSuperClassesReturnSinceFirstMethodAbstract() throws JavaModelException {
        mockMethod(method1, true);
        mockMethod(method2, false);
        mockObjectClass(superclass1, superclass2);
        mockTypeMethodFinder(superclass1, method1);
        mockTypeMethodFinder(superclass2, method2);
        assertFalse(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, METHOD_1_NAME, new String[] { PARAMETER },
                null));
    }

    @Test
    public void testTwoSuperClassesBothMethodsDesNotExists() throws JavaModelException {
        mockMethod(method1, false);
        mockMethod(method2, false);
        mockObjectClass(superclass1, superclass2);
        mockTypeMethodFinder(superclass1, null);
        mockTypeMethodFinder(superclass2, null);
        assertFalse(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, METHOD_1_NAME, new String[] { PARAMETER },
                null));
    }

    private void mockSuperClass1() {
        when(superclass1.getFullyQualifiedName()).thenReturn(SUPERCLASS_1_NAME);
        when(superclass1.getMethod(METHOD_1_NAME, new String[] { PARAMETER })).thenReturn(method1);
    }

    private void mockSuperClass2() {
        when(superclass2.getFullyQualifiedName()).thenReturn(SUPERCLASS_2_NAME);
        when(superclass2.getMethod(METHOD_1_NAME, new String[] { PARAMETER })).thenReturn(method2);
    }

    private void mockObjectClass(IType... types) throws JavaModelException {
        when(objectClass.newSupertypeHierarchy(null)).thenReturn(typeHierarchy);
        when(typeHierarchy.getAllSuperclasses(objectClass)).thenReturn(types);
    }

    private void mockMethod(IMethod method, boolean isAbstract) throws JavaModelException {
        when(method.getFlags()).thenReturn(isAbstract ? 0x400 : 0);
    }

    private void mockTypeMethodFinder(IType superclass12, IMethod object) {
        when(typeMethodFinder.findMethodWithNameAndParameters(superclass12, METHOD_1_NAME, new String[] { PARAMETER })).thenReturn(object);
    }

}
