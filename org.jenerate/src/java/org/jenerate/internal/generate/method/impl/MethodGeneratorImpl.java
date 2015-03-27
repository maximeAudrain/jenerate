package org.jenerate.internal.generate.method.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.generate.method.MethodGenerator;
import org.jenerate.internal.generate.method.util.JavaCodeFormatter;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;

public final class MethodGeneratorImpl<T extends MethodSkeleton<V>, U extends FieldDialog<V>, V extends MethodGenerationData>
        implements MethodGenerator<T, U, V> {

    private final DialogFactory<U, V> dialogFactory;
    private final JavaUiCodeAppender javaUiCodeAppender;
    private final JavaCodeFormatter jeneratePluginCodeFormatter;

    public MethodGeneratorImpl(DialogFactory<U, V> dialogFactory, JavaUiCodeAppender javaUiCodeAppender,
            JavaCodeFormatter jeneratePluginCodeFormatter) {
        this.dialogFactory = dialogFactory;
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.jeneratePluginCodeFormatter = jeneratePluginCodeFormatter;
    }

    @Override
    public void generate(Shell parentShell, IType objectClass, Set<Method<T, V>> methods) {
        try {
            Set<IMethod> excludedMethods = getExcludedMethods(objectClass, methods);
            U dialog = dialogFactory.createDialog(parentShell, objectClass, excludedMethods);
            int returnCode = dialog.getDialog().open();
            if (returnCode == Window.OK) {

                for (IMethod excludedMethod : excludedMethods) {
                    if (excludedMethod.exists()) {
                        excludedMethod.delete(true, null);
                    }
                }

                generateCode(parentShell, objectClass, dialog.getData(), methods);
            }

        } catch (Exception e) {
            MessageDialog.openError(parentShell, "MethodSkeleton Generation Failed", e.getMessage());
        }
    }

    private Set<IMethod> getExcludedMethods(IType objectClass, Set<Method<T, V>> methods) throws Exception {
        Set<IMethod> excludedMethods = new HashSet<IMethod>();
        for (Method<T, V> method : methods) {
            T methodSkeleton = method.getMethodSkeleton();
            IMethod excludedMethod = objectClass.getMethod(methodSkeleton.getMethodName(),
                    methodSkeleton.getMethodArguments(objectClass));
            if (excludedMethod.exists()) {
                excludedMethods.add(excludedMethod);
            }
        }
        return excludedMethods;
    }

    private void generateCode(Shell parentShell, IType objectClass, V data, Set<Method<T, V>> methods) throws Exception {
        IJavaElement currentPosition = data.getElementPosition();
        for (Method<T, V> method : methods) {
            MethodContent<T, V> methodContent = method.getMethodContent();
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

    private String format(final Shell parentShell, final IType objectClass, String source) throws JavaModelException {
        try {
            return jeneratePluginCodeFormatter.formatCode(objectClass, source);
        } catch (BadLocationException e) {
            MessageDialog.openError(parentShell, "Error", e.getMessage());
            return "";
        }
    }
}
