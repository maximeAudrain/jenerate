package org.jenerate.internal.strategy.method.content.impl;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

public abstract class AbstractMethodContent<T extends MethodSkeleton<U>, U extends MethodGenerationData> implements
        MethodContent<T, U> {

    protected final MethodContentStrategyIdentifier methodContentStrategyIdentifier;
    protected final PreferencesManager preferencesManager;
    protected final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public AbstractMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        this.methodContentStrategyIdentifier = methodContentStrategyIdentifier;
        this.preferencesManager = preferencesManager;
        this.generatorsCommonMethodsDelegate = generatorsCommonMethodsDelegate;
    }

    @Override
    public final MethodContentStrategyIdentifier getMethodContentStrategyIdentifier() {
        return methodContentStrategyIdentifier;
    }

}
