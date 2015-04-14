package org.jenerate.internal.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.jenerate.internal.domain.data.MethodGenerationData;

/**
 * Defines a dialog of the Jenerate plugin. It is called {@link FieldDialog} because it knows about the current fields
 * of the class where the code generation is ongoing. This dialog holds data filled up by the user that is then used to
 * act on the code generation.
 * 
 * @author maudrain
 * @param <T> the type of user data provided by this dialog
 */
public interface FieldDialog<U extends MethodGenerationData> {

    /**
     * @return the data which will be used for the code generation
     */
    U getData();

    /**
     * @return the concrete {@link Dialog} instance for this {@link FieldDialog}
     */
    Dialog getDialog();

    void showErrorMessage(String message);

    void clearErrorMessage();

    Composite getEditableComposite();
}
