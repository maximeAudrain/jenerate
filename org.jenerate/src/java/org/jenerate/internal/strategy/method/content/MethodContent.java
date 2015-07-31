package org.jenerate.internal.strategy.method.content;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Defines the {@link MethodContent} strategy of a method. A {@link MethodContent} has a 1..1 relationship with a
 * {@link StrategyIdentifier}. Also, a {@link MethodContent} has a 1..1 relationship with a {@link MethodSkeleton}. A
 * {@link MethodContent} has an ordered set of libraries to be imported for this content to compile once generated. One
 * can generate the string content of a method using {@link MethodContent#getMethodContent(IType, MethodGenerationData)}
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodSkeleton} related to this {@link MethodContent}
 * @param <U> the type of {@link MethodGenerationData} for this {@link MethodContent}
 */
public interface MethodContent<T extends MethodSkeleton<U>, U extends MethodGenerationData> {

    /**
     * Gets the method content from the provided arguments
     * 
     * @param objectClass the object class where the method content will be generated
     * @param data the generation data containing information provided by the user on how the content should be
     *            generated.
     * @return the generated string content of a method
     * @throws Exception if a problem occurred when generating the method content
     */
    String getMethodContent(IType objectClass, U data) throws Exception;

    /**
     * @return the ordered set of libraries to import for this {@link MethodContent}.
     */
    LinkedHashSet<String> getLibrariesToImport(U data);

    /**
     * @return the related {@link MethodSkeleton} class for this {@link MethodContent}
     */
    Class<T> getRelatedMethodSkeletonClass();

    /**
     * @return the {@link StrategyIdentifier} for this {@link MethodContent}
     */
    StrategyIdentifier getStrategyIdentifier();
}
