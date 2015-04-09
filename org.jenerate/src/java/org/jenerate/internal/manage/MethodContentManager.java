package org.jenerate.internal.manage;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Manager responsible for retrieving a {@link MethodContent} for a given {@link MethodSkeleton} and a
 * {@link StrategyIdentifier}.
 * 
 * @author maudrain
 */
public interface MethodContentManager {

    <V extends MethodGenerationData> LinkedHashSet<StrategyIdentifier> getPossibleStrategies(
            LinkedHashSet<MethodSkeleton<V>> methodSkeletons);
    
    <T extends MethodSkeleton<V>, V extends MethodGenerationData> LinkedHashSet<Method<T, V>> getAllMethods(
            LinkedHashSet<MethodSkeleton<V>> methodSkeletons, StrategyIdentifier selectedContentStrategy);

}
