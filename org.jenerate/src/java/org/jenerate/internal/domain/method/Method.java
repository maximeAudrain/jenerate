package org.jenerate.internal.domain.method;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

public interface Method<T extends MethodSkeleton<U>, U extends MethodGenerationData> {

    T getMethodSkeleton();

    MethodContent<T, U> getMethodContent();

}
