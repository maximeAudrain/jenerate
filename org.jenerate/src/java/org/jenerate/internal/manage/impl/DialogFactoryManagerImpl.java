package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.manage.DialogFactoryManager;
import org.jenerate.internal.manage.DialogStrategyManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;
import org.jenerate.internal.ui.dialogs.factory.impl.CompareToDialogFactory;
import org.jenerate.internal.ui.dialogs.factory.impl.DialogFactoryHelperImpl;
import org.jenerate.internal.ui.dialogs.factory.impl.EqualsHashCodeDialogFactory;
import org.jenerate.internal.ui.dialogs.factory.impl.MethodFinder;
import org.jenerate.internal.ui.dialogs.factory.impl.ToStringDialogFactory;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

/**
 * Default implementation of the {@link DialogFactoryManager}.
 * 
 * @author maudrain
 */
public final class DialogFactoryManagerImpl implements DialogFactoryManager {

    private final DialogFactoryHelper dialogFactoryHelper = new DialogFactoryHelperImpl(new MethodFinder());

    private final DialogStrategyManager dialogStrategyManager;

    private final Set<DialogFactory<? extends MethodGenerationData>> dialogProviders = new HashSet<>();

    /**
     * Caches all the different dialog factories
     * 
     * @param preferencesManager the preference manager
     * @param javaInterfaceCodeAppender the java interface code appender
     */
    public DialogFactoryManagerImpl(PreferencesManager preferencesManager,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        dialogStrategyManager = new DialogStrategyManagerImpl(preferencesManager);
        dialogProviders
                .add(new EqualsHashCodeDialogFactory(dialogStrategyManager, dialogFactoryHelper, preferencesManager));
        dialogProviders.add(new ToStringDialogFactory(dialogStrategyManager, dialogFactoryHelper, preferencesManager));
        dialogProviders.add(new CompareToDialogFactory(dialogStrategyManager, dialogFactoryHelper, preferencesManager,
                javaInterfaceCodeAppender));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends MethodGenerationData> DialogFactory<U> getDialogFactory(CommandIdentifier commandIdentifier) {
        for (DialogFactory<? extends MethodGenerationData> dialogProvider : dialogProviders) {
            if (commandIdentifier.equals(dialogProvider.getCommandIdentifier())) {
                return (DialogFactory<U>) dialogProvider;
            }
        }
        throw new IllegalStateException(
                "Unable to retrieve a DialogFactory for the given user command '" + commandIdentifier + "'");
    }
}
