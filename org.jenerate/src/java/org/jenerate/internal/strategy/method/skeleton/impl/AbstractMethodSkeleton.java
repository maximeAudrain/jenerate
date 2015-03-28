package org.jenerate.internal.strategy.method.skeleton.impl;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;

public abstract class AbstractMethodSkeleton<T extends MethodGenerationData> implements MethodSkeleton<T> {

    protected final PreferencesManager preferencesManager;
    protected final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public AbstractMethodSkeleton(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        this.preferencesManager = preferencesManager;
        this.generatorsCommonMethodsDelegate = generatorsCommonMethodsDelegate;
    }

    protected boolean addOverride(IType objectClass) {
        boolean addOverridePreference = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION)).booleanValue();
        return addOverridePreference && generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass);
    }

    /**
     * XXX see if libraries for skeletons are needed.
     */
    @Override
    public final LinkedHashSet<String> getLibrariesToImport() {
        return new LinkedHashSet<String>();
    }

}
