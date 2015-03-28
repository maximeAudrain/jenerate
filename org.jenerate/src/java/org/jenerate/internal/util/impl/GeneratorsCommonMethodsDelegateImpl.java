package org.jenerate.internal.util.impl;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;

/**
 * XXX get rid of me once the code is tested and the strategy pattern in effect. Also see if code redundancy can be
 * removed.
 * 
 * @author maudrain
 */
public class GeneratorsCommonMethodsDelegateImpl implements GeneratorsCommonMethodsDelegate {

    /**
     * XXX Is only used for field caching, should be moved close to the related content strategies
     */
    @Override
    public boolean areAllFinalFields(IField[] fields) throws JavaModelException {
        for (int i = 0; i < fields.length; i++) {
            if (!Flags.isFinal(fields[i].getFlags())) {
                return false;
            }
        }

        return true;
    }

    /**
     * XXX used by content and skeleton strategies
     */
    @Override
    public boolean isSourceLevelGreaterThanOrEqualTo5(IType objectClass) {
        IJavaProject project = objectClass.getJavaProject();
        float sc = Float.parseFloat(project.getOption(JavaCore.COMPILER_SOURCE, true));
        return sc >= 1.5;
    }
}
