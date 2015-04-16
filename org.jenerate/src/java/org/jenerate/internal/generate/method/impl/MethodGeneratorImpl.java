package org.jenerate.internal.generate.method.impl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.generate.method.MethodGenerator;
import org.jenerate.internal.generate.method.util.JavaCodeFormatter;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;
import org.jenerate.internal.manage.MethodContentManager;
import org.jenerate.internal.manage.MethodSkeletonManager;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;

/**
 * Default implementation of the {@link MethodGenerator}
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodSkeleton} for this method generator
 * @param <V> the type of {@link MethodGenerationData} this generator handles
 */
public final class MethodGeneratorImpl<T extends MethodSkeleton<U>, U extends MethodGenerationData> implements
        MethodGenerator<T, U> {

    private final DialogFactory<U> dialogFactory;
    private final JavaUiCodeAppender javaUiCodeAppender;
    private final JavaCodeFormatter jeneratePluginCodeFormatter;
    private final MethodSkeletonManager methodSkeletonManager;
    private final MethodContentManager methodContentManager;

    public MethodGeneratorImpl(DialogFactory<U> dialogFactory, JavaUiCodeAppender javaUiCodeAppender,
            JavaCodeFormatter jeneratePluginCodeFormatter, MethodSkeletonManager methodSkeletonManager,
            MethodContentManager methodContentManager) {
        this.dialogFactory = dialogFactory;
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.jeneratePluginCodeFormatter = jeneratePluginCodeFormatter;
        this.methodSkeletonManager = methodSkeletonManager;
        this.methodContentManager = methodContentManager;
    }

    @Override
    public void generate(Shell parentShell, IType objectClass, CommandIdentifier commandIdentifier) {
        LinkedHashSet<MethodSkeleton<U>> methodSkeletons = methodSkeletonManager.getMethodSkeletons(commandIdentifier);
        LinkedHashSet<StrategyIdentifier> strategyIdentifiers = methodContentManager
                .getStrategiesIntersection(methodSkeletons);

        try {
            Set<IMethod> excludedMethods = getExcludedMethods(objectClass, methodSkeletons);
            FieldDialog<U> dialog = dialogFactory.createDialog(parentShell, objectClass, excludedMethods,
                    strategyIdentifiers);
            int returnCode = dialog.getDialog().open();
            if (returnCode == Window.OK) {

                for (IMethod excludedMethod : excludedMethods) {
                    excludedMethod.delete(true, null);
                }

                U data = dialog.getData();
                StrategyIdentifier selectedContentStrategy = data.getSelectedStrategyIdentifier();
                LinkedHashSet<Method<T, U>> methods = methodContentManager.getAllMethods(methodSkeletons,
                        selectedContentStrategy);
                generateCode(parentShell, objectClass, data, methods);
            }

        } catch (Exception exception) {
            MessageDialog.openError(parentShell, "Method Generation Failed", exception.getMessage());
        }
    }

    private Set<IMethod> getExcludedMethods(IType objectClass, LinkedHashSet<MethodSkeleton<U>> methodSkeletons)
            throws Exception {
        Set<IMethod> excludedMethods = new HashSet<IMethod>();
        for (MethodSkeleton<U> methodSkeleton : methodSkeletons) {
            IMethod excludedMethod = objectClass.getMethod(methodSkeleton.getMethodName(),
                    methodSkeleton.getMethodArguments(objectClass));
            if (excludedMethod.exists()) {
                excludedMethods.add(excludedMethod);
            }
        }
        return excludedMethods;
    }

    private void generateCode(Shell parentShell, IType objectClass, U data, LinkedHashSet<Method<T, U>> methods)
            throws Exception {
        IJavaElement currentPosition = data.getElementPosition();
        for (Method<T, U> method : methods) {
            MethodContent<T, U> methodContent = method.getMethodContent();
            T methodSkeleton = method.getMethodSkeleton();
            String methodContentString = methodContent.getMethodContent(objectClass, data);
            String source = methodSkeleton.getMethod(objectClass, data, methodContentString);

            for (String libraryToImport : methodSkeleton.getLibrariesToImport()) {
                objectClass.getCompilationUnit().createImport(libraryToImport, null, null);
            }
            for (String libraryToImport : methodContent.getLibrariesToImport(data)) {
                objectClass.getCompilationUnit().createImport(libraryToImport, null, null);
            }
            String formattedContent = format(parentShell, objectClass, source);
            currentPosition = objectClass.createMethod(formattedContent, currentPosition, true, null);
        }
        javaUiCodeAppender.revealInEditor(objectClass, currentPosition);
    }

    private String format(final Shell parentShell, final IType objectClass, String source) {
        try {
            return jeneratePluginCodeFormatter.formatCode(objectClass, source);
        } catch (Exception exception) {
            MessageDialog.openError(parentShell, "Error formatting code '" + source + "'", exception.getMessage());
            return "";
        }
    }
}
