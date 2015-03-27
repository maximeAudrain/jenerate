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
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.generate.method.util.JavaCodeFormatter;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangEqualsMethodContent;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangHashCodeMethodContent;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.impl.EqualsHashCodeDialog;

/**
 * XXX test caching field empty for hashCode
 * 
 * @author jiayun, maudrain
 */
public final class EqualsHashCodeGenerator implements ILangGenerator {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final PreferencesManager preferencesManager;
    private final DialogFactory<EqualsHashCodeDialog, EqualsHashCodeGenerationData> dialogProvider;
    private final JavaCodeFormatter jeneratePluginCodeFormatter;
    private final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public EqualsHashCodeGenerator(JavaUiCodeAppender javaUiCodeAppender, PreferencesManager preferencesManager,
            DialogFactory<EqualsHashCodeDialog, EqualsHashCodeGenerationData> dialogProvider,
            JavaCodeFormatter jeneratePluginCodeFormatter,
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

            EqualsHashCodeDialog dialog = dialogProvider.createDialog(parentShell, objectClass, excludedMethods);
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

    private void generateCode(Shell parentShell, IType objectClass, EqualsHashCodeGenerationData data)
            throws JavaModelException, PartInitException {
        boolean useCommonLang3 = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
        IJavaElement created = generateHashCode(parentShell, objectClass, data, useCommonLang3);

        created = generateEquals(parentShell, objectClass, data, created, useCommonLang3);

        javaUiCodeAppender.revealInEditor(objectClass, created);
    }

    private IJavaElement generateHashCode(final Shell parentShell, final IType objectClass,
            EqualsHashCodeGenerationData data, boolean useCommonLang3) throws JavaModelException {

        IJavaElement currentPosition = data.getElementPosition();
        CommonsLangHashCodeMethodContent hashCodeMethodContent = null;
        // new CommonsLangHashCodeMethodContent(
        // preferencesManager, generatorsCommonMethodsDelegate, useCommonLang3);
        String methodContent = hashCodeMethodContent.getMethodContent(objectClass, data);
        HashCodeMethodSkeleton hashCodeMethod = new HashCodeMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate);
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
            EqualsHashCodeGenerationData data, final IJavaElement insertPosition, boolean useCommonLang3)
            throws JavaModelException {

        CommonsLangEqualsMethodContent equalsMethodContent = null;
        // new CommonsLangEqualsMethodContent(preferencesManager,
        // generatorsCommonMethodsDelegate, useCommonLang3);
        String methodContent = equalsMethodContent.getMethodContent(objectClass, data);
        EqualsMethodSkeleton equalsMethod = new EqualsMethodSkeleton(preferencesManager, generatorsCommonMethodsDelegate);
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
