/**
 * Copyright (c) 2014 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.jenerate.internal.lang.handlers;

import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.Method;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.lang.generators.MethodGenerator;
import org.jenerate.internal.lang.generators.impl.GeneratorsCommonMethodsDelegateImpl;
import org.jenerate.internal.manage.MethodGeneratorManager;
import org.jenerate.internal.manage.MethodManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.manage.impl.MethodGeneratorManagerImpl;
import org.jenerate.internal.manage.impl.MethodManagerImpl;
import org.jenerate.internal.manage.impl.PreferencesManagerImpl;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;
import org.jenerate.internal.util.impl.JavaInterfaceCodeAppenderImpl;
import org.jenerate.internal.util.impl.JavaUiCodeAppenderImpl;
import org.jenerate.internal.util.impl.JeneratePluginCodeFormatterImpl;

/**
 * Handler that determine which generation should be performed depending on the event commandId. It also ensures that
 * the currently selected object in the editor is a class in order to be able to perform the generation. The
 * {@link GenerateHandler#generate(String, ISelection, ICompilationUnit, Shell)} method is extracted from the
 * {@link IEditorActionDelegate} written by jiayun previously in the plugin.
 * 
 * @author jiayun, maudrain
 */
public class GenerateHandler extends AbstractHandler {

    private static final JavaUiCodeAppender JAVA_UI_CODE_APPENDER = new JavaUiCodeAppenderImpl();
    private static final PreferencesManager PREFERENCES_MANAGER = new PreferencesManagerImpl();
    private static final JeneratePluginCodeFormatter CODE_FORMATTER = new JeneratePluginCodeFormatterImpl();
    private static final GeneratorsCommonMethodsDelegate COMMON_METHODS_DELEGATE = new GeneratorsCommonMethodsDelegateImpl();
    private static final JavaInterfaceCodeAppender JAVA_INTERFACE_CODE_APPENDER = new JavaInterfaceCodeAppenderImpl();

    private final MethodManager methodManager;
    private final MethodGeneratorManager generatorManager;

    public GenerateHandler() {
        this.methodManager = new MethodManagerImpl(PREFERENCES_MANAGER, COMMON_METHODS_DELEGATE,
                JAVA_INTERFACE_CODE_APPENDER);
        this.generatorManager = new MethodGeneratorManagerImpl(PREFERENCES_MANAGER, COMMON_METHODS_DELEGATE,
                JAVA_INTERFACE_CODE_APPENDER, JAVA_UI_CODE_APPENDER, CODE_FORMATTER);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell parentShell = HandlerUtil.getActiveShell(event);
        IEditorPart editor = HandlerUtil.getActiveEditor(event);
        ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
        IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
        ICompilationUnit compilationUnit = manager.getWorkingCopy(editor.getEditorInput());
        generate(event.getCommand().getId(), currentSelection, compilationUnit, parentShell);
        return null;
    }

    private void generate(String commandId, ISelection iSelection, ICompilationUnit compilationUnit, Shell parentShell) {
        IType objectClass = null;
        try {
            ITextSelection selection = (ITextSelection) iSelection;
            IJavaElement element = compilationUnit.getElementAt(selection.getOffset());
            if (element != null) {
                objectClass = (IType) element.getAncestor(IJavaElement.TYPE);
            }
        } catch (JavaModelException e) {
            MessageDialog.openError(parentShell, "Error", e.getMessage());
        }

        if (objectClass == null) {
            objectClass = compilationUnit.findPrimaryType();
        }

        try {
            if (objectClass == null || !objectClass.isClass()) {
                MessageDialog.openInformation(parentShell, "Method Generation",
                        "Cursor not in a class, or no class has the same name with the Java file.");
            } else {
                UserActionIdentifier userActionIdentifier = UserActionIdentifier.getUserActionIdentifierFor(commandId);
                generateCode(parentShell, objectClass, userActionIdentifier);
            }
        } catch (Exception exception) {
            MessageDialog.openError(parentShell, "Error", exception.getMessage());
        }
    }

    private <T extends MethodSkeleton<V>, U extends FieldDialog<V>, V extends MethodGenerationData> void generateCode(
            Shell parentShell, IType objectClass, UserActionIdentifier userActionIdentifier) {
        Set<Method<T, V>> methods = methodManager.getMethods(userActionIdentifier);
        MethodGenerator<T, U, V> genericGenerator = generatorManager.getMethodGenerator(userActionIdentifier);
        genericGenerator.generate(parentShell, objectClass, methods);
    }
}
