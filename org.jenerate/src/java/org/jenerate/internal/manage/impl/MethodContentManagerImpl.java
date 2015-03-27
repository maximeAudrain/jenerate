package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.MethodContentManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangCompareToMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangEqualsMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangHashCodeMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringMethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class MethodContentManagerImpl implements MethodContentManager {

    private final Set<MethodContent<? extends MethodSkeleton<?>, ? extends MethodGenerationData>> methodContents = new HashSet<>();

    public MethodContentManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        methodContents.add(new CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager, generatorsCommonMethodsDelegate));
        methodContents.add(new CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager, generatorsCommonMethodsDelegate));

        methodContents.add(new CommonsLangHashCodeMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager, generatorsCommonMethodsDelegate));
        methodContents.add(new CommonsLangHashCodeMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager, generatorsCommonMethodsDelegate));

        methodContents.add(new CommonsLangToStringMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager, generatorsCommonMethodsDelegate));
        methodContents.add(new CommonsLangToStringMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager, generatorsCommonMethodsDelegate));

        methodContents.add(new CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager, generatorsCommonMethodsDelegate, javaInterfaceCodeAppender));
        methodContents.add(new CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager, generatorsCommonMethodsDelegate, javaInterfaceCodeAppender));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MethodSkeleton<U>, U extends MethodGenerationData> MethodContent<T, U> getMethodContent(
            MethodSkeleton<U> methodSkeleton, MethodContentStrategyIdentifier methodContentStrategyIdentifier) {
        for (MethodContent<? extends MethodSkeleton<?>, ? extends MethodGenerationData> methodContent : methodContents) {
            if (methodSkeleton.getClass().isAssignableFrom(methodContent.getRelatedMethodSkeletonClass())
                    && methodContentStrategyIdentifier.equals(methodContent.getMethodContentStrategyIdentifier())) {
                return (MethodContent<T, U>) methodContent;
            }
        }
        throw new IllegalStateException("Unable to retrieve the MethodContent for MethodSkeleton with class '"
                + methodSkeleton.getClass() + "' and MethodContentStrategyIdentifier '"
                + methodContentStrategyIdentifier + "'");
    }

}
