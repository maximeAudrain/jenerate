package org.jenerate.internal.manage.impl;

import java.util.LinkedHashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.MethodSkeletonManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.ToStringMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class MethodSkeletonManagerImpl implements MethodSkeletonManager {

    private final SortedSet<MethodSkeleton<?>> methodSkeletons = new TreeSet<MethodSkeleton<?>>();

    public MethodSkeletonManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        methodSkeletons.add(new HashCodeMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new EqualsMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new ToStringMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate));
        methodSkeletons.add(new CompareToMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate,
                javaInterfaceCodeAppender));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MethodGenerationData> LinkedHashSet<MethodSkeleton<T>> getMethodSkeletons(
            UserActionIdentifier userActionIdentifier) {
        LinkedHashSet<MethodSkeleton<T>> toReturn = new LinkedHashSet<MethodSkeleton<T>>();
        for (MethodSkeleton<? extends MethodGenerationData> methodSkeleton : methodSkeletons) {
            if (userActionIdentifier.equals(methodSkeleton.getUserActionIdentifier())) {
                toReturn.add((MethodSkeleton<T>) methodSkeleton);
            }
        }
        return toReturn;
    }

}
