package org.jenerate.internal.manage;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.ui.dialogs.JenerateDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;

public interface DialogProviderManager {

    <T extends JenerateDialog<U>, U extends JenerateDialogData> DialogProvider<T, U> getDialogProvider(
            UserActionIdentifier userActionIdentifier);

}
