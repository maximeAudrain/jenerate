package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.method.Method;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.impl.MethodImpl;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.MethodContentStrategyManager;
import org.jenerate.internal.manage.MethodSkeletonStrategyManager;
import org.jenerate.internal.manage.MethodStrategyManager;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class MethodStrategyManagerImpl implements MethodStrategyManager {

    private final PreferencesManager preferencesManager;
    private final MethodSkeletonStrategyManager methodSkeletonStrategyManager;
    private final MethodContentStrategyManager methodContentStrategyManager;

    public MethodStrategyManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        this.preferencesManager = preferencesManager;
        this.methodSkeletonStrategyManager = new MethodSkeletonStrategyManagerImpl(preferencesManager,
                generatorsCommonMethodsDelegate, javaInterfaceCodeAppender);
        this.methodContentStrategyManager = new MethodContentStrategyManagerImpl(preferencesManager,
                generatorsCommonMethodsDelegate, javaInterfaceCodeAppender);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MethodSkeleton<U>, U extends JenerateDialogData> Set<Method<T, U>> getMethods(
            UserActionIdentifier userActionIdentifier) {
        boolean useCommonLang3 = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
        MethodContentStrategyIdentifier methodContentStrategyIdentifier = MethodContentStrategyIdentifier.USE_COMMONS_LANG;
        if (useCommonLang3) {
            methodContentStrategyIdentifier = MethodContentStrategyIdentifier.USE_COMMONS_LANG3;
        }

        Set<MethodSkeleton<U>> methodSkeletons = methodSkeletonStrategyManager.getMethodSkeletons(userActionIdentifier);

        Set<Method<T, U>> methods = new HashSet<Method<T, U>>();
        for (MethodSkeleton<U> methodSkeleton : methodSkeletons) {
            MethodContent<T, U> methodContent = methodContentStrategyManager.getStrategy(methodSkeleton,
                    methodContentStrategyIdentifier);
            methods.add(new MethodImpl<T, U>((T) methodSkeleton, methodContent));
        }
        return methods;
    }

}
