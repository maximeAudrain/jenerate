package org.jenerate.internal.generate.method.util;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;

public interface JavaCodeFormatter {

    String formatCode(IType objectClass, String source) throws JavaModelException, BadLocationException;

}
