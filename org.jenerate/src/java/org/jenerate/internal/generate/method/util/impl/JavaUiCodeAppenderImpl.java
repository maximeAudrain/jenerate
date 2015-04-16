package org.jenerate.internal.generate.method.util.impl;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;

/**
 * Implementation of the {@link JavaUiCodeAppender}
 * 
 * @author maudrain
 */
public final class JavaUiCodeAppenderImpl implements JavaUiCodeAppender {

    @Override
    public void revealInEditor(IType objectClass, IJavaElement elementToReveal) throws JavaModelException,
            PartInitException {
        ICompilationUnit compilationUnit = objectClass.getCompilationUnit();
        IEditorPart javaEditor = JavaUI.openInEditor(compilationUnit);
        JavaUI.revealInEditor(javaEditor, elementToReveal);
    }

}
