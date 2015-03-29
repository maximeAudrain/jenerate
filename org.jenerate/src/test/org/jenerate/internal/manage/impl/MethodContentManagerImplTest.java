package org.jenerate.internal.manage.impl;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.AbstractMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link MethodContentManagerImpl}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class MethodContentManagerImplTest {

    @Mock
    private PreferencesManager preferencesManager;
    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;
    @Mock
    private StrategyIdentifier strategyIdentifier;
    @Mock
    private MethodSkeleton<MethodGenerationData> methodSkeleton;
    @Mock
    private AbstractMethodSkeleton<CompareToGenerationData> abstractMethodSkeleton;

    private MethodContentManagerImpl methodContentManager;

    @Before
    public void setUp() {
        methodContentManager = new MethodContentManagerImpl(preferencesManager, javaInterfaceCodeAppender);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithUnknownSkeletonAndIdentifier() {
        methodContentManager.getMethodContent(methodSkeleton, strategyIdentifier);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithUnknownSkeleton() {
        methodContentManager.getMethodContent(methodSkeleton, MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithUnknownIdentifier() {
        methodContentManager.getMethodContent(
                new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender), strategyIdentifier);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithSkeletonSubclass() {
        methodContentManager.getMethodContent(new TestMethodSkeleton(preferencesManager, javaInterfaceCodeAppender),
                MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithSkeletonAbstractClass() {
        methodContentManager.getMethodContent(new TestAbstractMethodSkeleton(preferencesManager),
                MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test
    public void testWithCompareToMethodSkeletonAndLang3Identifier() {
        MethodContent<MethodSkeleton<CompareToGenerationData>, CompareToGenerationData> methodContent = methodContentManager
                .getMethodContent(new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender),
                        MethodContentStrategyIdentifier.USE_COMMONS_LANG3);
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG3, methodContent.getStrategyIdentifier());
        assertEquals(CompareToMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());
    }

    private class TestMethodSkeleton extends CompareToMethodSkeleton {

        public TestMethodSkeleton(PreferencesManager preferencesManager,
                JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
            super(preferencesManager, javaInterfaceCodeAppender);
        }

    }

    private class TestAbstractMethodSkeleton extends AbstractMethodSkeleton<CompareToGenerationData> {

        public TestAbstractMethodSkeleton(PreferencesManager preferencesManager) {
            super(preferencesManager);
        }

        @Override
        public String getMethod(IType objectClass, CompareToGenerationData data, String methodContent) throws Exception {
            return null;
        }

        @Override
        public MethodsGenerationCommandIdentifier getUserActionIdentifier() {
            return null;
        }

        @Override
        public String getMethodName() {
            return null;
        }

        @Override
        public String[] getMethodArguments(IType objectClass) throws Exception {
            return null;
        }
    }

}
