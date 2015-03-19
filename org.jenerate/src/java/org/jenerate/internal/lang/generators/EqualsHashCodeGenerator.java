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
import org.jenerate.internal.util.JavaUtils;

/**
 * XXX test caching field empty for hashCode
 * 
 * @author jiayun, maudrain
 */
public final class EqualsHashCodeGenerator implements ILangGenerator {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final PreferencesManager preferencesManager;
    private final DialogProvider<EqualsHashCodeDialog> dialogProvider;

    public EqualsHashCodeGenerator(JavaUiCodeAppender javaUiCodeAppender, PreferencesManager preferencesManager,
            DialogProvider<EqualsHashCodeDialog> dialogProvider) {
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.preferencesManager = preferencesManager;
        this.dialogProvider = dialogProvider;
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
                fields = JavaUtils.getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass);
            } else {
                fields = JavaUtils.getNonStaticNonCacheFields(objectClass);
            }

            boolean disableAppendSuper = JavaUtils.isDirectSubclassOfObject(objectClass)
                    || !JavaUtils.isEqualsOverriddenInSuperclass(objectClass)
                    || !JavaUtils.isHashCodeOverriddenInSuperclass(objectClass);

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
                boolean compareReferences = dialog.getCompareReferences();
                boolean useGettersInsteadOfFields = dialog.getUseGettersInsteadOfFields();
                boolean useBlocksInIfStatements = dialog.getUseBlockInIfStatements();
                IInitMultNumbers imNumbers = dialog.getInitMultNumbers();

                boolean useCommonLang3 = ((Boolean) preferencesManager
                        .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
                boolean addOverridePreference = ((Boolean) preferencesManager
                        .getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION)).booleanValue();
                IJavaElement created = generateHashCode(parentShell, objectClass, checkedFields, insertPosition,
                        appendSuper, generateComment, imNumbers, useGettersInsteadOfFields, useCommonLang3,
                        addOverridePreference);

                created = generateEquals(parentShell, objectClass, checkedFields, created, appendSuper,
                        generateComment, compareReferences, useGettersInsteadOfFields, useBlocksInIfStatements,
                        useCommonLang3, addOverridePreference);

                javaUiCodeAppender.revealInEditor(objectClass, created);
            }

        } catch (CoreException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e.getMessage());
        }

    }

    private IJavaElement generateEquals(final Shell parentShell, final IType objectClass, final IField[] checkedFields,
            final IJavaElement insertPosition, final boolean appendSuper, final boolean generateComment,
            final boolean compareReferences, final boolean useGettersInsteadOfFields,
            final boolean useBlocksInIfStatements, boolean useCommonLang3, boolean addOverridePreference)
            throws JavaModelException {

        boolean addOverride = addOverridePreference
                && JavaUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass.getJavaProject());

        String source = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields, objectClass,
                appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));

        String formattedContent = JavaUtils.formatCode(parentShell, objectClass, source);

        objectClass.getCompilationUnit().createImport(CommonsLangLibraryUtils.getEqualsBuilderLibrary(useCommonLang3),
                null, null);
        return objectClass.createMethod(formattedContent, insertPosition, true, null);
    }

    private IJavaElement generateHashCode(final Shell parentShell, final IType objectClass,
            final IField[] checkedFields, final IJavaElement insertPosition, final boolean appendSuper,
            final boolean generateComment, final IInitMultNumbers imNumbers, final boolean useGettersInsteadOfFields,
            boolean useCommonLang3, boolean addOverridePreference) throws JavaModelException {

        boolean cacheHashCode = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).booleanValue();
        boolean isCacheable = cacheHashCode && JavaUtils.areAllFinalFields(checkedFields);
        String cachingField = "";
        if (isCacheable) {
            cachingField = (String) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.HASHCODE_CACHING_FIELD);
        }

        boolean addOverride = addOverridePreference
                && JavaUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass.getJavaProject());

        String source = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, cachingField, addOverride, useGettersInsteadOfFields));

        String formattedContent = JavaUtils.formatCode(parentShell, objectClass, source);

        objectClass.getCompilationUnit().createImport(
                CommonsLangLibraryUtils.getHashCodeBuilderLibrary(useCommonLang3), null, null);
        IJavaElement created = objectClass.createMethod(formattedContent, insertPosition, true, null);

        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient int " + cachingField + ";\n\n";
            String formattedFieldSrc = JavaUtils.formatCode(parentShell, objectClass, fieldSrc);
            created = objectClass.createField(formattedFieldSrc, created, true, null);
        }

        return created;
    }
}
