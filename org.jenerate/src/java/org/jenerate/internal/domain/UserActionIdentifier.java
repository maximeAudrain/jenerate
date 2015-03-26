package org.jenerate.internal.domain;

import java.util.HashMap;
import java.util.Map;

public enum UserActionIdentifier {

    EQUALS_HASH_CODE("org.jenerate.lang.actions.GenerateEqualsHashCodeAction"),
    TO_STRING("org.jenerate.lang.actions.GenerateToStringAction"),
    COMPARE_TO("org.jenerate.lang.actions.GenerateCompareToAction");

    private static final Map<String, UserActionIdentifier> IDENTIFIERS = new HashMap<String, UserActionIdentifier>();

    static {
        for (UserActionIdentifier userActionIdentifier : UserActionIdentifier.values()) {
            IDENTIFIERS.put(userActionIdentifier.getIdentifier(), userActionIdentifier);
        }
    }

    private final String identifier;

    private UserActionIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static UserActionIdentifier getUserActionIdentifierFor(String identifier) {
        if (!IDENTIFIERS.containsKey(identifier)) {
            throw new IllegalArgumentException();
        }
        return IDENTIFIERS.get(identifier);
    }

}
