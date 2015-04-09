package org.jenerate.internal.manage;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Manager responsible for the {@link MethodContent} strategies.
 * 
 * @author maudrain
 */
public interface MethodContentManager {

    /**
     * Gets all the possible {@link StrategyIdentifier}s for a provided ordered set of {@link MethodSkeleton}s. The
     * returned ordered set of {@link StrategyIdentifier}s contains the INTERSECTION of all found strategies for the
     * provided {@link MethodSkeleton}s, e.g if skeleton1 has strategy1 and skeleton2 has strategy2, the returned set
     * will be empty. XXX see if that can be done better: SortedSet, etc...
     * 
     * @param methodSkeletons the {@link MethodSkeleton}s
     * @return the intersection of all the strategies for the provided {@link MethodSkeleton}s
     */
    <V extends MethodGenerationData> LinkedHashSet<StrategyIdentifier> getStrategiesIntersection(
            LinkedHashSet<MethodSkeleton<V>> methodSkeletons);

    /**
     * Return all fully formed {@link Method}s given their {@link MethodSkeleton}s and the identifier of a strategy for
     * the method content. XXX see if that can be done better: SortedSet, etc...
     * 
     * @param methodSkeletons the {@link MethodSkeleton}s for which one wants the full {@link Method}s
     * @param strategyIdentifier the unique identifier of a method content strategy
     * @return all {@link Method} strategiesfor the code generation
     */
    <T extends MethodSkeleton<V>, V extends MethodGenerationData> LinkedHashSet<Method<T, V>> getAllMethods(
            LinkedHashSet<MethodSkeleton<V>> methodSkeletons, StrategyIdentifier strategyIdentifier);

}
