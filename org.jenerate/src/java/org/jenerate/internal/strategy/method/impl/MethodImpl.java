package org.jenerate.internal.strategy.method.impl;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Default implementation of the {@link Method} interface
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodSkeleton} this {@link Method} holds
 * @param <U> the type of {@link MethodGenerationData} for this {@link Method}
 */
public final class MethodImpl<T extends MethodSkeleton<U>, U extends MethodGenerationData> implements Method<T, U> {

    private final T methodSkeleton;
    private final MethodContent<T, U> methodContent;

    public MethodImpl(T methodSkeleton, MethodContent<T, U> methodContent) {
        this.methodSkeleton = methodSkeleton;
        this.methodContent = methodContent;
    }

    @Override
    public T getMethodSkeleton() {
        return methodSkeleton;
    }

    @Override
    public MethodContent<T, U> getMethodContent() {
        return methodContent;
    }
}
