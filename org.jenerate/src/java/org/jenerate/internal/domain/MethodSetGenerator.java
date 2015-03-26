package org.jenerate.internal.domain;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.ui.dialogs.JenerateDialog;

/**
 * Defines a {@link MethodSetGenerator}. This can generate a {@link MethodSet} using the {@link JenerateDialogData}
 * provided by a specific {@link JenerateDialog}.
 * 
 * @author maudrain
 * @param <T>
 * @param <U>
 */
public interface MethodSetGenerator<T extends JenerateDialog<U>, U extends JenerateDialogData> {

    T getJenerateDialog();

    MethodSet getMethodSet();

}
