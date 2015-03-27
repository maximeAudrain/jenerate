package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.DialogFactoryManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.impl.CompareToDialogFactory;
import org.jenerate.internal.ui.dialogs.factory.impl.EqualsHashCodeDialogFactory;
import org.jenerate.internal.ui.dialogs.factory.impl.ToStringDialogFactory;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class DialogFactoryManagerImpl implements DialogFactoryManager {

    private final Set<DialogFactory<? extends FieldDialog<?>, ? extends JenerateDialogData>> dialogProviders = new HashSet<>();

    public DialogFactoryManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        dialogProviders.add(new EqualsHashCodeDialogFactory(preferencesManager, generatorsCommonMethodsDelegate));
        dialogProviders.add(new ToStringDialogFactory(preferencesManager, generatorsCommonMethodsDelegate));
        dialogProviders.add(new CompareToDialogFactory(preferencesManager, generatorsCommonMethodsDelegate,
                javaInterfaceCodeAppender));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FieldDialog<U>, U extends JenerateDialogData> DialogFactory<T, U> getDialogFactory(
            UserActionIdentifier userActionIdentifier) {
        for (DialogFactory<? extends FieldDialog<?>, ? extends JenerateDialogData> dialogProvider : dialogProviders) {
            if (userActionIdentifier.equals(dialogProvider.getUserActionIdentifier())) {
                return (DialogFactory<T, U>) dialogProvider;
            }
        }
        throw new IllegalStateException("Unable to retrieve a DialogFactory for the given user action '"
                + userActionIdentifier + "'");
    }
}
