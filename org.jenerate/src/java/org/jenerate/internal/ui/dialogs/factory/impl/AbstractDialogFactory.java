package org.jenerate.internal.ui.dialogs.factory.impl;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;

/**
 * Abstract {@link DialogFactory} containing common methods and fields for the specific {@link DialogFactory}
 * implementations
 * 
 * @author maudrain
 * @param <T> the type of {@link FieldDialog} this factory provides.
 * @param <U> the type of {@link MethodGenerationData} provided by this {@link FieldDialog}
 */
public abstract class AbstractDialogFactory<T extends FieldDialog<U>, U extends MethodGenerationData> implements
        DialogFactory<T, U> {

    protected final DialogFactoryHelper dialogFactoryHelper;
    protected final PreferencesManager preferencesManager;

    /**
     * Constructor
     * 
     * @param dialogFactoryHelper the dialog factory helper
     * @param preferencesManager the preference manager
     */
    public AbstractDialogFactory(DialogFactoryHelper dialogFactoryHelper, PreferencesManager preferencesManager) {
        this.dialogFactoryHelper = dialogFactoryHelper;
        this.preferencesManager = preferencesManager;
    }

    protected IField[] getObjectClassFields(IType objectClass) throws JavaModelException {
        return dialogFactoryHelper.getObjectClassFields(objectClass, preferencesManager);
    }

}
