package org.jenerate.internal.ui.dialogs.provider;

import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.JenerateDialog;

/**
 * Defines a provider of dialog to be used when generating the code. This dialog provides user inputs on the preferences
 * for the code generation
 * 
 * @author maudrain
 * @param <T> the type of {@link FieldDialog} this provider provides.
 */
public interface DialogProvider<T extends JenerateDialog<U>, U extends JenerateDialogData> {

    T getDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods) throws Exception;

    UserActionIdentifier getUserActionIdentifier();
}
