package org.jenerate.internal.ui.dialogs.factory.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.impl.OrderableFieldDialogImpl;
import org.jenerate.internal.ui.dialogs.strategy.commonslang.CommonsLangToStringDialogStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for the {@link ToStringDialogFactory}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ToStringDialogFactoryTest extends AbstractDialogFactoryTest {

    private ToStringDialogFactory toStringDialogFactory;

    @Override
    public void callbackAfterSetUp() throws Exception {
        when(dialogSettings.getSection(CommonsLangToStringDialogStrategy.SETTINGS_SECTION)).thenReturn(dialogSettings);
        mockDisableAppendSuper(false);
        toStringDialogFactory = new ToStringDialogFactory(dialogStrategyManager, dialogFactoryHelper,
                preferencesManager);
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

    private void validateDialogCreation(IField[] iFields, boolean disableAppendSuper)
            throws JavaModelException, Exception {
        when(dialogFactoryHelper.getObjectClassFields(objectClass, preferencesManager)).thenReturn(iFields);
        FieldDialog<ToStringGenerationData> toStringDialog = toStringDialogFactory.createDialog(parentShell,
                objectClass, excludedMethods, possibleStrategyIdentifiers);
        assertArrayEquals(iFields, ((OrderableFieldDialogImpl<ToStringGenerationData>) toStringDialog).getFields());
        assertEquals(disableAppendSuper,
                !((OrderableFieldDialogImpl<ToStringGenerationData>) toStringDialog).getAppendSuper());
    }

    private void mockDisableAppendSuper(boolean isDisableAppendSuper) throws JavaModelException {
        when(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, "toString", new String[0], null))
                .thenReturn(!isDisableAppendSuper);
    }
}
