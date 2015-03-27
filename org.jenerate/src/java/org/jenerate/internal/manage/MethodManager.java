package org.jenerate.internal.manage;

import java.util.Set;

import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.Method;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

/**
 * Manager responsible for retrieving a set of {@link Method}s given a unique {@link UserActionIdentifier}.
 * 
 * @author maudrain
 */
public interface MethodManager {

    /**
     * Get all {@link Method}s for the provided argument
     * 
     * @param userActionIdentifier the unique identifier of a user action
     * @return the Set of {@link Method}s for the provided argument, or an empty set if no {@link Method}s could be
     *         found for the provided {@link UserActionIdentifier}
     */
    <T extends MethodSkeleton<U>, U extends MethodGenerationData> Set<Method<T, U>> getMethods(
            UserActionIdentifier userActionIdentifier);

}
