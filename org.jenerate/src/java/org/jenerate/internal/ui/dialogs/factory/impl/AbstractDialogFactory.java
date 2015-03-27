package org.jenerate.internal.ui.dialogs.factory.impl;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;

public abstract class AbstractDialogFactory<T extends FieldDialog<U>, U extends MethodGenerationData> implements
        DialogFactory<T, U> {

    protected final PreferencesManager preferencesManager;
    protected final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public AbstractDialogFactory(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        this.preferencesManager = preferencesManager;
        this.generatorsCommonMethodsDelegate = generatorsCommonMethodsDelegate;
    }

}
