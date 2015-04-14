package org.jenerate.internal.manage.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.AbstractMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testGetPossibleStrategiesWithUnknownSkeleton() {
        methodSkeletons.add(methodSkeleton1);
        LinkedHashSet<StrategyIdentifier> possibleStrategies = methodContentManager
                .getStrategiesIntersection(methodSkeletons);
        assertTrue(possibleStrategies.isEmpty());
    }

    @Test
    public void testGetPossibleStrategiesWithSkeletonSubclass() {
        methodSkeletons.add(new TestMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
        LinkedHashSet<StrategyIdentifier> possibleStrategies = methodContentManager
                .getStrategiesIntersection(methodSkeletons);
        assertTrue(possibleStrategies.isEmpty());
    }

    @Test
    public void testGetPossibleStrategiesWithSkeletonAbstractClass() {
        methodSkeletons.add(new TestAbstractMethodSkeleton(preferencesManager));
        LinkedHashSet<StrategyIdentifier> possibleStrategies = methodContentManager
                .getStrategiesIntersection(methodSkeletons);
        assertTrue(possibleStrategies.isEmpty());
    }

    @Test
    public void testGetPossibleStrategiesWithOneSkeleton() {
        methodSkeletons.add(new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
        LinkedHashSet<StrategyIdentifier> possibleStrategies = methodContentManager
                .getStrategiesIntersection(methodSkeletons);
        assertEquals(2, possibleStrategies.size());
    }

    @Test
    public void testGetPossibleStrategiesWithTwoSkeletons() {
        LinkedHashSet<MethodSkeleton<EqualsHashCodeGenerationData>> skeletons = new LinkedHashSet<>();
        EqualsMethodSkeleton skeleton1 = new EqualsMethodSkeleton(preferencesManager);
        HashCodeMethodSkeleton skeleton2 = new HashCodeMethodSkeleton(preferencesManager);
        skeletons.add(skeleton1);
        skeletons.add(skeleton2);
        LinkedHashSet<StrategyIdentifier> possibleStrategies = methodContentManager
                .getStrategiesIntersection(skeletons);
        assertEquals(3, possibleStrategies.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAllMethodsWithUnknownSkeletonAndIdentifier() {
        methodSkeletons.add(methodSkeleton1);
        methodContentManager.getAllMethods(methodSkeletons, strategyIdentifier);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAllMethodsWithUnknownSkeleton() {
        methodSkeletons.add(methodSkeleton1);
        methodContentManager.getAllMethods(methodSkeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAllMethodsWithUnknownIdentifier() {
        methodSkeletons.add(new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
        methodContentManager.getAllMethods(methodSkeletons, strategyIdentifier);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAllMethodsWithSkeletonSubclass() {
        methodSkeletons.add(new TestMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
        methodContentManager.getAllMethods(methodSkeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAllMethodsWithSkeletonAbstractClass() {
        methodSkeletons.add(new TestAbstractMethodSkeleton(preferencesManager));
        methodContentManager.getAllMethods(methodSkeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG);
    }

    @Test
    public void testGetAllMethodsWithCompareToMethodSkeletonAndLang3Identifier() {
        methodSkeletons.add(new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
        LinkedHashSet<Method<MethodSkeleton<CompareToGenerationData>, CompareToGenerationData>> methods = methodContentManager
                .getAllMethods(methodSkeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG3);
        assertEquals(1, methods.size());
        Method<MethodSkeleton<CompareToGenerationData>, CompareToGenerationData> method = methods.iterator().next();
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG3, method.getMethodContent()
                .getStrategyIdentifier());
        assertEquals(CompareToMethodSkeleton.class, method.getMethodContent().getRelatedMethodSkeletonClass());
    }

    @Test
    public void testGetAllMethodsWithTwoSkeletonsAndLangIdentifier() {
        LinkedHashSet<MethodSkeleton<EqualsHashCodeGenerationData>> skeletons = new LinkedHashSet<>();
        EqualsMethodSkeleton skeleton1 = new EqualsMethodSkeleton(preferencesManager);
        HashCodeMethodSkeleton skeleton2 = new HashCodeMethodSkeleton(preferencesManager);
        skeletons.add(skeleton1);
        skeletons.add(skeleton2);
        LinkedHashSet<Method<MethodSkeleton<EqualsHashCodeGenerationData>, EqualsHashCodeGenerationData>> methods = methodContentManager
                .getAllMethods(skeletons, MethodContentStrategyIdentifier.USE_COMMONS_LANG);

        assertEquals(2, methods.size());
        Iterator<Method<MethodSkeleton<EqualsHashCodeGenerationData>, EqualsHashCodeGenerationData>> iterator = methods
                .iterator();

        MethodContent<MethodSkeleton<EqualsHashCodeGenerationData>, EqualsHashCodeGenerationData> methodContent = iterator
                .next().getMethodContent();
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG, methodContent.getStrategyIdentifier());
        assertEquals(EqualsMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());

        MethodContent<MethodSkeleton<EqualsHashCodeGenerationData>, EqualsHashCodeGenerationData> methodContent2 = iterator
                .next().getMethodContent();
        assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG, methodContent2.getStrategyIdentifier());
        assertEquals(HashCodeMethodSkeleton.class, methodContent2.getRelatedMethodSkeletonClass());
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
