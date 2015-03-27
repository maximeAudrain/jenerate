package org.jenerate.internal.domain.method.content;

import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

/**
 * Defines content of a single method.
 * 
 * @author maudrain
 */
public interface MethodContent<T extends MethodSkeleton<U>, U extends MethodGenerationData> {

    String getMethodContent(IType objectClass, U data) throws Exception;

    Set<String> getLibrariesToImport(U data);

    Class<T> getRelatedMethodSkeletonClass();

    MethodContentStrategyIdentifier getMethodContentStrategyIdentifier();
}
