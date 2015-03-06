// $Id$
package org.jenerate.internal.lang.generators;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiayun
 */
public final class LangGenerators {

    private static final String KEY_PREFIX = "org.jenerate.lang.actions.";

    public static final String COMPARETO_GENERATOR_KEY = KEY_PREFIX + "GenerateCompareToAction";

    public static final String EQUALS_HASHCODE_GENERATOR_KEY = KEY_PREFIX + "GenerateEqualsHashCodeAction";

    public static final String TOSTRING_GENERATOR_KEY = KEY_PREFIX + "GenerateToStringAction";

    private static final Map<String, ILangGenerator> generators = new HashMap<>();

    static {
        generators.put(COMPARETO_GENERATOR_KEY, CompareToGenerator.getInstance());
        generators.put(EQUALS_HASHCODE_GENERATOR_KEY, EqualsHashCodeGenerator.getInstance());
        generators.put(TOSTRING_GENERATOR_KEY, ToStringGenerator.getInstance());
    }

    private LangGenerators() {
    }

    public static ILangGenerator getGenerator(String key) {
        return generators.get(key);
    }
}
