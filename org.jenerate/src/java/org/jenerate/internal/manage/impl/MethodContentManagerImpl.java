package org.jenerate.internal.manage.impl;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.manage.MethodContentManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangCompareToMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangEqualsMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangHashCodeMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringMethodContent;
import org.jenerate.internal.strategy.method.content.impl.guava.GuavaCompareToMethodContent;
import org.jenerate.internal.strategy.method.content.impl.guava.GuavaEqualsMethodContent;
import org.jenerate.internal.strategy.method.content.impl.guava.GuavaHashCodeMethodContent;
import org.jenerate.internal.strategy.method.content.impl.guava.GuavaToStringMethodContent;
import org.jenerate.internal.strategy.method.content.impl.java.JavaEqualsMethodContent;
import org.jenerate.internal.strategy.method.content.impl.java.JavaHashCodeMethodContent;
import org.jenerate.internal.strategy.method.impl.MethodImpl;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

/**
 * Default implementation of the {@link MethodContentManager}
 * 
 * @author maudrain
 */
public final class MethodContentManagerImpl implements MethodContentManager {

    private final LinkedHashSet<MethodContent<? extends MethodSkeleton<?>, ? extends MethodGenerationData>> methodContents = new LinkedHashSet<>();

    /**
     * Caches all the different method content strategies
     * 
     * @param preferencesManager the preference manager
     * @param javaInterfaceCodeAppender the java interface code appender
     */
    public MethodContentManagerImpl(PreferencesManager preferencesManager,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        methodContents.add(new CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager));
        methodContents.add(new CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager));

        methodContents.add(new CommonsLangHashCodeMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager));
        methodContents.add(new CommonsLangHashCodeMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager));

        methodContents.add(new CommonsLangToStringMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager));
        methodContents.add(new CommonsLangToStringMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager));

        methodContents.add(new CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager, javaInterfaceCodeAppender));
        methodContents.add(new CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager, javaInterfaceCodeAppender));

        methodContents.add(new GuavaToStringMethodContent(preferencesManager));
        methodContents.add(new GuavaEqualsMethodContent(preferencesManager));
        methodContents.add(new GuavaHashCodeMethodContent(preferencesManager));
        methodContents.add(new GuavaCompareToMethodContent(preferencesManager, javaInterfaceCodeAppender));

        methodContents.add(new JavaHashCodeMethodContent(preferencesManager));
        methodContents.add(new JavaEqualsMethodContent(preferencesManager));
    }

    /**
     * XXX TEST ME BETTER
     */
    @Override
    public <V extends MethodGenerationData> LinkedHashSet<StrategyIdentifier> getStrategiesIntersection(
            LinkedHashSet<MethodSkeleton<V>> methodSkeletons) {
        LinkedHashSet<StrategyIdentifier> strategyIdentifiersUnion = new LinkedHashSet<StrategyIdentifier>();
        for (MethodSkeleton<V> methodSkeleton : methodSkeletons) {
            strategyIdentifiersUnion.addAll(getAllStrategyIdentifiers(methodSkeleton));
        }
        LinkedHashSet<StrategyIdentifier> strategyIdentifiersIntersection = new LinkedHashSet<StrategyIdentifier>(
                strategyIdentifiersUnion);
        for (MethodSkeleton<V> methodSkeleton : methodSkeletons) {
            strategyIdentifiersIntersection.retainAll(getAllStrategyIdentifiers(methodSkeleton));
        }
        return strategyIdentifiersIntersection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MethodSkeleton<V>, V extends MethodGenerationData> LinkedHashSet<Method<T, V>> getAllMethods(
            LinkedHashSet<MethodSkeleton<V>> methodSkeletons, StrategyIdentifier selectedContentStrategy) {
        LinkedHashSet<Method<T, V>> methods = new LinkedHashSet<Method<T, V>>();
        for (MethodSkeleton<V> methodSkeleton : methodSkeletons) {
            MethodContent<T, V> methodContent = getMethodContent(methodSkeleton, selectedContentStrategy);
            methods.add(new MethodImpl<T, V>((T) methodSkeleton, methodContent));
        }
        return methods;
    }

    private <U extends MethodGenerationData> LinkedHashSet<StrategyIdentifier> getAllStrategyIdentifiers(
            MethodSkeleton<U> methodSkeleton) {
        LinkedHashSet<StrategyIdentifier> strategyIdentifiers = new LinkedHashSet<StrategyIdentifier>();
        for (MethodContent<? extends MethodSkeleton<?>, ? extends MethodGenerationData> methodContent : methodContents) {
            if (methodSkeleton.getClass().isAssignableFrom(methodContent.getRelatedMethodSkeletonClass())) {
                strategyIdentifiers.add(methodContent.getStrategyIdentifier());
            }
        }
        return strategyIdentifiers;
    }

    @SuppressWarnings("unchecked")
    private <T extends MethodSkeleton<U>, U extends MethodGenerationData> MethodContent<T, U> getMethodContent(
            MethodSkeleton<U> methodSkeleton, StrategyIdentifier strategyIdentifier) {
        for (MethodContent<? extends MethodSkeleton<?>, ? extends MethodGenerationData> methodContent : methodContents) {
            if (methodSkeleton.getClass().isAssignableFrom(methodContent.getRelatedMethodSkeletonClass())
                    && strategyIdentifier.equals(methodContent.getStrategyIdentifier())) {
                return (MethodContent<T, U>) methodContent;
            }
        }
        throw new IllegalStateException("Unable to retrieve the MethodContent for MethodSkeleton with class '"
                + methodSkeleton.getClass() + "' and StrategyIdentifier '" + strategyIdentifier + "'");
    }

}
