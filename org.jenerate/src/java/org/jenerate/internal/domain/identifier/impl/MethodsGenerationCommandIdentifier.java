package org.jenerate.internal.domain.identifier.impl;

import java.util.HashMap;
import java.util.Map;

import org.jenerate.internal.domain.identifier.CommandIdentifier;

/**
 * Default enum implementation of a {@link CommandIdentifier} specified for methods generation. The String identifier
 * corresponds to the command id as defined in the plugin.xml.
 * 
 * @author maudrain
 */
public enum MethodsGenerationCommandIdentifier implements CommandIdentifier {

    EQUALS_HASH_CODE("org.jenerate.commands.GenerateEqualsHashCodeCommand"),
    TO_STRING("org.jenerate.commands.GenerateToStringCommand"),
    COMPARE_TO("org.jenerate.commands.GenerateCompareToCommand");

    private static final Map<String, MethodsGenerationCommandIdentifier> IDENTIFIERS = new HashMap<String, MethodsGenerationCommandIdentifier>();

    static {
        for (MethodsGenerationCommandIdentifier userActionIdentifier : MethodsGenerationCommandIdentifier.values()) {
            IDENTIFIERS.put(userActionIdentifier.getIdentifier(), userActionIdentifier);
        }
    }

    private final String identifier;

    private MethodsGenerationCommandIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public static MethodsGenerationCommandIdentifier getUserActionIdentifierFor(String identifier) {
        if (!IDENTIFIERS.containsKey(identifier)) {
            throw new IllegalArgumentException();
        }
        return IDENTIFIERS.get(identifier);
    }

}
