package org.jenerate.internal.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;

/**
 * Defines a dialog of the Jenerate plugin. This dialog provides data filled up by the user to tune up the code
 * generation.
 * 
 * @author maudrain
 * @param <T> the type of user data provided by this dialog
 */
public interface JenerateDialog<T extends JenerateDialogData> {

    T getData();

    Dialog getDialog();
}
