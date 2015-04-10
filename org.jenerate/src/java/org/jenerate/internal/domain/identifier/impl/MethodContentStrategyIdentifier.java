package org.jenerate.internal.domain.identifier.impl;

import org.jenerate.internal.domain.identifier.StrategyIdentifier;

/**
 * Contains all different identifiers of method content strategies
 * 
 * @author maudrain
 */
public enum MethodContentStrategyIdentifier implements StrategyIdentifier {

    USE_COMMONS_LANG,
    USE_COMMONS_LANG3,
    USE_GUAVA;

    @Override
    public String getName() {
        return name();
    }
}
