package org.jenerate.internal.ui.dialogs.provider;

import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.ui.dialogs.JenerateDialog;

/**
 * Defines a {@link DialogFactory} that allows to create {@link JenerateDialog} given certain parameters. The
 * {@link DialogFactory} has a 1 to 1 relationship with an {@link UserActionIdentifier}.
 * 
 * @author maudrain
 * @param <T> the type of {@link JenerateDialog} this provider provides.
 * @param <U> the type of {@link JenerateDialogData} provided by this {@link JenerateDialog}
 */
public interface DialogFactory<T extends JenerateDialog<U>, U extends JenerateDialogData> {

    /**
     * @param parentShell the shell where the dialog is opened from.
     * @param objectClass the current class on which the code generation process is in effect
     * @param excludedMethods a set of excluded methods XXX see why this guy needs this
     * @return the fully formed {@link JenerateDialog} for the provided parameters
     * @throws Exception if a problem occur when creating the {@link JenerateDialog}
     */
    T createDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods) throws Exception;

    /**
     * @return the related {@link UserActionIdentifier} for this {@link DialogFactory}
     */
    UserActionIdentifier getUserActionIdentifier();
}
