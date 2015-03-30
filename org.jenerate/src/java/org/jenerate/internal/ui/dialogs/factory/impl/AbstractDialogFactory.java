package org.jenerate.internal.ui.dialogs.factory.impl;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;

public abstract class AbstractDialogFactory<T extends FieldDialog<U>, U extends MethodGenerationData> implements
        DialogFactory<T, U> {

    protected final DialogFactoryHelper dialogFactoryHelper;
    protected final PreferencesManager preferencesManager;

    public AbstractDialogFactory(DialogFactoryHelper dialogFactoryHelper, PreferencesManager preferencesManager) {
        this.dialogFactoryHelper = dialogFactoryHelper;
        this.preferencesManager = preferencesManager;
    }

    protected IField[] getObjectClassFields(IType objectClass) throws JavaModelException {
        return dialogFactoryHelper.getObjectClassFields(objectClass, preferencesManager);
    }

}
