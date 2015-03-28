package org.jenerate.internal.strategy.method.content;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Defines content of a single method.
 * 
 * @author maudrain
 */
public interface MethodContent<T extends MethodSkeleton<U>, U extends MethodGenerationData> {

    String getMethodContent(IType objectClass, U data) throws Exception;

    /**
     * XXX see if that can be done better: SortedSet, etc...
     */
    LinkedHashSet<String> getLibrariesToImport(U data);

    Class<T> getRelatedMethodSkeletonClass();

    MethodContentStrategyIdentifier getMethodContentStrategyIdentifier();
}
