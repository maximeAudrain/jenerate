package org.jenerate.internal.manage;

import java.util.Set;

import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

/**
 * Manager responsible for retrieving a set of {@link MethodSkeleton} definitions for a specific
 * {@link UserActionIdentifier}.
 * 
 * @author maudrain
 */
public interface MethodSkeletonManager {

    /**
     * Get all {@link MethodSkeleton}s for the provided parameter
     * 
     * @param userActionIdentifier the unique identifier of a certain user action
     * @return the Set of {@link MethodSkeleton} for the given {@link UserActionIdentifier}, or an empty set if not
     *         {@link MethodSkeleton}s could be found for the provided {@link UserActionIdentifier}.
     */
    <T extends MethodGenerationData> Set<MethodSkeleton<T>> getMethodSkeletons(UserActionIdentifier userActionIdentifier);

}
