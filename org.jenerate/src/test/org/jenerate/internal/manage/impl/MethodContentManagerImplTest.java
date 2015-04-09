package org.jenerate.internal.manage.impl;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.Method;
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
    private AbstractMethodSkeleton<CompareToGenerationData> abstractMethodSkeleton;

    @Mock
    private MethodSkeleton<CompareToGenerationData> methodSkeleton1;
    @Mock
    private MethodSkeleton<CompareToGenerationData> methodSkeleton2;
    private LinkedHashSet<MethodSkeleton<CompareToGenerationData>> methodSkeletons;

    private MethodContentManagerImpl methodContentManager;

    @Before
    public void setUp() {
        methodSkeletons = new LinkedHashSet<MethodSkeleton<CompareToGenerationData>>();
        methodContentManager = new MethodContentManagerImpl(preferencesManager, javaInterfaceCodeAppender);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithUnknownSkeletonAndIdentifier() {
        methodSkeletons.add(methodSkeleton1);
        methodContentManager.getAllMethods(methodSkeletons, strategyIdentifier);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithUnknownSkeleton() {
        methodSkeletons.add(methodSkeleton1);
        methodContentManager.getAllMethods(methodSkeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithUnknownIdentifier() {
        methodSkeletons.add(new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
        methodContentManager.getAllMethods(methodSkeletons, strategyIdentifier);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithSkeletonSubclass() {
        methodSkeletons.add(new TestMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
        methodContentManager.getAllMethods(methodSkeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithSkeletonAbstractClass() {
        methodSkeletons.add(new TestAbstractMethodSkeleton(preferencesManager));
        methodContentManager.getAllMethods(methodSkeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test
    public void testWithCompareToMethodSkeletonAndLang3Identifier() {
        methodSkeletons.add(new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
        LinkedHashSet<Method<MethodSkeleton<CompareToGenerationData>, CompareToGenerationData>> methods = methodContentManager
                .getAllMethods(methodSkeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG3);
        assertEquals(1, methods.size());
        Method<MethodSkeleton<CompareToGenerationData>, CompareToGenerationData> method = methods.iterator().next();
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG3, method.getMethodContent()
                .getStrategyIdentifier());
        assertEquals(CompareToMethodSkeleton.class, method.getMethodContent().getRelatedMethodSkeletonClass());
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
        public MethodsGenerationCommandIdentifier getCommandIdentifier() {
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
