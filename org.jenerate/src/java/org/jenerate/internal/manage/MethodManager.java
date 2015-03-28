package org.jenerate.internal.manage;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Manager responsible for retrieving a {@link LinkedHashSet} of {@link Method}s given a unique
 * {@link CommandIdentifier} .
 * 
 * @author maudrain
 */
public interface MethodManager {

    /**
     * Get all {@link Method}s for the provided argument. XXX see if that can be done better: SortedSet, etc...
     * 
     * @param commandIdentifier the unique identifier of a command
     * @return the {@link LinkedHashSet} of {@link Method}s for the provided argument, or an empty set if no
     *         {@link Method}s could be found for the provided {@link CommandIdentifier}
     */
    <T extends MethodSkeleton<U>, U extends MethodGenerationData> LinkedHashSet<Method<T, U>> getMethods(
            CommandIdentifier commandIdentifier);

}
