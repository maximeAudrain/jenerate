// $Id$
package org.jenerate.internal.lang.generators;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.generate.method.util.JavaCodeFormatter;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringMethodContent;
import org.jenerate.internal.strategy.method.skeleton.impl.ToStringMethodSkeleton;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.impl.ToStringDialog;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;

/**
 * XXX test caching field empty for toString
 * 
 * @author jiayun, maudrain
 */
public final class ToStringGenerator implements ILangGenerator {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final PreferencesManager preferencesManager;
    private final DialogFactory<ToStringDialog, ToStringGenerationData> dialogProvider;
    private final JavaCodeFormatter jeneratePluginCodeFormatter;
    private final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public ToStringGenerator(JavaUiCodeAppender javaUiCodeAppender, PreferencesManager preferencesManager,
            DialogFactory<ToStringDialog, ToStringGenerationData> dialogProvider,
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

            ToStringDialog dialog = dialogProvider.createDialog(parentShell, objectClass, excludedMethods);
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

        IMethod existingMethod = objectClass.getMethod("toString", new String[0]);

        if (existingMethod.exists()) {
            excludedMethods.add(existingMethod);
        }
        return excludedMethods;
    }

    private void generateCode(final Shell parentShell, final IType objectClass, ToStringGenerationData data)
            throws Exception {

        IJavaElement currentPosition = data.getElementPosition();
        boolean useCommonLang3 = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
        CommonsLangToStringMethodContent toStringMethodContent = null;
        // new CommonsLangToStringMethodContent(
        // preferencesManager, generatorsCommonMethodsDelegate, useCommonLang3);
        String methodContent = toStringMethodContent.getMethodContent(objectClass, data);
        ToStringMethodSkeleton toStringMethod = new ToStringMethodSkeleton(preferencesManager,
                generatorsCommonMethodsDelegate);
        String source = toStringMethod.getMethod(objectClass, data, methodContent);
        String formattedContent = format(parentShell, objectClass, source);
        IMethod created = objectClass.createMethod(formattedContent, currentPosition, true, null);

        for (String libraryToImport : toStringMethod.getLibrariesToImport()) {
            objectClass.getCompilationUnit().createImport(libraryToImport, null, null);
        }
        for (String libraryToImport : toStringMethodContent.getLibrariesToImport(data)) {
            objectClass.getCompilationUnit().createImport(libraryToImport, null, null);
        }

        javaUiCodeAppender.revealInEditor(objectClass, created);
    }

    private String format(final Shell parentShell, final IType objectClass, String source) throws JavaModelException {
        try {
            return jeneratePluginCodeFormatter.formatCode(objectClass, source);
        } catch (Exception e) {
            MessageDialog.openError(parentShell, "Error", e.getMessage());
            return "";
        }
    }

}
