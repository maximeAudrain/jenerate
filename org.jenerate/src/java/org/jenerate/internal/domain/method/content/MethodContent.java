package org.jenerate.internal.domain.method.content;

import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.JenerateDialogData;


/**
 * Defines content of a single method.
 * 
 * @author maudrain
 */
public interface MethodContent<T extends JenerateDialogData> {

    String getMethodContent(IType objectClass, T data) throws Exception;
    
    Set<String> getLibrariesToImport(T data);
}
