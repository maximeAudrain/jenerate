package org.jenerate.internal.domain.data;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;

/**
 * Defines data used when generating method code. It holds common information for all method code generation. The
 * different values of those information are determined by the user.
 * 
 * @author maudrain
 */
public interface MethodGenerationData {

    IJavaElement getElementPosition();

    IField[] getCheckedFields();

    boolean getAppendSuper();

    boolean getGenerateComment();

    boolean getUseGettersInsteadOfFields();

    boolean getUseBlockInIfStatements();
}
