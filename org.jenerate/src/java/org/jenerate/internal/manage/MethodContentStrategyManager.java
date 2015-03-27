package org.jenerate.internal.manage;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

public interface MethodContentStrategyManager {

    <T extends MethodSkeleton<U>, U extends JenerateDialogData> MethodContent<T, U> getStrategy(
            MethodSkeleton<U> methodSkeleton,
            MethodContentStrategyIdentifier methodContentStrategyIdentifier);

}
