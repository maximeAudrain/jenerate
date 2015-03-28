package org.jenerate.internal.manage;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Manager responsible for retrieving a {@link LinkedHashSet} of {@link MethodSkeleton} definitions for a specific
 * {@link CommandIdentifier}.
 * 
 * @author maudrain
 */
public interface MethodSkeletonManager {

    /**
     * Get all {@link MethodSkeleton}s for the provided parameter. XXX see if that can be done better: SortedSet, etc...
     * 
     * @param commandIdentifier the unique identifier of a certain command
     * @return the {@link LinkedHashSet} of {@link MethodSkeleton} for the given {@link CommandIdentifier}, or an empty
     *         set if not {@link MethodSkeleton}s could be found for the provided {@link CommandIdentifier}.
     */
    <T extends MethodGenerationData> LinkedHashSet<MethodSkeleton<T>> getMethodSkeletons(
            CommandIdentifier commandIdentifier);

}
