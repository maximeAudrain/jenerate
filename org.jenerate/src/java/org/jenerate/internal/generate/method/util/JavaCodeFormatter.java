package org.jenerate.internal.generate.method.util;

import org.eclipse.jdt.core.IType;

/**
 * A code formatter that formats a certain source code depending on the current formatting preferences
 * 
 * @author jiayun, maudrain
 */
public interface JavaCodeFormatter {

    String formatCode(IType objectClass, String source) throws Exception;

}
