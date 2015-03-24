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
import org.jenerate.internal.data.IInitMultNumbers;
import org.jenerate.internal.data.impl.EqualsMethodGenerationData;
import org.jenerate.internal.data.impl.HashCodeMethodGenerationData;
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
    private final DialogProvider<EqualsHashCodeDialog> dialogProvider;
    private final JeneratePluginCodeFormatter jeneratePluginCodeFormatter;
    private final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    public EqualsHashCodeGenerator(JavaUiCodeAppender javaUiCodeAppender, PreferencesManager preferencesManager,
            DialogProvider<EqualsHashCodeDialog> dialogProvider,
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

        Set<IMethod> excludedMethods = new HashSet<>();

        IMethod existingEquals = objectClass.getMethod("equals", new String[] { "QObject;" });
        IMethod existingHashCode = objectClass.getMethod("hashCode", new String[0]);
        if (existingEquals.exists()) {
            excludedMethods.add(existingEquals);
        }
        if (existingHashCode.exists()) {
            excludedMethods.add(existingHashCode);
        }
        try {
            IField[] fields;
            boolean displayFieldsOfSuperClasses = ((Boolean) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.DISPLAY_FIELDS_OF_SUPERCLASSES)).booleanValue();
            if (displayFieldsOfSuperClasses) {
                fields = generatorsCommonMethodsDelegate
                        .getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass,
                                preferencesManager);
            } else {
                fields = generatorsCommonMethodsDelegate.getNonStaticNonCacheFields(objectClass, preferencesManager);
            }

            boolean disableAppendSuper = isDirectSubclassOfObject(objectClass)
                    || !isEqualsOverriddenInSuperclass(objectClass) || !isHashCodeOverriddenInSuperclass(objectClass);

            EqualsHashCodeDialog dialog = dialogProvider.getDialog(parentShell, objectClass, excludedMethods, fields,
                    disableAppendSuper, preferencesManager);
            int returnCode = dialog.open();
            if (returnCode == Window.OK) {

                if (existingEquals.exists()) {
                    existingEquals.delete(true, null);
                }
                if (existingHashCode.exists()) {
                    existingHashCode.delete(true, null);
                }

                IField[] checkedFields = dialog.getCheckedFields();
                IJavaElement insertPosition = dialog.getElementPosition();
                boolean appendSuper = dialog.getAppendSuper();
                boolean generateComment = dialog.getGenerateComment();
                boolean useGettersInsteadOfFields = dialog.getUseGettersInsteadOfFields();
                boolean compareReferences = dialog.getCompareReferences();
                boolean useBlocksInIfStatements = dialog.getUseBlockInIfStatements();
                IInitMultNumbers imNumbers = dialog.getInitMultNumbers();

                boolean useCommonLang3 = ((Boolean) preferencesManager
                        .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
                boolean addOverridePreference = ((Boolean) preferencesManager
                        .getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION)).booleanValue();
                boolean addOverride = addOverridePreference
                        && generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass);
                IJavaElement created = generateHashCode(parentShell, objectClass, checkedFields, insertPosition,
                        appendSuper, generateComment, imNumbers, useGettersInsteadOfFields, useCommonLang3,
                        addOverride);

                created = generateEquals(parentShell, objectClass, checkedFields, created, appendSuper,
                        generateComment, compareReferences, useGettersInsteadOfFields, useBlocksInIfStatements,
                        useCommonLang3, addOverride);

                javaUiCodeAppender.revealInEditor(objectClass, created);
            }

        } catch (CoreException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e.getMessage());
        }

    }

    private IJavaElement generateHashCode(final Shell parentShell, final IType objectClass,
            final IField[] checkedFields, final IJavaElement insertPosition, final boolean appendSuper,
            final boolean generateComment, final IInitMultNumbers imNumbers, final boolean useGettersInsteadOfFields,
            boolean useCommonLang3, boolean addOverride) throws JavaModelException {

        boolean cacheHashCode = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).booleanValue();
        boolean isCacheable = cacheHashCode && generatorsCommonMethodsDelegate.areAllFinalFields(checkedFields);
        String cachingField = "";
        if (isCacheable) {
            cachingField = (String) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.HASHCODE_CACHING_FIELD);
        }

        String source = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, cachingField, addOverride, useGettersInsteadOfFields));

        String formattedContent = format(parentShell, objectClass, source);

        objectClass.getCompilationUnit().createImport(
                CommonsLangLibraryUtils.getHashCodeBuilderLibrary(useCommonLang3), null, null);
        IJavaElement created = objectClass.createMethod(formattedContent, insertPosition, true, null);

        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient int " + cachingField + ";\n\n";
            String formattedFieldSrc = format(parentShell, objectClass, fieldSrc);
            created = objectClass.createField(formattedFieldSrc, created, true, null);
        }

        return created;
    }
    
    private IJavaElement generateEquals(final Shell parentShell, final IType objectClass, final IField[] checkedFields,
            final IJavaElement insertPosition, final boolean appendSuper, final boolean generateComment,
            final boolean compareReferences, final boolean useGettersInsteadOfFields,
            final boolean useBlocksInIfStatements, boolean useCommonLang3, boolean addOverride)
            throws JavaModelException {

        String source = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields, objectClass,
                appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));
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
        if (superclass.equals("Object")) {
            return true;
        }
        if (superclass.equals("java.lang.Object")) {
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
