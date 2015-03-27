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

    @Override
    public Set<Method<? extends MethodSkeleton<?>, ? extends JenerateDialogData>> getMethods(
            UserActionIdentifier userActionIdentifier) {
        boolean useCommonLang3 = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
        MethodContentStrategyIdentifier methodContentStrategyIdentifier = MethodContentStrategyIdentifier.USE_COMMONS_LANG;
        if (useCommonLang3) {
            methodContentStrategyIdentifier = MethodContentStrategyIdentifier.USE_COMMONS_LANG3;
        }

        Set<MethodSkeleton<? extends JenerateDialogData>> methodSkeletons = methodSkeletonStrategyManager
                .getMethodSkeletons(userActionIdentifier);

        Set<Method<? extends MethodSkeleton<?>, ? extends JenerateDialogData>> methods = new HashSet<Method<? extends MethodSkeleton<?>, ? extends JenerateDialogData>>();
        for (MethodSkeleton<? extends JenerateDialogData> methodSkeleton : methodSkeletons) {
            MethodContent<? extends MethodSkeleton<?>, ? extends JenerateDialogData> methodContent = methodContentStrategyManager
                    .getStrategy(methodSkeleton, methodContentStrategyIdentifier);
            methods.add(new MethodImpl(methodSkeleton, methodContent));
        }
        return methods;
    }

}
