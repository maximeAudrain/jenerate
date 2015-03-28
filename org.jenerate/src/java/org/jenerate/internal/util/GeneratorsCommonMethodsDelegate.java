package org.jenerate.internal.util;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * XXX get rid of me once the code is tested and the strategy pattern in effect
 * 
 * @author maudrain
 */
public interface GeneratorsCommonMethodsDelegate {

    boolean areAllFinalFields(final IField[] fields) throws JavaModelException;

    boolean isSourceLevelGreaterThanOrEqualTo5(IType objectClass);
}
