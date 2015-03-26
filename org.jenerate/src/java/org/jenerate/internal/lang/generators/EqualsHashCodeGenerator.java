// $Id$
package org.jenerate.internal.lang.generators;

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
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.domain.method.content.equals.CommonsLangEqualsMethodContent;
import org.jenerate.internal.domain.method.content.hashcode.CommonsLangHashCodeMethodContent;
import org.jenerate.internal.domain.method.skeleton.impl.EqualsMethod;
import org.jenerate.internal.domain.method.skeleton.impl.HashCodeMethod;
import org.jenerate.internal.ui.dialogs.EqualsHashCodeDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;

/**
 * XXX test caching field empty for hashCode
 * 
 * @author jiayun, maudrain
 */
public final class EqualsHashCodeGenerator implements ILangGenerator {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final PreferencesManager preferencesManager;
    private final DialogProvider<EqualsHashCodeDialog, EqualsHashCodeDialogData> dialogProvider;
    private final JeneratePluginCodeFormatter jeneratePluginCodeFormatter;
    private final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public EqualsHashCodeGenerator(JavaUiCodeAppender javaUiCodeAppender, PreferencesManager preferencesManager,
            DialogProvider<EqualsHashCodeDialog, EqualsHashCodeDialogData> dialogProvider,
            JeneratePluginCodeFormatter jeneratePluginCodeFormatter,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.preferencesManager = preferencesManager;
        this.dialogProvider = dialogProvider;
        this.jeneratePluginCodeFormatter = jeneratePluginCodeFormatter;
        this.generatorsCommonMethodsDelegate = generatorsCommonMethodsDelegate;
    }

    @Override
    public void generate(Shell parentShell, IType objectClass) {
        Set<IMethod> excludedMethods = getExcludedMethods(objectClass);
        try {

            EqualsHashCodeDialog dialog = dialogProvider.getDialog(parentShell, objectClass, excludedMethods);
            int returnCode = dialog.open();
            if (returnCode == Window.OK) {

                for (IMethod excludedMethod : excludedMethods) {
                    if (excludedMethod.exists()) {
                        excludedMethod.delete(true, null);
                    }
                }

                generateCode(parentShell, objectClass, dialog.getData());
            }

        } catch (Exception e) {
            MessageDialog.openError(parentShell, "MethodSkeleton Generation Failed", e.getMessage());
        }

    }

    private Set<IMethod> getExcludedMethods(IType objectClass) {
        Set<IMethod> excludedMethods = new HashSet<>();

        IMethod existingEquals = objectClass.getMethod("equals", new String[] { "QObject;" });
        IMethod existingHashCode = objectClass.getMethod("hashCode", new String[0]);
        if (existingEquals.exists()) {
            excludedMethods.add(existingEquals);
        }
        if (existingHashCode.exists()) {
            excludedMethods.add(existingHashCode);
        }
        return excludedMethods;
    }

    private void generateCode(Shell parentShell, IType objectClass, EqualsHashCodeDialogData data)
            throws JavaModelException, PartInitException {
        boolean useCommonLang3 = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
        IJavaElement created = generateHashCode(parentShell, objectClass, data, useCommonLang3);

        created = generateEquals(parentShell, objectClass, data, created, useCommonLang3);

        javaUiCodeAppender.revealInEditor(objectClass, created);
    }

    private IJavaElement generateHashCode(final Shell parentShell, final IType objectClass,
            EqualsHashCodeDialogData data, boolean useCommonLang3) throws JavaModelException {

        IJavaElement currentPosition = data.getElementPosition();
        CommonsLangHashCodeMethodContent hashCodeMethodContent = null;
//                new CommonsLangHashCodeMethodContent(
//                preferencesManager, generatorsCommonMethodsDelegate, useCommonLang3);
        String methodContent = hashCodeMethodContent.getMethodContent(objectClass, data);
        HashCodeMethod hashCodeMethod = new HashCodeMethod(preferencesManager, generatorsCommonMethodsDelegate);
        String source = hashCodeMethod.getMethod(objectClass, data, methodContent);

        for (String libraryToImport : hashCodeMethod.getLibrariesToImport()) {
            objectClass.getCompilationUnit().createImport(libraryToImport, null, null);
        }
        for (String libraryToImport : hashCodeMethodContent.getLibrariesToImport(data)) {
            objectClass.getCompilationUnit().createImport(libraryToImport, null, null);
        }

        String formattedContent = format(parentShell, objectClass, source);
        return objectClass.createMethod(formattedContent, currentPosition, true, null);
    }

    private IJavaElement generateEquals(final Shell parentShell, final IType objectClass,
            EqualsHashCodeDialogData data, final IJavaElement insertPosition, boolean useCommonLang3)
            throws JavaModelException {

        CommonsLangEqualsMethodContent equalsMethodContent = null;
//                new CommonsLangEqualsMethodContent(preferencesManager,
//                generatorsCommonMethodsDelegate, useCommonLang3);
        String methodContent = equalsMethodContent.getMethodContent(objectClass, data);
        EqualsMethod equalsMethod = new EqualsMethod(preferencesManager, generatorsCommonMethodsDelegate);
        String source = equalsMethod.getMethod(objectClass, data, methodContent);

        for (String libraryToImport : equalsMethod.getLibrariesToImport()) {
            objectClass.getCompilationUnit().createImport(libraryToImport, null, null);
        }
        for (String libraryToImport : equalsMethodContent.getLibrariesToImport(data)) {
            objectClass.getCompilationUnit().createImport(libraryToImport, null, null);
        }

        String formattedContent = format(parentShell, objectClass, source);
        return objectClass.createMethod(formattedContent, insertPosition, true, null);
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
