package org.jenerate.internal.domain.method.content;

import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;

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
