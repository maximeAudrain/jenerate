package org.jenerate.internal.manage;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.ui.dialogs.JenerateDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogFactory;

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
    <T extends JenerateDialog<U>, U extends JenerateDialogData> DialogFactory<T, U> getDialogFactory(
            UserActionIdentifier userActionIdentifier);

}
