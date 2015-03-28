package org.jenerate.internal.manage.impl;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.manage.MethodSkeletonManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.ToStringMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class MethodSkeletonManagerImpl implements MethodSkeletonManager {

    private final LinkedHashSet<MethodSkeleton<?>> methodSkeletons = new LinkedHashSet<MethodSkeleton<?>>();

    public MethodSkeletonManagerImpl(PreferencesManager preferencesManager,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        methodSkeletons.add(new HashCodeMethodSkeleton(preferencesManager));
        methodSkeletons.add(new EqualsMethodSkeleton(preferencesManager));
        methodSkeletons.add(new ToStringMethodSkeleton(preferencesManager));
        methodSkeletons.add(new CompareToMethodSkeleton(preferencesManager, javaInterfaceCodeAppender));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MethodGenerationData> LinkedHashSet<MethodSkeleton<T>> getMethodSkeletons(
            CommandIdentifier commandIdentifier) {
        LinkedHashSet<MethodSkeleton<T>> toReturn = new LinkedHashSet<MethodSkeleton<T>>();
        for (MethodSkeleton<? extends MethodGenerationData> methodSkeleton : methodSkeletons) {
            if (commandIdentifier.equals(methodSkeleton.getUserActionIdentifier())) {
                toReturn.add((MethodSkeleton<T>) methodSkeleton);
            }
        }
        return toReturn;
    }

}
