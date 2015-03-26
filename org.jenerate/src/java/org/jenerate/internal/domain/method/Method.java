package org.jenerate.internal.domain.method;

import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.JenerateDialogData;

/**
 * Define a method that can be generated. The method skeleton that can be generated depends on the user specific needs
 * provided by the {@link JenerateDialogData}
 * 
 * @author maudrain
 */
public interface Method<T extends JenerateDialogData> {

    String getMethod(IType objectClass, T data, String methodContent) throws JavaModelException;

    Set<String> getLibrariesToImport();

}
