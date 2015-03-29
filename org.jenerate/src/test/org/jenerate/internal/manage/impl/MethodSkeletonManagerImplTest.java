package org.jenerate.internal.manage.impl;

import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link MethodSkeletonManagerImpl}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class MethodSkeletonManagerImplTest {

    @Mock
    private PreferencesManager preferencesManager;
    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;
    @Mock
    private CommandIdentifier unknownCommandIdentifier;

    private MethodSkeletonManagerImpl methodSkeletonManager;

    @Before
    public void setUp() {
        methodSkeletonManager = new MethodSkeletonManagerImpl(preferencesManager, javaInterfaceCodeAppender);
    }

    @Test
    public void testWithUnknownCommandIdentifier() {
        assertTrue(methodSkeletonManager.getMethodSkeletons(unknownCommandIdentifier).isEmpty());
    }

    @Test
    public void testWithEqualsHashCodeIdentifier() {
        assertNotNull(methodSkeletonManager.getMethodSkeletons(MethodsGenerationCommandIdentifier.EQUALS_HASH_CODE));
    }

}
