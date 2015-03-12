package org.jenerate.internal.util;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;

/**
 * Helper class providing common methods for code generation
 * 
 * @author maudrain
 */
public final class JavaUiCodeAppender {

    /**
     * @see JavaUI#revealInEditor(IEditorPart, IJavaElement)
     */
    public void revealInEditor(IType objectClass, IJavaElement elementToReveal) throws JavaModelException,
            PartInitException {
        ICompilationUnit compilationUnit = objectClass.getCompilationUnit();
        IEditorPart javaEditor = JavaUI.openInEditor(compilationUnit);
        JavaUI.revealInEditor(javaEditor, elementToReveal);
    }

}
