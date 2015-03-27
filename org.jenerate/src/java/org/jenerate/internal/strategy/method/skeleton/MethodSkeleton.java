package org.jenerate.internal.strategy.method.skeleton;

import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;

/**
 * Define a method that can be generated. The method skeleton that can be generated depends on the user specific needs
 * provided by the {@link MethodGenerationData}
 * 
 * @author maudrain
 */
public interface MethodSkeleton<T extends MethodGenerationData> {

    String getMethod(IType objectClass, T data, String methodContent) throws Exception;

    Set<String> getLibrariesToImport();

    UserActionIdentifier getUserActionIdentifier();

    String getMethodName();

    String[] getMethodArguments(IType objectClass) throws Exception;

}
