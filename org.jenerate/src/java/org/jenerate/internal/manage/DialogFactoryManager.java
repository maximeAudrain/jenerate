package org.jenerate.internal.manage;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;

/**
 * Manager responsible for retrieving a {@link DialogFactory} for a given {@link CommandIdentifier}
 * 
 * @author maudrain
 */
public interface DialogFactoryManager {

    /**
     * Get the {@link DialogFactory} for the given parameter
     * 
     * @param commandIdentifier the unique identifier of a command
     * @return the {@link DialogFactory} for the provided parameter
     * @throws IllegalStateException if no {@link DialogFactory} could be found for a provided {@link CommandIdentifier}
     */
    <U extends MethodGenerationData> DialogFactory<U> getDialogFactory(CommandIdentifier commandIdentifier);

}
