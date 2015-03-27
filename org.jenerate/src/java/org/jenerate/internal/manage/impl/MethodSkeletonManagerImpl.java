package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.domain.method.skeleton.impl.CompareToMethod;
import org.jenerate.internal.domain.method.skeleton.impl.EqualsMethod;
import org.jenerate.internal.domain.method.skeleton.impl.HashCodeMethod;
import org.jenerate.internal.domain.method.skeleton.impl.ToStringMethod;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.MethodSkeletonManager;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class MethodSkeletonManagerImpl implements MethodSkeletonManager {

    private final Set<MethodSkeleton<?>> methodSkeletons = new HashSet<MethodSkeleton<?>>();

    public MethodSkeletonManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        methodSkeletons.add(new EqualsMethod(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new HashCodeMethod(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new ToStringMethod(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new CompareToMethod(preferencesManager, generatorsCommonMethodsDelegate,
                javaInterfaceCodeAppender));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends JenerateDialogData> Set<MethodSkeleton<T>> getMethodSkeletons(
            UserActionIdentifier userActionIdentifier) {
        Set<MethodSkeleton<T>> toReturn = new HashSet<MethodSkeleton<T>>();
        for (MethodSkeleton<? extends JenerateDialogData> methodSkeleton : methodSkeletons) {
            if (userActionIdentifier.equals(methodSkeleton.getUserActionIdentifier())) {
                toReturn.add((MethodSkeleton<T>) methodSkeleton);
            }
        }
        return toReturn;
    }

}
