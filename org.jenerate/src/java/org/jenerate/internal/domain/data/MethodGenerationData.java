package org.jenerate.internal.domain.data;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;

/**
 * Defines data used when generating method code. It holds common information for all method code generation. The
 * different values of those information are determined by the user.
 * 
 * @author maudrain
 */
public interface MethodGenerationData {

    IField[] getCheckedFields();

    StrategyIdentifier getSelectedContentStrategy();

    IJavaElement getElementPosition();

    boolean getAppendSuper();

    boolean getGenerateComment();

    boolean getUseGettersInsteadOfFields();

    boolean getUseBlockInIfStatements();
}
