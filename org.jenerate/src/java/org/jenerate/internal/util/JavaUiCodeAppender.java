package org.jenerate.internal.util;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;

/**
 * Class providing code appending capabilities in order to reveal a certain piece of code in the editor.
 * 
 * @author maudrain
 */
public interface JavaUiCodeAppender {

    /**
     * @see JavaUI#revealInEditor(IEditorPart, IJavaElement)
     */
    void revealInEditor(IType objectClass, IJavaElement elementToReveal) throws JavaModelException, PartInitException;

}
