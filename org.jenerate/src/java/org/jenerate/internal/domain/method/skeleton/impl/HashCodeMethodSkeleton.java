package org.jenerate.internal.domain.method.skeleton.impl;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;

public class HashCodeMethodSkeleton extends AbstractMethodSkeleton<EqualsHashCodeGenerationData> {

    public HashCodeMethodSkeleton(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethod(IType objectClass, EqualsHashCodeGenerationData data, String methodContent)
            throws JavaModelException {
        boolean addOverride = addOverride(objectClass);
        return MethodGenerations.createHashCodeMethod(data, addOverride, methodContent);
    }

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.EQUALS_HASH_CODE;
    }

    @Override
    public String getMethodName() {
        return "hashCode";
    }

}
