package org.jenerate.internal.strategy.method.skeleton.impl;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.util.impl.CompilerSourceUtils;

public abstract class AbstractMethodSkeleton<T extends MethodGenerationData> implements MethodSkeleton<T> {

    protected final PreferencesManager preferencesManager;

    public AbstractMethodSkeleton(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    protected boolean addOverride(IType objectClass) {
        boolean addOverridePreference = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION)).booleanValue();
        return addOverridePreference && CompilerSourceUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass);
    }

    /**
     * XXX see if libraries for skeletons are needed.
     */
    @Override
    public final LinkedHashSet<String> getLibrariesToImport() {
        return new LinkedHashSet<String>();
    }

}
