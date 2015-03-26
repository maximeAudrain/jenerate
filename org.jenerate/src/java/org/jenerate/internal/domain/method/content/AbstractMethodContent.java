package org.jenerate.internal.domain.method.content;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public abstract class AbstractMethodContent<T extends JenerateDialogData> implements MethodContent<T> {

    protected final PreferencesManager preferencesManager;
    protected final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;
    
    public AbstractMethodContent(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        this.preferencesManager = preferencesManager;
        this.generatorsCommonMethodsDelegate = generatorsCommonMethodsDelegate;
    }
    
    
}
