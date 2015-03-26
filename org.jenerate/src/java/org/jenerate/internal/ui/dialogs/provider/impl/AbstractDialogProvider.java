package org.jenerate.internal.ui.dialogs.provider.impl;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.dialogs.JenerateDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public abstract class AbstractDialogProvider<T extends JenerateDialog<U>, U extends JenerateDialogData> implements
        DialogProvider<T, U> {

    protected final PreferencesManager preferencesManager;
    protected final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public AbstractDialogProvider(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        this.preferencesManager = preferencesManager;
        this.generatorsCommonMethodsDelegate = generatorsCommonMethodsDelegate;
    }

}
