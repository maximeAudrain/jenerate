package org.jenerate.internal.ui.dialogs.factory.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.DialogStrategyManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;
import org.jenerate.internal.ui.dialogs.impl.FieldDialogImpl;
import org.jenerate.internal.ui.dialogs.strategy.DialogStrategy;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Abstract test for the {@link DialogFactory}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractDialogFactoryTest {

    @Mock
    protected DialogStrategyManager dialogStrategyManager;
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

    protected LinkedHashSet<StrategyIdentifier> possibleStrategyIdentifiers;
    @Mock
    protected StrategyIdentifier identifier1;
    @Mock
    protected StrategyIdentifier identifier2;

    @Mock
    private DialogStrategy<MethodGenerationData> dialogStrategy;

    @Before
    public void setUp() throws Exception {
        mockDialogSettings();
        mockObjectClass();
        excludedMethods = new HashSet<IMethod>();
        excludedMethods.add(method1);
        excludedMethods.add(method2);
        possibleStrategyIdentifiers = new LinkedHashSet<StrategyIdentifier>();
        possibleStrategyIdentifiers.add(identifier1);
        possibleStrategyIdentifiers.add(identifier2);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.PREFERED_COMMON_METHODS_CONTENT_STRATEGY))
                .thenReturn(identifier1);
        when(dialogStrategyManager.getDialogStrategy(any(CommandIdentifier.class), any(StrategyIdentifier.class)))
                .thenReturn(dialogStrategy);
        callbackAfterSetUp();
    }

    private void mockObjectClass() throws JavaModelException {
        when(objectClass.getChildren()).thenReturn(new IJavaElement[0]);
        when(objectClass.getMethods()).thenReturn(new IMethod[0]);
    }

    private void mockDialogSettings() {
        when(dialogSettings.getBoolean(FieldDialogImpl.SETTINGS_APPEND_SUPER)).thenReturn(true);
        when(dialogSettings.getSection(FieldDialogImpl.ABSTRACT_SETTINGS_SECTION)).thenReturn(dialogSettings);
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
