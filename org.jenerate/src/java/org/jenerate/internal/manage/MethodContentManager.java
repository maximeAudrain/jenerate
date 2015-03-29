package org.jenerate.internal.manage;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Manager responsible for retrieving a {@link MethodContent} for a given {@link MethodSkeleton} and a
 * {@link StrategyIdentifier}.
 * 
 * @author maudrain
 */
public interface MethodContentManager {

    /**
     * Get the {@link MethodContent} for the provided parameters
     * 
     * @param methodSkeleton the {@link MethodSkeleton} for which a specific {@link MethodContent} will be retrieved
     * @param strategyIdentifier the unique identifier of a specific {@link MethodContent} strategy
     * @return the {@link MethodContent} for the provided parameters
     * @throws IllegalStateException if the {@link MethodContent} could not be retrieved for the provided parameters
     */
    <T extends MethodSkeleton<U>, U extends MethodGenerationData> MethodContent<T, U> getMethodContent(
            MethodSkeleton<U> methodSkeleton, StrategyIdentifier strategyIdentifier);

}
