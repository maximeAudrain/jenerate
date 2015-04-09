package org.jenerate.internal.ui.dialogs.factory.impl;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.ui.dialogs.impl.ToStringDialog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ToStringDialogFactory}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class ToStringDialogFactoryTest extends AbstractDialogFactoryTest {

    private ToStringDialogFactory toStringDialogFactory;

    @Override
    public void callbackAfterSetUp() throws Exception {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.PREFERED_COMMON_METHODS_CONTENT_STRATEGY))
                .thenReturn(MethodContentStrategyIdentifier.USE_COMMONS_LANG);
        when(dialogSettings.getSection(ToStringDialog.SETTINGS_SECTION)).thenReturn(dialogSettings);
        mockDisableAppendSuper(false);
        toStringDialogFactory = new ToStringDialogFactory(dialogFactoryHelper, preferencesManager);
    }

    @Test
    public void testGetCommandIdentifier() {
        assertEquals(MethodsGenerationCommandIdentifier.TO_STRING, toStringDialogFactory.getCommandIdentifier());
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
    public void testCreateDialogWithFieldsAndDisableAppendSuperWithJavaLangObject() throws Exception {
        mockDisableAppendSuper(true);
        validateDialogCreation(createFields(field1, field2), true);
    }

    private void validateDialogCreation(IField[] iFields, boolean disableAppendSuper) throws JavaModelException,
            Exception {
        when(dialogFactoryHelper.getObjectClassFields(objectClass, preferencesManager)).thenReturn(iFields);
        ToStringDialog toStringDialog = toStringDialogFactory.createDialog(parentShell, objectClass, excludedMethods);
        assertArrayEquals(iFields, toStringDialog.getFields());
        assertEquals(disableAppendSuper, !toStringDialog.getAppendSuper());
    }

    private void mockDisableAppendSuper(boolean isDisableAppendSuper) throws JavaModelException {
        when(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, "toString", new String[0], null)).thenReturn(
                !isDisableAppendSuper);
    }
}
