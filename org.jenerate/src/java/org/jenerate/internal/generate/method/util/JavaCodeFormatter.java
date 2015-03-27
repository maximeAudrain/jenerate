package org.jenerate.internal.generate.method.util;

import org.eclipse.jdt.core.IType;

public interface JavaCodeFormatter {

    String formatCode(IType objectClass, String source) throws Exception;

}
