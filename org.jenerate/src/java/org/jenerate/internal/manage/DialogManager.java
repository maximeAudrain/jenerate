package org.jenerate.internal.manage;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.impl.UserActionIdentifier;
import org.jenerate.internal.ui.dialogs.JenerateDialog;

public interface DialogManager<T extends JenerateDialog<U>, U extends JenerateDialogData> {

    T getJenerateDialog(UserActionIdentifier userActionIdentifier);

}
