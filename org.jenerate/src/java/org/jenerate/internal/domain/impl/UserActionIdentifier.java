package org.jenerate.internal.domain.impl;

import org.jenerate.internal.lang.generators.GeneratorsManager;

public enum UserActionIdentifier {

    EQUALS_HASH_CODE(GeneratorsManager.EQUALS_HASHCODE_GENERATOR_KEY),
    TO_STRING(GeneratorsManager.TOSTRING_GENERATOR_KEY),
    COMPARE_TO(GeneratorsManager.COMPARETO_GENERATOR_KEY);

    private final String identifier;

    private UserActionIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

}
