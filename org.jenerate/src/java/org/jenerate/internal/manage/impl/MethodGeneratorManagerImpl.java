package org.jenerate.internal.manage.impl;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.generate.method.MethodGenerator;
import org.jenerate.internal.generate.method.impl.MethodGeneratorImpl;
import org.jenerate.internal.generate.method.util.JavaCodeFormatter;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;
import org.jenerate.internal.manage.DialogFactoryManager;
import org.jenerate.internal.manage.MethodGeneratorManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class MethodGeneratorManagerImpl implements MethodGeneratorManager {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final JavaCodeFormatter jeneratePluginCodeFormatter;
    private final DialogFactoryManager dialogProviderManager;

    public MethodGeneratorManagerImpl(PreferencesManager preferencesManager,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender, JavaUiCodeAppender javaUiCodeAppender,
            JavaCodeFormatter jeneratePluginCodeFormatter) {
        this.dialogProviderManager = new DialogFactoryManagerImpl(preferencesManager, javaInterfaceCodeAppender);
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.jeneratePluginCodeFormatter = jeneratePluginCodeFormatter;

    }

    @Override
    public <T extends MethodSkeleton<V>, U extends FieldDialog<V>, V extends MethodGenerationData> MethodGenerator<T, U, V> getMethodGenerator(
            CommandIdentifier commandIdentifier) {
        try {
            DialogFactory<U, V> dialogProvider = dialogProviderManager.getDialogFactory(commandIdentifier);
            return new MethodGeneratorImpl<T, U, V>(dialogProvider, javaUiCodeAppender, jeneratePluginCodeFormatter);
        } catch (IllegalStateException exception) {
            throw exception;
        }
    }

}
