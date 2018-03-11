package org.jenerate.internal.ui.dialogs.factory.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.impl.OrderableFieldDialogImpl;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for the {@link CompareToDialogFactory}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class CompareToDialogFactoryTest extends AbstractDialogFactoryTest {

    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    private CompareToDialogFactory compareToDialogFactory;

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockDisableAppendSuper(false);
        compareToDialogFactory = new CompareToDialogFactory(dialogStrategyManager, dialogFactoryHelper,
                preferencesManager, javaInterfaceCodeAppender);
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

    private void validateDialogCreation(IField[] iFields, boolean disableAppendSuper)
            throws JavaModelException, Exception {
        when(dialogFactoryHelper.getObjectClassFields(objectClass, preferencesManager)).thenReturn(iFields);
        FieldDialog<CompareToGenerationData> compareToDialog = compareToDialogFactory.createDialog(parentShell,
                objectClass, excludedMethods, possibleStrategyIdentifiers);
        assertArrayEquals(iFields, ((OrderableFieldDialogImpl<CompareToGenerationData>) compareToDialog).getFields());
        assertEquals(disableAppendSuper,
                !((OrderableFieldDialogImpl<CompareToGenerationData>) compareToDialog).getAppendSuper());
    }

    private void mockDisableAppendSuper(boolean isDisableAppendSuper) throws JavaModelException {
        when(dialogFactoryHelper.isOverriddenInSuperclass(objectClass, "compareTo", new String[] { "java.lang.Object" },
                null)).thenReturn(!isDisableAppendSuper);
        when(javaInterfaceCodeAppender.isImplementedInSupertype(objectClass, "Comparable"))
                .thenReturn(!isDisableAppendSuper);
    }
}
