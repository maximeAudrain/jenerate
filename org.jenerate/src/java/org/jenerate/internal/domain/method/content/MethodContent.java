package org.jenerate.internal.domain.method.content;

import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

/**
 * Defines content of a single method.
 * 
 * @author maudrain
 */
public interface MethodContent<T extends MethodSkeleton<U>, U extends JenerateDialogData> {

    String getMethodContent(IType objectClass, U data) throws Exception;

    Set<String> getLibrariesToImport(U data);

    Class<T> getRelatedMethodSkeletonClass();

    MethodContentStrategyIdentifier getMethodContentStrategyIdentifier();
}
