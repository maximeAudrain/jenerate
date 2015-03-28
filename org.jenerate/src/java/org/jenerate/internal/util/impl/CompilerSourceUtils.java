package org.jenerate.internal.util.impl;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

/**
 * Utility class for Java Compiler information
 * 
 * @author maudrain
 */
public final class CompilerSourceUtils {

    private CompilerSourceUtils() {
        /* Only static helper methods */
    }

    /**
     * Determines if the source code of a certain {@link IType} is compiled for Java5 or above
     * 
     * @param objectClass the class for which to check the source compiler version
     * @return {@code true} if the compiler is equals to Java5 or above, {@code false} otherwise
     */
    public static boolean isSourceLevelGreaterThanOrEqualTo5(IType objectClass) {
        IJavaProject project = objectClass.getJavaProject();
        float source = Float.parseFloat(project.getOption(JavaCore.COMPILER_SOURCE, true));
        return source >= 1.5;
    }

}
