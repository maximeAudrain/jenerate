package org.jenerate.internal.manage;

import java.util.Set;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.method.Method;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

public interface MethodStrategyManager {

    <T extends MethodSkeleton<U>, U extends JenerateDialogData> Set<Method<T, U>> getMethods(
            UserActionIdentifier userActionIdentifier);

}
