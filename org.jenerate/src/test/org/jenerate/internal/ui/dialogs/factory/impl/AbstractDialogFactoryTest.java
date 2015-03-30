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
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;
import org.jenerate.internal.ui.dialogs.impl.AbstractFieldDialog;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * Abstract test for the {@link DialogFactory}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractDialogFactoryTest {

    @Mock
    protected DialogFactoryHelper dialogFactoryHelper;
    @Mock
    protected PreferencesManager preferencesManager;

    @Mock
    protected IDialogSettings dialogSettings;
    @Mock
    protected IType objectClass;
    @Mock
    protected Shell parentShell;

    @Mock
    protected IMethod method1;
    @Mock
    protected IMethod method2;
    protected Set<IMethod> excludedMethods;

    @Mock
    protected IField field1;
    @Mock
    protected IField field2;

    @Before
    public void setUp() throws Exception {
        mockDialogSettings();
        mockObjectClass();
        excludedMethods = new HashSet<IMethod>();
        excludedMethods.add(method1);
        excludedMethods.add(method2);
        callbackAfterSetUp();
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

    protected IField[] createFields(IField... fields) {
        return fields;
    }

    /**
     * Callback after the Junit Before annotated setUp method is called
     * 
     * @throws Exception if an exception occur
     */
    public abstract void callbackAfterSetUp() throws Exception;

}
