package org.jenerate.internal.strategy.method;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Defines a pair of {@link MethodSkeleton} and {@link MethodContent} as being a {@link Method}.
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodSkeleton} this {@link Method} holds
 * @param <U> the type of {@link MethodGenerationData} for this {@link Method}
 */
public interface Method<T extends MethodSkeleton<U>, U extends MethodGenerationData> {

    /**
     * @return the {@link MethodSkeleton} of this method
     */
    T getMethodSkeleton();

    /**
     * @return the {@link MethodContent} of this method
     */
    MethodContent<T, U> getMethodContent();

}
