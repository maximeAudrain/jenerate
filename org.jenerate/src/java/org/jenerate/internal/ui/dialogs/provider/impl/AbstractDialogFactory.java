package org.jenerate.internal.ui.dialogs.provider.impl;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.dialogs.JenerateDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogFactory;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public abstract class AbstractDialogFactory<T extends JenerateDialog<U>, U extends JenerateDialogData> implements
        DialogFactory<T, U> {

    protected final PreferencesManager preferencesManager;
    protected final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public AbstractDialogFactory(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        this.preferencesManager = preferencesManager;
        this.generatorsCommonMethodsDelegate = generatorsCommonMethodsDelegate;
    }

}
