package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.manage.DialogFactoryManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.impl.CompareToDialogFactory;
import org.jenerate.internal.ui.dialogs.factory.impl.EqualsHashCodeDialogFactory;
import org.jenerate.internal.ui.dialogs.factory.impl.ToStringDialogFactory;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class DialogFactoryManagerImpl implements DialogFactoryManager {

    private final Set<DialogFactory<? extends FieldDialog<?>, ? extends MethodGenerationData>> dialogProviders = new HashSet<>();

    public DialogFactoryManagerImpl(PreferencesManager preferencesManager,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        dialogProviders.add(new EqualsHashCodeDialogFactory(preferencesManager));
        dialogProviders.add(new ToStringDialogFactory(preferencesManager));
        dialogProviders.add(new CompareToDialogFactory(preferencesManager, javaInterfaceCodeAppender));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FieldDialog<U>, U extends MethodGenerationData> DialogFactory<T, U> getDialogFactory(
            CommandIdentifier commandIdentifier) {
        for (DialogFactory<? extends FieldDialog<?>, ? extends MethodGenerationData> dialogProvider : dialogProviders) {
            if (commandIdentifier.equals(dialogProvider.getUserActionIdentifier())) {
                return (DialogFactory<T, U>) dialogProvider;
            }
        }
        throw new IllegalStateException("Unable to retrieve a DialogFactory for the given user action '"
                + commandIdentifier + "'");
    }
}
