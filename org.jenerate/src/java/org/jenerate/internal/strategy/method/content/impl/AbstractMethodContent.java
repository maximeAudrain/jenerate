package org.jenerate.internal.strategy.method.content.impl;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

public abstract class AbstractMethodContent<T extends MethodSkeleton<U>, U extends MethodGenerationData> implements
        MethodContent<T, U> {

    protected final StrategyIdentifier strategyIdentifier;
    protected final PreferencesManager preferencesManager;

    public AbstractMethodContent(StrategyIdentifier strategyIdentifier,
            PreferencesManager preferencesManager) {
        this.strategyIdentifier = strategyIdentifier;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public final StrategyIdentifier getStrategyIdentifier() {
        return strategyIdentifier;
    }

}
