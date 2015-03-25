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
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.data.impl.CompareToMethodGenerationData;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.ui.dialogs.OrderableFieldDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;

/**
 * @author jiayun, maudrain
 */
public final class CompareToGenerator implements ILangGenerator {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final PreferencesManager preferencesManager;
    private final DialogProvider<OrderableFieldDialog> dialogProvider;
    private final JeneratePluginCodeFormatter jeneratePluginCodeFormatter;
    private final GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;
    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CompareToGenerator(JavaUiCodeAppender javaUiCodeAppender, PreferencesManager preferencesManager,
            DialogProvider<OrderableFieldDialog> dialogProvider,
            JeneratePluginCodeFormatter jeneratePluginCodeFormatter,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.preferencesManager = preferencesManager;
        this.dialogProvider = dialogProvider;
        this.jeneratePluginCodeFormatter = jeneratePluginCodeFormatter;
        this.generatorsCommonMethodsDelegate = generatorsCommonMethodsDelegate;
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public void generate(Shell parentShell, IType objectClass) {

        Set<IMethod> excludedMethods = new HashSet<>();

        IMethod existingMethod = objectClass.getMethod("compareTo", new String[] { "QObject;" });

        if (!existingMethod.exists()) {
            existingMethod = objectClass.getMethod("compareTo",
                    new String[] { "Q" + objectClass.getElementName() + ";" });
        }

        if (existingMethod.exists()) {
            excludedMethods.add(existingMethod);
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

            boolean disableAppendSuper = !(javaInterfaceCodeAppender
                    .isImplementedInSupertype(objectClass, "Comparable") && isCompareToImplementedInSuperclass(objectClass));
            OrderableFieldDialog dialog = dialogProvider.getDialog(parentShell, objectClass, excludedMethods, fields,
                    disableAppendSuper, preferencesManager);
            int returnCode = dialog.open();
            if (returnCode == Window.OK) {

                if (existingMethod.exists()) {
                    existingMethod.delete(true, null);
                }

                IField[] checkedFields = dialog.getCheckedFields();
                IJavaElement insertPosition = dialog.getElementPosition();
                boolean appendSuper = dialog.getAppendSuper();
                boolean generateComment = dialog.getGenerateComment();

                generateCompareTo(parentShell, objectClass, checkedFields, insertPosition, appendSuper, generateComment);
            }

        } catch (CoreException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e.getMessage());
        }

    }

    private void generateCompareTo(final Shell parentShell, final IType objectClass, final IField[] checkedFields,
            final IJavaElement insertPosition, final boolean appendSuper, final boolean generateComment)
            throws PartInitException, JavaModelException, MalformedTreeException {

        boolean implementedOrExtendedInSuperType = javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(
                objectClass, "Comparable");
        boolean generifyPreference = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).booleanValue();
        boolean generify = generifyPreference
                && generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)
                && !implementedOrExtendedInSuperType;

        if (!implementedOrExtendedInSuperType) {
            try {
                String interfaceName = "Comparable";
                if (generify) {
                    interfaceName = "Comparable<" + objectClass.getElementName() + ">";
                }
                javaInterfaceCodeAppender.addSuperInterface(objectClass, interfaceName);
            } catch (InvalidInputException e) {
                MessageDialog.openError(parentShell, "Error",
                        "Failed to add Comparable to implements clause:\n" + e.getMessage());
            }
        }

        String source = MethodGenerations.createCompareToMethod(new CompareToMethodGenerationData(checkedFields,
                objectClass, appendSuper, generateComment, generify));

        String formattedContent = format(parentShell, objectClass, source);

        boolean useCommonLang3 = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).booleanValue();
        objectClass.getCompilationUnit().createImport(
                CommonsLangLibraryUtils.getCompareToBuilderLibrary(useCommonLang3), null, null);
        IMethod created = objectClass.createMethod(formattedContent, insertPosition, true, null);

        javaUiCodeAppender.revealInEditor(objectClass, created);
    }

    private String format(final Shell parentShell, final IType objectClass, String source) throws JavaModelException {
        try {
            return jeneratePluginCodeFormatter.formatCode(objectClass, source);
        } catch (BadLocationException e) {
            MessageDialog.openError(parentShell, "Error", e.getMessage());
            return "";
        }
    }

    public boolean isCompareToImplementedInSuperclass(final IType objectClass) throws JavaModelException {
        return generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "compareTo",
                new String[] { "QObject;" }, null);
    }

}
