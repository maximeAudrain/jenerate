// $Id$
package org.jenerate.internal.lang.generators;

import java.util.HashMap;
import java.util.Map;

import org.jenerate.internal.lang.generators.impl.GeneratorsCommonMethodsDelegateImpl;
import org.jenerate.internal.ui.dialogs.provider.impl.CompareToDialogProvider;
import org.jenerate.internal.ui.dialogs.provider.impl.EqualsHashCodeDialogProvider;
import org.jenerate.internal.ui.dialogs.provider.impl.ToStringDialogProvider;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.ui.preferences.impl.PreferencesManagerImpl;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;
import org.jenerate.internal.util.impl.JavaInterfaceCodeAppenderImpl;
import org.jenerate.internal.util.impl.JavaUiCodeAppenderImpl;
import org.jenerate.internal.util.impl.JeneratePluginCodeFormatterImpl;

/**
 * @author jiayun
 */
public final class GeneratorsManager {

    private static final String KEY_PREFIX = "org.jenerate.lang.actions.";

    public static final String COMPARETO_GENERATOR_KEY = KEY_PREFIX + "GenerateCompareToAction";
    public static final String EQUALS_HASHCODE_GENERATOR_KEY = KEY_PREFIX + "GenerateEqualsHashCodeAction";
    public static final String TOSTRING_GENERATOR_KEY = KEY_PREFIX + "GenerateToStringAction";

    private static final JavaUiCodeAppender JAVA_UI_CODE_APPENDER = new JavaUiCodeAppenderImpl();
    private static final PreferencesManager PREFERENCES_MANAGER = new PreferencesManagerImpl();
    private static final JeneratePluginCodeFormatter CODE_FORMATTER = new JeneratePluginCodeFormatterImpl();
    private static final GeneratorsCommonMethodsDelegate COMMON_METHODS_DELEGATE = new GeneratorsCommonMethodsDelegateImpl();
    private static final JavaInterfaceCodeAppender JAVA_INTERFACE_CODE_APPENDER = new JavaInterfaceCodeAppenderImpl();

    private final Map<String, ILangGenerator> generators = new HashMap<>();

    public GeneratorsManager() {
        generators.put(COMPARETO_GENERATOR_KEY, new CompareToGenerator(JAVA_UI_CODE_APPENDER, PREFERENCES_MANAGER,
                new CompareToDialogProvider(), CODE_FORMATTER, COMMON_METHODS_DELEGATE, JAVA_INTERFACE_CODE_APPENDER));
        generators.put(EQUALS_HASHCODE_GENERATOR_KEY, new EqualsHashCodeGenerator(JAVA_UI_CODE_APPENDER,
                PREFERENCES_MANAGER, new EqualsHashCodeDialogProvider(), CODE_FORMATTER, COMMON_METHODS_DELEGATE));
        generators.put(TOSTRING_GENERATOR_KEY, new ToStringGenerator(JAVA_UI_CODE_APPENDER, PREFERENCES_MANAGER,
                new ToStringDialogProvider(), CODE_FORMATTER, COMMON_METHODS_DELEGATE));
    }

    public ILangGenerator getGenerator(String key) {
        return generators.get(key);
    }
}
