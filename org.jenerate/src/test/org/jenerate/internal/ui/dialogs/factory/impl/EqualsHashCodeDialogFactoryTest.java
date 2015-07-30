package org.jenerate.internal.ui.dialogs.factory.impl;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.impl.FieldDialogImpl;
import org.jenerate.internal.ui.dialogs.strategy.EqualsHashCodeDialogStrategyHelper;
import org.jenerate.internal.ui.dialogs.strategy.commonslang.CommonsLangEqualsHashCodeDialogStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EqualsHashCodeDialogFactory}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class EqualsHashCodeDialogFactoryTest extends AbstractDialogFactoryTest {

    private EqualsHashCodeDialogFactory equalsHashCodeDialogFactory;

    @Override
    public void callbackAfterSetUp() throws Exception {
        when(dialogSettings.getSection(EqualsHashCodeDialogStrategyHelper.EQUALS_SETTINGS_SECTION)).thenReturn(
                dialogSettings);
        when(dialogSettings.getSection(CommonsLangEqualsHashCodeDialogStrategy.HASHCODE_SETTINGS_SECTION)).thenReturn(
                dialogSettings);
        mockDisableAppendSuper(false);
        equalsHashCodeDialogFactory = new EqualsHashCodeDialogFactory(dialogStrategyManager, dialogFactoryHelper,
                preferencesManager);
    }

    @Test
    public void testGetCommandIdentifier() {
        assertEquals(MethodsGenerationCommandIdentifier.EQUALS_HASH_CODE,
                equalsHashCodeDialogFactory.getCommandIdentifier());
    }

    @Test
    public void testCreateDialogWithEmptyFieldsAndNoDisableAppendSuper() throws Exception {
        when(objectClass.getSuperclassName()).thenReturn("SuperClass");
        validateDialogCreation(createFields(), false);
    }

    @Test
    public void testCreateDialogWithFieldsAndNoDisableAppendSuper() throws Exception {
        when(objectClass.getSuperclassName()).thenReturn("SuperClass");
        validateDialogCreation(createFields(field1, field2), false);
    }

    @Test
    public void testCreateDialogWithFieldsAndDisableAppendSuperWithJavaLangObject() throws Exception {
        when(objectClass.getSuperclassName()).thenReturn("java.lang.Object");
        mockDisableAppendSuper(true);
        validateDialogCreation(createFields(field1, field2), true);
    }

    @Test
    public void testCreateDialogWithFieldsAndDisableAppendSuperWithObject() throws Exception {
        when(objectClass.getSuperclassName()).thenReturn("Object");
        mockDisableAppendSuper(true);
        validateDialogCreation(createFields(field1, field2), true);
    }

    @Test
    public void testCreateDialogWithFieldsAndDisableAppendSuperWithNull() throws Exception {
        when(objectClass.getSuperclassName()).thenReturn(null);
        mockDisableAppendSuper(true);
        validateDialogCreation(createFields(field1, field2), true);
    }

    private void validateDialogCreation(IField[] iFields, boolean disableAppendSuper) throws JavaModelException,
            Exception {
        when(dialogFactoryHelper.getObjectClassFields(objectClass, preferencesManager)).thenReturn(iFields);
        FieldDialog<EqualsHashCodeGenerationData> equalsHashCodeDialog = equalsHashCodeDialogFactory.createDialog(
                parentShell, objectClass, excludedMethods, possibleStrategyIdentifiers);
        assertArrayEquals(iFields, ((FieldDialogImpl<EqualsHashCodeGenerationData>) equalsHashCodeDialog).getFields());
        assertEquals(disableAppendSuper,
                !((FieldDialogImpl<EqualsHashCodeGenerationData>) equalsHashCodeDialog).getAppendSuper());
    }

    private void mockDisableAppendSuper(boolean isDisableAppendSuper) throws JavaModelException {
        when(
                dialogFactoryHelper.isOverriddenInSuperclass(objectClass, "equals", new String[] { "QObject;" },
                        "java.lang.Object")).thenReturn(!isDisableAppendSuper);
        when(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, "hashCode", new String[0], "java.lang.Object"))
                .thenReturn(!isDisableAppendSuper);
    }
}
