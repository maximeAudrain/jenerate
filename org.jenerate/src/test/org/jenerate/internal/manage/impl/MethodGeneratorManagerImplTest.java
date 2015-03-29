package org.jenerate.internal.manage.impl;

import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.generate.method.util.JavaCodeFormatter;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Unit test for the {@link MethodGeneratorManagerImpl}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class MethodGeneratorManagerImplTest {

    @Mock
    private PreferencesManager preferencesManager;
    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;
    @Mock
    private JavaUiCodeAppender javaUiCodeAppender;
    @Mock
    private JavaCodeFormatter javaCodeFormatter;
    @Mock
    private CommandIdentifier unknownCommandIdentifier;

    private MethodGeneratorManagerImpl methodGeneratorManager;

    @Before
    public void setUp() {
        methodGeneratorManager = new MethodGeneratorManagerImpl(preferencesManager, javaInterfaceCodeAppender,
                javaUiCodeAppender, javaCodeFormatter);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithUnknownCommandIdentifier() {
        methodGeneratorManager.getMethodGenerator(unknownCommandIdentifier);
    }

    @Test
    public void testWithToStringCommandIdentifier() {
        assertNotNull(methodGeneratorManager.getMethodGenerator(MethodsGenerationCommandIdentifier.TO_STRING));

    }
}
