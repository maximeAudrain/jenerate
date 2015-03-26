package org.jenerate.internal.manage;

import org.jenerate.internal.domain.impl.MethodContentStrategyIdentifier;

public interface MethodContentStrategyManager {

    MethodContentStrategy<?> getStrategy(MethodContentStrategyIdentifier identifier);

}
