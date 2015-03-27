package org.jenerate.internal.manage.impl;

import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.lang.generators.MethodGenerator;
import org.jenerate.internal.lang.generators.impl.MethodGeneratorImpl;
import org.jenerate.internal.manage.DialogFactoryManager;
import org.jenerate.internal.manage.MethodGeneratorManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;

public class MethodGeneratorManagerImpl implements MethodGeneratorManager {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final JeneratePluginCodeFormatter jeneratePluginCodeFormatter;
    private final DialogFactoryManager dialogProviderManager;

    public MethodGeneratorManagerImpl(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender, JavaUiCodeAppender javaUiCodeAppender,
            JeneratePluginCodeFormatter jeneratePluginCodeFormatter) {
        this.dialogProviderManager = new DialogFactoryManagerImpl(preferencesManager, generatorsCommonMethodsDelegate,
                javaInterfaceCodeAppender);
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.jeneratePluginCodeFormatter = jeneratePluginCodeFormatter;

    }

    @Override
    public <T extends MethodSkeleton<V>, U extends FieldDialog<V>, V extends MethodGenerationData> MethodGenerator<T, U, V> getMethodGenerator(
            UserActionIdentifier userActionIdentifier) {
        try {
            DialogFactory<U, V> dialogProvider = dialogProviderManager.getDialogFactory(userActionIdentifier);
            return new MethodGeneratorImpl<T, U, V>(dialogProvider, javaUiCodeAppender, jeneratePluginCodeFormatter);
        } catch (IllegalStateException exception) {
            throw exception;
        }
    }

}
