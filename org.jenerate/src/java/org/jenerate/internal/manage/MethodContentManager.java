package org.jenerate.internal.manage;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;

/**
 * Manager responsible for retrieving a {@link MethodContent} for a given {@link MethodSkeleton} and a
 * {@link MethodContentStrategyIdentifier}.
 * 
 * @author maudrain
 */
public interface MethodContentManager {

    /**
     * Get the {@link MethodContent} for the provided parameters
     * 
     * @param methodSkeleton the {@link MethodSkeleton} for which a specific {@link MethodContent} will be retrieved
     * @param methodContentStrategyIdentifier the unique identifier of a specific {@link MethodContent} strategy
     * @return the {@link MethodContent} for the provided parameters
     * @throws IllegalStateException if the {@link MethodContent} could not be retrieved for the provided parameters
     */
    <T extends MethodSkeleton<U>, U extends JenerateDialogData> MethodContent<T, U> getMethodContent(
            MethodSkeleton<U> methodSkeleton, MethodContentStrategyIdentifier methodContentStrategyIdentifier);

}
