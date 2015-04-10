package org.jenerate.internal.ui.dialogs.factory;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;

/**
 * Defines a {@link DialogFactory} that allows to create {@link FieldDialog} given certain parameters. The
 * {@link DialogFactory} has a 1 to 1 relationship with a {@link CommandIdentifier}.
 * 
 * @author maudrain
 * @param <T> the type of {@link FieldDialog} this factory provides.
 * @param <U> the type of {@link MethodGenerationData} provided by this {@link FieldDialog}
 */
public interface DialogFactory<T extends FieldDialog<U>, U extends MethodGenerationData> {

    /**
     * Creates a {@link Dialog} from the provided parameters
     * 
     * @param parentShell the shell where the dialog is opened from.
     * @param objectClass the current class on which the code generation process is in effect
     * @param excludedMethods a set of excluded methods XXX see why this guy needs this
     * @return the fully formed {@link FieldDialog} for the provided parameters
     * @throws Exception if a problem occur when creating the {@link FieldDialog}
     */
    T createDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods,
            LinkedHashSet<StrategyIdentifier> possibleStrategies) throws Exception;

    /**
     * @return the related {@link CommandIdentifier} for this {@link DialogFactory}
     */
    CommandIdentifier getCommandIdentifier();
}
