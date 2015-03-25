package org.jenerate.internal.util;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.text.edits.MalformedTreeException;

/**
 * Defines utility methods modifying the object class to to append java interface code
 * 
 * @author maudrain
 */
public interface JavaInterfaceCodeAppender {

    boolean isImplementedOrExtendedInSupertype(final IType objectClass, final String interfaceName)
            throws JavaModelException;

    boolean isImplementedInSupertype(final IType objectClass, final String interfaceName) throws JavaModelException;

    void addSuperInterface(final IType objectClass, final String interfaceName) throws JavaModelException,
            InvalidInputException, MalformedTreeException;

}
