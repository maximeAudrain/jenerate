package org.jenerate.internal.manage;

import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;

/**
 * Manager responsible for retrieving a {@link DialogFactory} for a given {@link UserActionIdentifier}
 * 
 * @author maudrain
 */
public interface DialogFactoryManager {

    /**
     * Get the {@link DialogFactory} for the given parameter
     * 
     * @param userActionIdentifier the unique identifier of a user action
     * @return the {@link DialogFactory} for the provided parameter
     * @throws IllegalStateException if no {@link DialogFactory} could be found for a provided
     *             {@link UserActionIdentifier}
     */
    <T extends FieldDialog<U>, U extends MethodGenerationData> DialogFactory<T, U> getDialogFactory(
            UserActionIdentifier userActionIdentifier);

}
