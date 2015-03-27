package org.jenerate.internal.ui.dialogs.factory;

import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.ui.dialogs.FieldDialog;

/**
 * Defines a {@link DialogFactory} that allows to create {@link FieldDialog} given certain parameters. The
 * {@link DialogFactory} has a 1 to 1 relationship with an {@link UserActionIdentifier}.
 * 
 * @author maudrain
 * @param <T> the type of {@link FieldDialog} this provider provides.
 * @param <U> the type of {@link MethodGenerationData} provided by this {@link FieldDialog}
 */
public interface DialogFactory<T extends FieldDialog<U>, U extends MethodGenerationData> {

    /**
     * @param parentShell the shell where the dialog is opened from.
     * @param objectClass the current class on which the code generation process is in effect
     * @param excludedMethods a set of excluded methods XXX see why this guy needs this
     * @return the fully formed {@link FieldDialog} for the provided parameters
     * @throws Exception if a problem occur when creating the {@link FieldDialog}
     */
    T createDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods) throws Exception;

    /**
     * @return the related {@link UserActionIdentifier} for this {@link DialogFactory}
     */
    UserActionIdentifier getUserActionIdentifier();
}
