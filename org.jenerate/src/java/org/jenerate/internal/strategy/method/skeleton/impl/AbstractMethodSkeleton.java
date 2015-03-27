package org.jenerate.internal.strategy.method.skeleton.impl;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

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
     * XXX see if this is needed
     */
    @Override
    public final Set<String> getLibrariesToImport() {
        return Collections.emptySet();
    }

}
