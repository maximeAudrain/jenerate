package org.jenerate.internal.domain.method.impl;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.ToStringDialogData;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class ToStringMethod extends AbstractMethod<ToStringDialogData> {

    public ToStringMethod(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethod(IType objectClass, ToStringDialogData data, String methodContent) throws JavaModelException {
        boolean addOverride = addOverride(objectClass);
        return MethodGenerations.createToStringMethod(data, addOverride, methodContent);
    }

}
