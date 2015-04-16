package org.jenerate.internal.domain.data;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;

/**
 * Defines data used when generating method code. It holds common information for all method code generation. The
 * different values of those information are specified by the user in the dialog that opens when the method code is
 * being generated.
 * 
 * @author maudrain
 */
public interface MethodGenerationData {

    /**
     * @return all fields selected in the dialog for the code generation
     */
    IField[] getCheckedFields();

    /**
     * @return the selected {@link StrategyIdentifier} in the dialog
     */
    StrategyIdentifier getSelectedStrategyIdentifier();

    /**
     * @return the position where the code should be generated
     */
    IJavaElement getElementPosition();

    /**
     * @return {@code true} if the super should be taken in account for the code generation, {@code false} otherwise.
     */
    boolean appendSuper();

    /**
     * @return {@code true} if javadoc should be generated for the code generation, {@code false} otherwise.
     */
    boolean generateComment();

    /**
     * @return {@code true} if getters should be used for the code generation, {@code false} otherwise.
     */
    boolean useGettersInsteadOfFields();

    /**
     * @return {@code true} if block in 'if' statements should be used for the code generation, {@code false} otherwise.
     */
    boolean useBlockInIfStatements();
}
