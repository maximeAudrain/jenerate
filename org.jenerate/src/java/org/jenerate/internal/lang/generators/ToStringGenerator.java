// $Id$
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
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.ui.dialogs.OrderableFieldDialog;
import org.jenerate.internal.util.JavaUtils;
import org.jenerate.internal.util.PreferenceUtils;

/**
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

        IMethod existingMethod = objectClass.getMethod("toString", new String[0]);
        Set<IMethod> excludedMethods = new HashSet<>();
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

        ICompilationUnit cu = objectClass.getCompilationUnit();
        IEditorPart javaEditor = JavaUI.openInEditor(cu);

        boolean isCacheable = PreferenceUtils.getCacheToString() && JavaUtils.areAllFinalFields(checkedFields);

        boolean addOverride = PreferenceUtils.getAddOverride()
                && PreferenceUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass.getJavaProject());

        String styleConstant = getStyleConstantAndAddImport(style, objectClass);
        String source = createMethod(objectClass, checkedFields, appendSuper, generateComment, styleConstant,
                isCacheable, addOverride, useGettersInsteadOfFields);

        String formattedContent = JavaUtils.formatCode(parentShell, objectClass, source);

        objectClass.getCompilationUnit().createImport(CommonsLangLibraryUtils.getToStringBuilderLibrary(), null, null);
        IMethod created = objectClass.createMethod(formattedContent, insertPosition, true, null);

        String cachingField = PreferenceUtils.getToStringCachingField();
        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient String " + cachingField + ";\n\n";
            String formattedFieldSrc = JavaUtils.formatCode(parentShell, objectClass, fieldSrc);
            objectClass.createField(formattedFieldSrc, created, true, null);
        }

        JavaUI.revealInEditor(javaEditor, (IJavaElement) created);
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

    private String createMethod(final IType objectClass, final IField[] checkedFields, final boolean appendSuper,
            final boolean generateComment, final String styleConstant, final boolean isCacheable,
            final boolean addOverride, final boolean useGettersInsteadOfFields) throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (generateComment) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#toString()\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public String toString() {\n");
        if (isCacheable) {
            String cachingField = PreferenceUtils.getToStringCachingField();
            content.append("if (" + cachingField + "== null) {\n");
            content.append(cachingField + " = ");
            content.append(createBuilderString(checkedFields, appendSuper, styleConstant, useGettersInsteadOfFields));
            content.append("}\n");
            content.append("return " + cachingField + ";\n");

        } else {
            content.append("return ");
            content.append(createBuilderString(checkedFields, appendSuper, styleConstant, useGettersInsteadOfFields));
        }
        content.append("}\n\n");

        return content.toString();
    }

    private String createBuilderString(final IField[] checkedFields, final boolean appendSuper,
            final String styleConstant, final boolean useGettersInsteadOfFields) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        if (styleConstant == null) {
            content.append("new ToStringBuilder(this)");
        } else {
            content.append("new ToStringBuilder(this, ");
            content.append(styleConstant);
            content.append(")");
        }
        if (appendSuper) {
            content.append(".appendSuper(super.toString())");
        }
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(\"");
            content.append(checkedFields[i].getElementName());
            content.append("\", ");
            content.append(JavaUtils.generateFieldAccessor(checkedFields[i], useGettersInsteadOfFields));
            content.append(")");
        }
        content.append(".toString();\n");

        return content.toString();
    }

    private static class ToStringDialog extends OrderableFieldDialog {

        private Combo styleCombo;

        private String toStringStyle;

        private IDialogSettings settings;

        private static final String SETTINGS_SECTION = "ToStringDialog";

        private static final String SETTINGS_STYLE = "ToStringStyle";

        public ToStringDialog(final Shell parentShell, final String dialogTitle, final IType objectClass,
                final IField[] fields, final Set<IMethod> excludedMethods, final boolean disableAppendSuper)
                throws JavaModelException {

            super(parentShell, dialogTitle, objectClass, fields, excludedMethods, disableAppendSuper);

            IDialogSettings dialogSettings = JeneratePlugin.getDefault().getDialogSettings();
            settings = dialogSettings.getSection(SETTINGS_SECTION);
            if (settings == null) {
                settings = dialogSettings.addNewSection(SETTINGS_SECTION);
            }

            toStringStyle = settings.get(SETTINGS_STYLE);
            if (toStringStyle == null) {
                toStringStyle = CommonsLangLibraryUtils.getToStringStyleLibraryDefaultStyle();
            } else {
                String[] splittedToStringStyle = toStringStyle.split("\\.");
                String chosenStyle = splittedToStringStyle[splittedToStringStyle.length - 1];
                toStringStyle = CommonsLangLibraryUtils.getToStringStyleLibrary() + CommonsLangLibraryUtils.DOT_STRING
                        + chosenStyle;
            }
        }

        @Override
        public boolean close() {
            toStringStyle = styleCombo.getText();
            settings.put(SETTINGS_STYLE, toStringStyle);
            return super.close();
        }

        @Override
        protected Composite createOptionComposite(Composite composite) {
            Composite optionComposite = super.createOptionComposite(composite);
            addStyleChoices(optionComposite);
            return optionComposite;
        }

        private Composite addStyleChoices(final Composite composite) {
            Label label = new Label(composite, SWT.NONE);
            label.setText("&ToString style:");

            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
            label.setLayoutData(data);

            styleCombo = new Combo(composite, SWT.NONE);
            styleCombo.setItems(CommonsLangLibraryUtils.createToStringStyles());
            styleCombo.setText(toStringStyle);

            data = new GridData(GridData.FILL_HORIZONTAL);
            styleCombo.setLayoutData(data);

            return composite;
        }

        public String getToStringStyle() {
            return toStringStyle;
        }
    }

}
