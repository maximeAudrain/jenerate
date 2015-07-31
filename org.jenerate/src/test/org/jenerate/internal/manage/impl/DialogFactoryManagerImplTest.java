package org.jenerate.internal.manage.impl;

import static org.junit.Assert.assertNotNull;

import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for the {@link DialogFactoryManagerImpl}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class DialogFactoryManagerImplTest {

    @Mock
    private PreferencesManager preferencesManager;
    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    @Mock
    private CommandIdentifier unknownCommandIdentifier;

    private DialogFactoryManagerImpl dialogFactoryManager;

    @Before
    public void SetUp() {
        dialogFactoryManager = new DialogFactoryManagerImpl(preferencesManager, javaInterfaceCodeAppender);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithUnknownCommandIdentifier() {
        dialogFactoryManager.getDialogFactory(unknownCommandIdentifier);
    }

    @Test
    public void testWithCompareToCommandIdentifier() {
        assertNotNull(dialogFactoryManager.getDialogFactory(MethodsGenerationCommandIdentifier.COMPARE_TO));
    }

}
