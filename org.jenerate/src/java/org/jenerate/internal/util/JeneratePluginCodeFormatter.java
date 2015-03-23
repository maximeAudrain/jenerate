package org.jenerate.internal.util;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;

public interface JeneratePluginCodeFormatter {

    String formatCode(IType objectClass, String source) throws JavaModelException, BadLocationException;

}
