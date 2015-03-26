package org.jenerate.internal.domain.method.impl;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.method.Method;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

public class MethodImpl<T extends MethodSkeleton<U>, U extends JenerateDialogData> implements Method<T, U> {

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
