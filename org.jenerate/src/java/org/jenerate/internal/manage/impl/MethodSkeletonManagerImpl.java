package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.domain.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.domain.method.skeleton.impl.EqualsMethodSkeleton;
import org.jenerate.internal.domain.method.skeleton.impl.HashCodeMethodSkeleton;
import org.jenerate.internal.domain.method.skeleton.impl.ToStringMethodSkeleton;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.MethodSkeletonManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class MethodSkeletonManagerImpl implements MethodSkeletonManager {

    private final Set<MethodSkeleton<?>> methodSkeletons = new HashSet<MethodSkeleton<?>>();

    public MethodSkeletonManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        methodSkeletons.add(new EqualsMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new HashCodeMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new ToStringMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new CompareToMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate,
                javaInterfaceCodeAppender));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MethodGenerationData> Set<MethodSkeleton<T>> getMethodSkeletons(
            UserActionIdentifier userActionIdentifier) {
        Set<MethodSkeleton<T>> toReturn = new HashSet<MethodSkeleton<T>>();
        for (MethodSkeleton<? extends MethodGenerationData> methodSkeleton : methodSkeletons) {
            if (userActionIdentifier.equals(methodSkeleton.getUserActionIdentifier())) {
                toReturn.add((MethodSkeleton<T>) methodSkeleton);
            }
        }
        return toReturn;
    }

}
