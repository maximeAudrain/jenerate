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
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.data.impl.ToStringMethodGenerationData;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.ui.dialogs.ToStringDialog;
import org.jenerate.internal.util.GenerationUtils;
import org.jenerate.internal.util.JavaUtils;
import org.jenerate.internal.util.PreferenceUtils;

/**
 * XXX test caching field empty for toString
 * 
 * @author jiayun
 */
public final class ToStringGenerator implements ILangGenerator {

    private static final ILangGenerator instance = new ToStringGenerator();

    private ToStringGenerator() {
    }

    public static ILangGenerator getInstance() {
        return instance;
    }

    @Override
    public void generate(Shell parentShell, IType objectClass) {

        Set<IMethod> excludedMethods = new HashSet<>();

        IMethod existingMethod = objectClass.getMethod("toString", new String[0]);

        if (existingMethod.exists()) {
            excludedMethods.add(existingMethod);
        }
        try {
            IField[] fields;
            if (PreferenceUtils.getDisplayFieldsOfSuperclasses()) {
                fields = JavaUtils.getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass);
            } else {
                fields = JavaUtils.getNonStaticNonCacheFields(objectClass);
            }

            ToStringDialog dialog = new ToStringDialog(parentShell, "Generate ToString Method", objectClass, fields,
                    excludedMethods, !JavaUtils.isToStringConcreteInSuperclass(objectClass));
            int returnCode = dialog.open();
            if (returnCode == Window.OK) {

                if (existingMethod.exists()) {
                    existingMethod.delete(true, null);
                }

                IField[] checkedFields = dialog.getCheckedFields();
                IJavaElement insertPosition = dialog.getElementPosition();
                boolean appendSuper = dialog.getAppendSuper();
                boolean generateComment = dialog.getGenerateComment();
                boolean useGettersInsteadOfFields = dialog.getUseGettersInsteadOfFields();
                String style = dialog.getToStringStyle();

                generateToString(parentShell, objectClass, checkedFields, insertPosition, appendSuper, generateComment,
                        style, useGettersInsteadOfFields);
            }

        } catch (CoreException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e.getMessage());
        }

    }

    private void generateToString(final Shell parentShell, final IType objectClass, final IField[] checkedFields,
            final IJavaElement insertPosition, final boolean appendSuper, final boolean generateComment,
            final String style, final boolean useGettersInsteadOfFields) throws PartInitException, JavaModelException {

        boolean isCacheable = PreferenceUtils.getCacheToString() && JavaUtils.areAllFinalFields(checkedFields);
        String cachingField = "";
        if (isCacheable) {
            cachingField = PreferenceUtils.getToStringCachingField();
        }

        boolean addOverride = PreferenceUtils.getAddOverride()
                && PreferenceUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass.getJavaProject());

        String styleConstant = getStyleConstantAndAddImport(style, objectClass);
        String source = MethodGenerations.createToStringMethod(new ToStringMethodGenerationData(checkedFields, appendSuper, generateComment, styleConstant, cachingField, addOverride, useGettersInsteadOfFields));

        String formattedContent = JavaUtils.formatCode(parentShell, objectClass, source);

        objectClass.getCompilationUnit().createImport(CommonsLangLibraryUtils.getToStringBuilderLibrary(), null, null);
        IMethod created = objectClass.createMethod(formattedContent, insertPosition, true, null);

        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient String " + cachingField + ";\n\n";
            String formattedFieldSrc = JavaUtils.formatCode(parentShell, objectClass, fieldSrc);
            objectClass.createField(formattedFieldSrc, created, true, null);
        }

        GenerationUtils.revealInEditor(objectClass, created);
    }

    private String getStyleConstantAndAddImport(final String style, final IType objectClass) throws JavaModelException {

        String styleConstant = null;
        if (!style.equals(CommonsLangLibraryUtils.getToStringStyleLibraryDefaultStyle()) && !style.equals("")) {

            int lastDot = style.lastIndexOf('.');
            if (lastDot != -1 && lastDot != (style.length() - 1)) {

                String styleClass = style.substring(0, lastDot);
                if (styleClass.length() == 0) {
                    return null;
                }

                int lastDot2 = styleClass.lastIndexOf('.');
                if (lastDot2 != (styleClass.length() - 1)) {

                    styleConstant = style.substring(lastDot2 + 1, style.length());
                    if (lastDot2 != -1) {
                        objectClass.getCompilationUnit().createImport(styleClass, null, null);
                    }
                }
            }
        }
        return styleConstant;
    }

}
