// $Id$
package org.jenerate.internal.lang.generators;

import java.util.HashMap;
import java.util.Map;

import org.jenerate.internal.util.JavaUiCodeAppender;

/**
 * @author jiayun
 */
public final class GeneratorsManager {

    private static final String KEY_PREFIX = "org.jenerate.lang.actions.";

    public static final String COMPARETO_GENERATOR_KEY = KEY_PREFIX + "GenerateCompareToAction";
    public static final String EQUALS_HASHCODE_GENERATOR_KEY = KEY_PREFIX + "GenerateEqualsHashCodeAction";
    public static final String TOSTRING_GENERATOR_KEY = KEY_PREFIX + "GenerateToStringAction";

    private static final JavaUiCodeAppender JAVA_UI_CODE_APPENDER = new JavaUiCodeAppender();

    private final Map<String, ILangGenerator> generators = new HashMap<>();

    public GeneratorsManager() {
        generators.put(COMPARETO_GENERATOR_KEY, new CompareToGenerator(JAVA_UI_CODE_APPENDER));
        generators.put(EQUALS_HASHCODE_GENERATOR_KEY, new EqualsHashCodeGenerator(JAVA_UI_CODE_APPENDER));
        generators.put(TOSTRING_GENERATOR_KEY, new ToStringGenerator(JAVA_UI_CODE_APPENDER));
    }

    public ILangGenerator getGenerator(String key) {
        return generators.get(key);
    }
}
