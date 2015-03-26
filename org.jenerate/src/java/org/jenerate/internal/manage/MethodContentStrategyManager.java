package org.jenerate.internal.manage;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

public interface MethodContentStrategyManager {

    MethodContent<? extends MethodSkeleton<?>, ? extends JenerateDialogData> getStrategy(
            MethodSkeleton<? extends JenerateDialogData> methodSkeleton,
            MethodContentStrategyIdentifier methodContentStrategyIdentifier);

}
