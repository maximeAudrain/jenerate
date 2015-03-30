package org.jenerate.internal.strategy.method.content.impl;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Abstract implementation of the {@link MethodContent}
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodSkeleton} related to this {@link MethodContent}
 * @param <U> the type of {@link MethodGenerationData} for this {@link MethodContent}
 */
public abstract class AbstractMethodContent<T extends MethodSkeleton<U>, U extends MethodGenerationData> implements
        MethodContent<T, U> {

    private final StrategyIdentifier strategyIdentifier;
    protected final PreferencesManager preferencesManager;

    /**
     * Constructor
     * 
     * @param strategyIdentifier the strategy identifier for this {@link MethodContent}
     * @param preferencesManager the preference manager
     */
    public AbstractMethodContent(StrategyIdentifier strategyIdentifier, PreferencesManager preferencesManager) {
        this.strategyIdentifier = strategyIdentifier;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public final StrategyIdentifier getStrategyIdentifier() {
        return strategyIdentifier;
    }

}
