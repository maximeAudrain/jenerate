package org.jenerate.internal.ui.dialogs.factory.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;
import org.jenerate.internal.ui.dialogs.impl.AbstractFieldDialog;
import org.jenerate.internal.ui.dialogs.impl.CompareToDialog;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link CompareToDialogFactory}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class CompareToDialogFactoryTest {

    @Mock
    private DialogFactoryHelper dialogFactoryHelper;
    @Mock
    private PreferencesManager preferencesManager;
    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    @Mock
    private IDialogSettings dialogSettings;
    @Mock
    private IType objectClass;
    @Mock
    private Shell parentShell;

    @Mock
    private IMethod method1;
    @Mock
    private IMethod method2;
    private Set<IMethod> excludedMethods;

    @Mock
    private IField field1;
    @Mock
    private IField field2;

    private CompareToDialogFactory compareToDialogFactory;

    @Before
    public void setUp() throws JavaModelException {
        mockDialogSettings();

        mockObjectClass();

        excludedMethods = new HashSet<IMethod>();
        excludedMethods.add(method1);
        excludedMethods.add(method2);
        mockDisableAppendSuper(false);
        compareToDialogFactory = new CompareToDialogFactory(dialogFactoryHelper, preferencesManager,
                javaInterfaceCodeAppender);
    }

    @Test
    public void testGetCommandIdentifier() {
        assertEquals(MethodsGenerationCommandIdentifier.COMPARE_TO, compareToDialogFactory.getCommandIdentifier());
    }

    @Test
    public void testCreateDialogWithEmptyFieldsAndNoDisableAppendSuper() throws Exception {
        validateDialogCreation(createFields(), false);
    }

    @Test
    public void testCreateDialogWithFieldsAndNoDisableAppendSuper() throws Exception {
        validateDialogCreation(createFields(field1, field2), false);
    }

    @Test
    public void testCreateDialogWithFieldsAndDisableAppendSuper() throws Exception {
        mockDisableAppendSuper(true);
        validateDialogCreation(createFields(field1, field2), true);
    }

    private void validateDialogCreation(IField[] iFields, boolean disableAppendSuper) throws JavaModelException,
            Exception {
        when(dialogFactoryHelper.getObjectClassFields(objectClass, preferencesManager)).thenReturn(iFields);
        CompareToDialog compareToDialog = compareToDialogFactory
                .createDialog(parentShell, objectClass, excludedMethods);
        assertArrayEquals(iFields, compareToDialog.getFields());
        assertEquals(disableAppendSuper, !compareToDialog.getAppendSuper());
    }

    private void mockDisableAppendSuper(boolean isDisableAppendSuper) throws JavaModelException {
        when(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, "compareTo", new String[] { "QObject;" }, null))
                .thenReturn(!isDisableAppendSuper);
        when(javaInterfaceCodeAppender.isImplementedInSupertype(objectClass, "Comparable")).thenReturn(
                !isDisableAppendSuper);
    }

    private void mockObjectClass() throws JavaModelException {
        when(objectClass.getChildren()).thenReturn(new IJavaElement[0]);
        when(objectClass.getMethods()).thenReturn(new IMethod[0]);
    }

    private void mockDialogSettings() {
        when(dialogSettings.getBoolean(AbstractFieldDialog.SETTINGS_APPEND_SUPER)).thenReturn(true);
        when(dialogSettings.getSection(AbstractFieldDialog.ABSTRACT_SETTINGS_SECTION)).thenReturn(dialogSettings);
        when(dialogFactoryHelper.getDialogSettings()).thenReturn(dialogSettings);
    }

    private IField[] createFields(IField... fields) {
        return fields;
    }
}
