package org.jenerate.internal.domain.method.impl;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.method.Method;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public abstract class AbstractMethod<T extends JenerateDialogData> implements Method<T> {

    protected final PreferencesManager preferencesManager;
    protected final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public AbstractMethod(PreferencesManager preferencesManager,
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
