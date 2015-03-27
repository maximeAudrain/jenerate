package org.jenerate.internal.strategy.method.skeleton.impl;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;

public class ToStringMethodSkeleton extends AbstractMethodSkeleton<ToStringGenerationData> {

    public ToStringMethodSkeleton(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethod(IType objectClass, ToStringGenerationData data, String methodContent) throws JavaModelException {
        boolean addOverride = addOverride(objectClass);
        return MethodGenerations.createToStringMethod(data, addOverride, methodContent);
    }

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.TO_STRING;
    }

    @Override
    public String getMethodName() {
        return "toString";
    }

}
