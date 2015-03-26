package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.content.compareto.CommonsLangCompareToMethodContent;
import org.jenerate.internal.domain.method.content.equals.CommonsLangEqualsMethodContent;
import org.jenerate.internal.domain.method.content.hashcode.CommonsLangHashCodeMethodContent;
import org.jenerate.internal.domain.method.content.tostring.CommonsLangToStringMethodContent;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.MethodContentStrategyManager;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class MethodContentStrategyManagerImpl implements MethodContentStrategyManager {

    private final Set<MethodContent<? extends MethodSkeleton<?>, ? extends JenerateDialogData>> methodContents = new HashSet<>();

    public MethodContentStrategyManagerImpl(PreferencesManager preferencesManager,
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

    @Override
    public MethodContent<? extends MethodSkeleton<?>, ? extends JenerateDialogData> getStrategy(
            MethodSkeleton<? extends JenerateDialogData> methodSkeleton,
            MethodContentStrategyIdentifier methodContentStrategyIdentifier) {
        for (MethodContent<? extends MethodSkeleton<?>, ? extends JenerateDialogData> methodContent : methodContents) {
            if (methodSkeleton.getClass().isAssignableFrom(methodContent.getRelatedMethodSkeletonClass())
                    && methodContentStrategyIdentifier.equals(methodContent.getMethodContentStrategyIdentifier())) {
                return methodContent;
            }
        }
        throw new IllegalStateException();
    }

}
