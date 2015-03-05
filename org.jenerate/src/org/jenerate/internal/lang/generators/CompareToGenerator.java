//$Id$
package org.jenerate.internal.lang.generators;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.ui.dialogs.OrderableFieldDialog;
import org.jenerate.internal.util.JavaUtils;
import org.jenerate.internal.util.PreferenceUtils;

/**
 * @author jiayun
 */
public final class CompareToGenerator implements ILangGenerator {

    private static final ILangGenerator instance = new CompareToGenerator();

    private CompareToGenerator() {
    }

    public static ILangGenerator getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jiayun.commons4e.internal.lang.generators.ILangGenerator#generate(org.eclipse.swt.widgets.Shell,
     *      org.eclipse.jdt.core.IType)
     */
    public void generate(Shell parentShell, IType objectClass) {

        Set excludedMethods = new HashSet();

        IMethod existingMethod = objectClass.getMethod("compareTo",
                new String[] { "QObject;" });

        if (!existingMethod.exists()) {
            existingMethod = objectClass.getMethod("compareTo",
                    new String[] { "Q" + objectClass.getElementName() + ";" });
        }

        if (existingMethod.exists()) {
            excludedMethods.add(existingMethod);
        }

        try {
            IField[] fields;
            if (PreferenceUtils.getDisplayFieldsOfSuperclasses()) {
                fields = JavaUtils
                        .getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass);
            } else {
                fields = JavaUtils.getNonStaticNonCacheFields(objectClass);
            }

            OrderableFieldDialog dialog = new OrderableFieldDialog(parentShell,
                    "Generate CompareTo Method", objectClass, fields,
                    excludedMethods, !(JavaUtils.isImplementedInSupertype(
                            objectClass, "Comparable") && JavaUtils
                            .isHashCodeOverriddenInSuperclass(objectClass)));
            int returnCode = dialog.open();
            if (returnCode == Window.OK) {

                if (existingMethod.exists()) {
                    existingMethod.delete(true, null);
                }

                IField[] checkedFields = dialog.getCheckedFields();
                IJavaElement insertPosition = dialog.getElementPosition();
                boolean appendSuper = dialog.getAppendSuper();
                boolean generateComment = dialog.getGenerateComment();

                generateCompareTo(parentShell, objectClass, checkedFields,
                        insertPosition, appendSuper, generateComment);
            }

        } catch (CoreException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e
                    .getMessage());
        } catch (BadLocationException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e
                    .getMessage());
        }

    }

    private void generateCompareTo(final Shell parentShell,
            final IType objectClass, final IField[] checkedFields,
            final IJavaElement insertPosition, final boolean appendSuper,
            final boolean generateComment) throws PartInitException,
            JavaModelException, MalformedTreeException, BadLocationException {

        ICompilationUnit cu = objectClass.getCompilationUnit();
        IEditorPart javaEditor = JavaUI.openInEditor(cu);

        boolean implementedOrExtendedInSuperType = JavaUtils
                .isImplementedOrExtendedInSupertype(objectClass, "Comparable");
        boolean generify = PreferenceUtils.getGenerifyCompareTo()
                && PreferenceUtils
                        .isSourceLevelGreaterThanOrEqualTo5(objectClass
                                .getJavaProject())
                && !implementedOrExtendedInSuperType;

        if (!implementedOrExtendedInSuperType) {
            try {
                if (generify) {
                    JavaUtils.addSuperInterface(objectClass, "Comparable<"
                            + objectClass.getElementName() + ">");
                } else {
                    JavaUtils.addSuperInterface(objectClass, "Comparable");
                }
            } catch (InvalidInputException e) {
                MessageDialog.openError(parentShell, "Error",
                        "Failed to add Comparable to implements clause:\n"
                                + e.getMessage());
            }
        }

        String source = createMethod(objectClass, checkedFields, appendSuper,
                generateComment, generify);

        String formattedContent = JavaUtils.formatCode(parentShell,
                objectClass, source);

        objectClass.getCompilationUnit().createImport(
                CommonsLangLibraryUtils.getCompareToBuilderLibrary(), null, null);
        IMethod created = objectClass.createMethod(formattedContent,
                insertPosition, true, null);

        JavaUI.revealInEditor(javaEditor, (IJavaElement) created);
    }

    private String createMethod(final IType objectClass,
            final IField[] checkedFields, final boolean appendSuper,
            final boolean generateComment, final boolean generify) {

        StringBuffer content = new StringBuffer();
        if (generateComment) {
            content.append("/* (non-Javadoc)\n");
            content
                    .append(" * @see java.lang.Comparable#compareTo(java.lang.Object)\n");
            content.append(" */\n");
        }
        String other;
        if (generify) {
            content.append("public int compareTo(final "
                    + objectClass.getElementName() + " other) {\n");

            other = "other";
        } else {
            content.append("public int compareTo(final Object other) {\n");
            content.append(objectClass.getElementName());
            content.append(" castOther = (");
            content.append(objectClass.getElementName());
            content.append(") other;\n");

            other = "castOther";
        }
        content.append("return new CompareToBuilder()");
        if (appendSuper) {
            content.append(".appendSuper(super.compareTo(other))");
        }
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(checkedFields[i].getElementName());
            content.append(", " + other + ".");
            content.append(checkedFields[i].getElementName());
            content.append(")");
        }
        content.append(".toComparison();\n");
        content.append("}\n\n");

        return content.toString();
    }

}
