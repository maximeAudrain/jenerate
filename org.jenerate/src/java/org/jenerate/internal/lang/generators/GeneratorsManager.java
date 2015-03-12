// $Id$
package org.jenerate.internal.lang.generators;

import java.util.HashMap;
import java.util.Map;

import org.jenerate.internal.ui.dialogs.provider.impl.CompareToDialogProvider;
import org.jenerate.internal.ui.dialogs.provider.impl.EqualsHashCodeDialogProvider;
import org.jenerate.internal.ui.dialogs.provider.impl.ToStringDialogProvider;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.ui.preferences.impl.PreferencesManagerImpl;
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
    private static final PreferencesManager PREFERENCES_MANAGER = new PreferencesManagerImpl();

    private final Map<String, ILangGenerator> generators = new HashMap<>();

    public GeneratorsManager() {
        generators.put(COMPARETO_GENERATOR_KEY, new CompareToGenerator(JAVA_UI_CODE_APPENDER, PREFERENCES_MANAGER,
                new CompareToDialogProvider()));
        generators.put(EQUALS_HASHCODE_GENERATOR_KEY, new EqualsHashCodeGenerator(JAVA_UI_CODE_APPENDER,
                PREFERENCES_MANAGER, new EqualsHashCodeDialogProvider()));
        generators.put(TOSTRING_GENERATOR_KEY, new ToStringGenerator(JAVA_UI_CODE_APPENDER, PREFERENCES_MANAGER,
                new ToStringDialogProvider()));
    }

    public ILangGenerator getGenerator(String key) {
        return generators.get(key);
    }
}
