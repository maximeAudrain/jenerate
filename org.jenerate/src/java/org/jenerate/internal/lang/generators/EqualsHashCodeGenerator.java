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
import org.jenerate.internal.data.impl.EqualsMethodGenerationData;
import org.jenerate.internal.data.impl.HashCodeMethodGenerationData;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.ui.dialogs.EqualsHashCodeDialog;
import org.jenerate.internal.util.GenerationUtils;
import org.jenerate.internal.util.JavaUtils;
import org.jenerate.internal.util.PreferenceUtils;

/**
 * XXX test caching field empty for hashCode
 * 
 * @author jiayun
 */
public final class EqualsHashCodeGenerator implements ILangGenerator {

    private static final ILangGenerator instance = new EqualsHashCodeGenerator();

    private EqualsHashCodeGenerator() {
    }

    public static ILangGenerator getInstance() {
        return instance;
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
            if (PreferenceUtils.getDisplayFieldsOfSuperclasses()) {
                fields = JavaUtils.getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass);
            } else {
                fields = JavaUtils.getNonStaticNonCacheFields(objectClass);
            }

            boolean disableAppendSuper = JavaUtils.isDirectSubclassOfObject(objectClass)
                    || !JavaUtils.isEqualsOverriddenInSuperclass(objectClass)
                    || !JavaUtils.isHashCodeOverriddenInSuperclass(objectClass);

            EqualsHashCodeDialog dialog = new EqualsHashCodeDialog(parentShell, "Generate Equals and HashCode",
                    objectClass, fields, excludedMethods, disableAppendSuper);
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

                IJavaElement created = generateHashCode(parentShell, objectClass, checkedFields, insertPosition,
                        appendSuper, generateComment, imNumbers, useGettersInsteadOfFields);

                created = generateEquals(parentShell, objectClass, checkedFields, created, appendSuper,
                        generateComment, compareReferences, useGettersInsteadOfFields, useBlocksInIfStatements);

                GenerationUtils.revealInEditor(objectClass, created);
            }

        } catch (CoreException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e.getMessage());
        }

    }

    private IJavaElement generateEquals(final Shell parentShell, final IType objectClass, final IField[] checkedFields,
            final IJavaElement insertPosition, final boolean appendSuper, final boolean generateComment,
            final boolean compareReferences, final boolean useGettersInsteadOfFields,
            final boolean useBlocksInIfStatements) throws JavaModelException {

        boolean addOverride = PreferenceUtils.getAddOverride()
                && PreferenceUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass.getJavaProject());

        String source = MethodGenerations.createEqualsMethod(new EqualsMethodGenerationData(checkedFields, objectClass,
                appendSuper, generateComment, compareReferences, addOverride, useGettersInsteadOfFields,
                useBlocksInIfStatements));

        String formattedContent = JavaUtils.formatCode(parentShell, objectClass, source);

        objectClass.getCompilationUnit().createImport(CommonsLangLibraryUtils.getEqualsBuilderLibrary(), null, null);
        return objectClass.createMethod(formattedContent, insertPosition, true, null);
    }

    private IJavaElement generateHashCode(final Shell parentShell, final IType objectClass,
            final IField[] checkedFields, final IJavaElement insertPosition, final boolean appendSuper,
            final boolean generateComment, final IInitMultNumbers imNumbers, final boolean useGettersInsteadOfFields)
            throws JavaModelException {

        boolean isCacheable = PreferenceUtils.getCacheHashCode() && JavaUtils.areAllFinalFields(checkedFields);
        String cachingField = "";
        if (isCacheable) {
            cachingField = PreferenceUtils.getHashCodeCachingField();
        }

        boolean addOverride = PreferenceUtils.getAddOverride()
                && PreferenceUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass.getJavaProject());

        String source = MethodGenerations.createHashCodeMethod(new HashCodeMethodGenerationData(checkedFields,
                appendSuper, generateComment, imNumbers, cachingField, addOverride, useGettersInsteadOfFields));

        String formattedContent = JavaUtils.formatCode(parentShell, objectClass, source);

        objectClass.getCompilationUnit().createImport(CommonsLangLibraryUtils.getHashCodeBuilderLibrary(), null, null);
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
