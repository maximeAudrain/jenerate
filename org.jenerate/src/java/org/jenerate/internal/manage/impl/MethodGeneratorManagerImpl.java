package org.jenerate.internal.manage.impl;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.lang.generators.MethodGenerator;
import org.jenerate.internal.lang.generators.impl.MethodGeneratorImpl;
import org.jenerate.internal.manage.DialogProviderManager;
import org.jenerate.internal.manage.MethodGeneratorManager;
import org.jenerate.internal.ui.dialogs.JenerateDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;

public class MethodGeneratorManagerImpl implements MethodGeneratorManager {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final JeneratePluginCodeFormatter jeneratePluginCodeFormatter;
    private final DialogProviderManager dialogProviderManager;

    public MethodGeneratorManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender, JavaUiCodeAppender javaUiCodeAppender,
            JeneratePluginCodeFormatter jeneratePluginCodeFormatter) {
        this.dialogProviderManager = new DialogProviderManagerImpl(preferencesManager, generatorsCommonMethodsDelegate,
                javaInterfaceCodeAppender);
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.jeneratePluginCodeFormatter = jeneratePluginCodeFormatter;

    }

    @Override
    public <T extends MethodSkeleton<V>, U extends JenerateDialog<V>, V extends JenerateDialogData> MethodGenerator<T, U, V> getGenericGenerator(
            UserActionIdentifier userActionIdentifier) {
        DialogProvider<U, V> dialogProvider = dialogProviderManager.getDialogProvider(userActionIdentifier);
        return new MethodGeneratorImpl<T, U, V>(dialogProvider, javaUiCodeAppender, jeneratePluginCodeFormatter);
    }

}
