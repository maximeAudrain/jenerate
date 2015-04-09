package org.jenerate.internal.generate.method;

import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.ui.dialogs.FieldDialog;

/**
 * A method generator that can generate any number of methods in the current opened file. It determines which methods to
 * generate from the user provided {@link CommandIdentifier}.
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodSkeleton} for this method generator
 * @param <U> the type of {@link FieldDialog} for this method generator
 * @param <V> the type of {@link MethodGenerationData} this generator handles
 */
public interface MethodGenerator<T extends MethodSkeleton<V>, U extends FieldDialog<V>, V extends MethodGenerationData> {

    /**
     * Generate the methods for in the provided objectClass for the provided {@link CommandIdentifier}
     * 
     * @param parentShell the parent shell
     * @param objectClass the current java class where to generate the methods
     * @param commandIdentifier the user provided {@link CommandIdentifier} to determine which methods to generate
     */
    void generate(Shell parentShell, IType objectClass, CommandIdentifier commandIdentifier);
}
