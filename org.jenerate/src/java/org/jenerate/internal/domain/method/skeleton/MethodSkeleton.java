package org.jenerate.internal.domain.method.skeleton;

import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;

/**
 * Define a method that can be generated. The method skeleton that can be generated depends on the user specific needs
 * provided by the {@link JenerateDialogData}
 * 
 * @author maudrain
 */
public interface MethodSkeleton<T extends JenerateDialogData> {

    String getMethod(IType objectClass, T data, String methodContent) throws JavaModelException;

    Set<String> getLibrariesToImport();

    UserActionIdentifier getUserActionIdentifier();

    String getMethodName();

}
