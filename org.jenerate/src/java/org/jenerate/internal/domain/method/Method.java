package org.jenerate.internal.domain.method;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

public interface Method<T extends MethodSkeleton<U>, U extends JenerateDialogData> {

    T getMethodSkeleton();

    MethodContent<T, U> getMethodContent();

}
