/**
 * Copyright (c) 2014 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.jenerate.internal.lang.handlers;

import java.util.HashSet;
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
import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.method.Method;
import org.jenerate.internal.domain.method.content.MethodContent;
import org.jenerate.internal.domain.method.impl.MethodImpl;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.lang.generators.GenericGenerator;
import org.jenerate.internal.lang.generators.impl.GeneratorsCommonMethodsDelegateImpl;
import org.jenerate.internal.lang.generators.impl.GenericGeneratorImpl;
import org.jenerate.internal.manage.DialogProviderManager;
import org.jenerate.internal.manage.MethodContentStrategyManager;
import org.jenerate.internal.manage.MethodStrategyManager;
import org.jenerate.internal.manage.impl.DialogProviderManagerImpl;
import org.jenerate.internal.manage.impl.MethodContentStrategyManagerImpl;
import org.jenerate.internal.manage.impl.MethodStrategyManagerImpl;
import org.jenerate.internal.ui.dialogs.JenerateDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.ui.preferences.impl.PreferencesManagerImpl;
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
                MessageDialog.openInformation(parentShell, "MethodSkeleton Generation",
                        "Cursor not in a class, or no class has the same name with the Java file.");
            } else {
                generateCode(commandId, parentShell, objectClass);
            }
        } catch (JavaModelException e) {
            MessageDialog.openError(parentShell, "Error", e.getMessage());
        }
    }

    /**
     * XXX see if the suppress warnings can be removed
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void generateCode(String commandId, Shell parentShell, IType objectClass) {
        boolean useCommonLang3 = ((Boolean) PREFERENCES_MANAGER
                .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
        MethodContentStrategyIdentifier methodContentStrategyIdentifier = MethodContentStrategyIdentifier.USE_COMMONS_LANG;
        if (useCommonLang3) {
            methodContentStrategyIdentifier = MethodContentStrategyIdentifier.USE_COMMONS_LANG3;
        }

        UserActionIdentifier userActionIdentifier = UserActionIdentifier.getUserActionIdentifierFor(commandId);

        MethodStrategyManager methodStrategyManager = new MethodStrategyManagerImpl(PREFERENCES_MANAGER,
                COMMON_METHODS_DELEGATE, JAVA_INTERFACE_CODE_APPENDER);
        MethodContentStrategyManager methodContentStrategyManager = new MethodContentStrategyManagerImpl(
                PREFERENCES_MANAGER, COMMON_METHODS_DELEGATE, JAVA_INTERFACE_CODE_APPENDER);
        DialogProviderManager dialogProviderManager = new DialogProviderManagerImpl(PREFERENCES_MANAGER,
                COMMON_METHODS_DELEGATE, JAVA_INTERFACE_CODE_APPENDER);
        DialogProvider<? extends JenerateDialog<?>, ? extends JenerateDialogData> dialogProvider = dialogProviderManager
                .getDialogProvider(userActionIdentifier);

        Set<MethodSkeleton<? extends JenerateDialogData>> methodSkeletons = methodStrategyManager
                .getMethodSkeletons(userActionIdentifier);

        Set<Method<? extends MethodSkeleton<?>, ? extends JenerateDialogData>> methods = new HashSet<Method<? extends MethodSkeleton<?>, ? extends JenerateDialogData>>();
        for (MethodSkeleton<? extends JenerateDialogData> methodSkeleton : methodSkeletons) {
            MethodContent<? extends MethodSkeleton<?>, ? extends JenerateDialogData> methodContent = methodContentStrategyManager
                    .getStrategy(methodSkeleton, methodContentStrategyIdentifier);
            methods.add(new MethodImpl(methodSkeleton, methodContent));
        }

        GenericGenerator genericGenerator = new GenericGeneratorImpl(dialogProvider, JAVA_UI_CODE_APPENDER,
                CODE_FORMATTER);

        genericGenerator.generate(parentShell, objectClass, methods);
    }
}
