package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.DialogProviderManager;
import org.jenerate.internal.ui.dialogs.JenerateDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.dialogs.provider.impl.CompareToDialogProvider;
import org.jenerate.internal.ui.dialogs.provider.impl.EqualsHashCodeDialogProvider;
import org.jenerate.internal.ui.dialogs.provider.impl.ToStringDialogProvider;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class DialogProviderManagerImpl implements DialogProviderManager {

    private final Set<DialogProvider<? extends JenerateDialog<?>, ? extends JenerateDialogData>> dialogProviders = new HashSet<>();

    public DialogProviderManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        dialogProviders.add(new EqualsHashCodeDialogProvider(preferencesManager, generatorsCommonMethodsDelegate));
        dialogProviders.add(new ToStringDialogProvider(preferencesManager, generatorsCommonMethodsDelegate));
        dialogProviders.add(new CompareToDialogProvider(preferencesManager, generatorsCommonMethodsDelegate,
                javaInterfaceCodeAppender));
    }

    @Override
    public DialogProvider<? extends JenerateDialog<?>, ? extends JenerateDialogData> getDialogProvider(
            UserActionIdentifier userActionIdentifier) {
        for (DialogProvider<? extends JenerateDialog<?>, ? extends JenerateDialogData> dialogProvider : dialogProviders) {
            if (userActionIdentifier.equals(dialogProvider.getUserActionIdentifier())) {
                return dialogProvider;
            }
        }
        throw new IllegalStateException();
    }
}
