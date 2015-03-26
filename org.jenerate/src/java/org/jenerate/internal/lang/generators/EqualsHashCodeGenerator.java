// $Id$
package org.jenerate.internal.lang.generators;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
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
import org.jenerate.internal.domain.method.impl.EqualsMethod;
import org.jenerate.internal.domain.method.impl.HashCodeMethod;
import org.jenerate.internal.lang.MethodGenerations;
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
            IField[] fields = generatorsCommonMethodsDelegate.getObjectClassFields(objectClass, preferencesManager);

            boolean disableAppendSuper = getDisableAppendSuper(objectClass);

            EqualsHashCodeDialog dialog = dialogProvider.getDialog(parentShell, objectClass, excludedMethods, fields,
                    disableAppendSuper, preferencesManager);
            int returnCode = dialog.open();
            if (returnCode == Window.OK) {

                for (IMethod excludedMethod : excludedMethods) {
                    if (excludedMethod.exists()) {
                        excludedMethod.delete(true, null);
                    }
                }

                generateCode(parentShell, objectClass, dialog.getData());
            }

        } catch (CoreException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e.getMessage());
        }

    }

    private boolean getDisableAppendSuper(IType objectClass) throws JavaModelException {
        return isDirectSubclassOfObject(objectClass) || !isEqualsOverriddenInSuperclass(objectClass)
                || !isHashCodeOverriddenInSuperclass(objectClass);
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

        boolean cacheHashCode = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).booleanValue();
        boolean isCacheable = cacheHashCode
                && generatorsCommonMethodsDelegate.areAllFinalFields(data.getCheckedFields());
        String cachingField = "";
        if (isCacheable) {
            cachingField = (String) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.HASHCODE_CACHING_FIELD);
        }

        IJavaElement currentPosition = data.getElementPosition();
        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient int " + cachingField + ";\n\n";
            objectClass.createField(fieldSrc, currentPosition, true, null);
        }

        String hashCodeMethodContent = MethodGenerations.generateHashCodeMethodContent(data, cachingField);
        HashCodeMethod hashCodeMethod = new HashCodeMethod(preferencesManager, generatorsCommonMethodsDelegate);
        String source = hashCodeMethod.getMethod(objectClass, data, hashCodeMethodContent);

        String formattedContent = format(parentShell, objectClass, source);

        objectClass.getCompilationUnit().createImport(
                CommonsLangLibraryUtils.getHashCodeBuilderLibrary(useCommonLang3), null, null);
        return objectClass.createMethod(formattedContent, currentPosition, true, null);
    }

    private IJavaElement generateEquals(final Shell parentShell, final IType objectClass,
            EqualsHashCodeDialogData data, final IJavaElement insertPosition, boolean useCommonLang3)
            throws JavaModelException {

        String equalsMethodContent = MethodGenerations.generateEqualsMethodContent(data, objectClass);
        EqualsMethod equalsMethod = new EqualsMethod(preferencesManager, generatorsCommonMethodsDelegate);
        String source = equalsMethod.getMethod(objectClass, data, equalsMethodContent);
        String formattedContent = format(parentShell, objectClass, source);
        objectClass.getCompilationUnit().createImport(CommonsLangLibraryUtils.getEqualsBuilderLibrary(useCommonLang3),
                null, null);
        return objectClass.createMethod(formattedContent, insertPosition, true, null);
    }

    private boolean isDirectSubclassOfObject(final IType objectClass) throws JavaModelException {
        String superclass = objectClass.getSuperclassName();

        if (superclass == null) {
            return true;
        }
        if (superclass.equals("Object") || superclass.equals("java.lang.Object")) {
            return true;
        }

        return false;
    }

    private String format(final Shell parentShell, final IType objectClass, String source) throws JavaModelException {
        try {
            return jeneratePluginCodeFormatter.formatCode(objectClass, source);
        } catch (BadLocationException e) {
            MessageDialog.openError(parentShell, "Error", e.getMessage());
            return "";
        }
    }

    public boolean isHashCodeOverriddenInSuperclass(final IType objectClass) throws JavaModelException {
        return generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "hashCode", new String[0],
                "java.lang.Object");
    }

    public boolean isEqualsOverriddenInSuperclass(final IType objectClass) throws JavaModelException {
        return generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "equals",
                new String[] { "QObject;" }, "java.lang.Object");
    }
}
