// $Id$
package org.jenerate.internal.lang.generators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
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
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JavaUtils;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;

/**
 * @author jiayun, maudrain
 */
public final class CompareToGenerator implements ILangGenerator {

    private final JavaUiCodeAppender javaUiCodeAppender;
    private final PreferencesManager preferencesManager;
    private final DialogProvider<OrderableFieldDialog> dialogProvider;
    private final JeneratePluginCodeFormatter jeneratePluginCodeFormatter;

    public CompareToGenerator(JavaUiCodeAppender javaUiCodeAppender, PreferencesManager preferencesManager,
            DialogProvider<OrderableFieldDialog> dialogProvider, JeneratePluginCodeFormatter jeneratePluginCodeFormatter) {
        this.javaUiCodeAppender = javaUiCodeAppender;
        this.preferencesManager = preferencesManager;
        this.dialogProvider = dialogProvider;
        this.jeneratePluginCodeFormatter = jeneratePluginCodeFormatter;
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
                fields = JavaUtils.getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass,
                        preferencesManager);
            } else {
                fields = JavaUtils.getNonStaticNonCacheFields(objectClass, preferencesManager);
            }

            boolean disableAppendSuper = !(isImplementedInSupertype(objectClass, "Comparable") && JavaUtils
                    .isCompareToImplementedInSuperclass(objectClass));
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

        boolean implementedOrExtendedInSuperType = isImplementedOrExtendedInSupertype(objectClass, "Comparable");
        boolean generifyPreference = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).booleanValue();
        boolean generify = generifyPreference
                && JavaUtils.isSourceLevelGreaterThanOrEqualTo5(objectClass.getJavaProject())
                && !implementedOrExtendedInSuperType;

        if (!implementedOrExtendedInSuperType) {
            try {
                if (generify) {
                    addSuperInterface(objectClass, "Comparable<" + objectClass.getElementName() + ">");
                } else {
                    addSuperInterface(objectClass, "Comparable");
                }
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

    private void addSuperInterface(final IType objectClass, final String interfaceName) throws JavaModelException,
            InvalidInputException, MalformedTreeException {

        if (isImplementedOrExtendedInSupertype(objectClass, interfaceName))
            return;

        String[] interfaces = objectClass.getSuperInterfaceNames();
        String simpleName = getSimpleInterfaceName(interfaceName);
        boolean foundButTypeParamsNotMatched = false;
        for (int i = 0, size = interfaces.length; i < size; i++) {
            if (interfaces[i].equals(interfaceName)) {
                return;
            }
            if (interfaces[i].startsWith(simpleName)) {
                foundButTypeParamsNotMatched = true;
            }
        }

        ICompilationUnit cu = objectClass.getCompilationUnit();
        IBuffer buffer = cu.getBuffer();
        char[] source = buffer.getCharacters();
        IScanner scanner = ToolFactory.createScanner(false, false, false, false);
        scanner.setSource(source);
        scanner.resetTo(objectClass.getNameRange().getOffset(), source.length - 1);

        if (interfaces.length == 0) {

            while (true) {
                int token = scanner.getNextToken();
                if (token == ITerminalSymbols.TokenNameLBRACE) {

                    buffer.replace(scanner.getCurrentTokenStartPosition(), 0, "implements " + interfaceName + " ");
                    break;
                }
            }

        } else if (foundButTypeParamsNotMatched) {

            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setSource(cu);
            parser.setResolveBindings(true);
            CompilationUnit cuNode = (CompilationUnit) parser.createAST(null);
            TypeDeclaration classNode = (TypeDeclaration) cuNode.findDeclaringNode(objectClass.getKey());
            List ifTypes = classNode.superInterfaceTypes();
            Type targetIf = null;
            for (int i = 0; i < ifTypes.size(); i++) {
                targetIf = (Type) ifTypes.get(i);
                if (targetIf.resolveBinding().getName().startsWith(simpleName)) {
                    break;
                }
            }

            buffer.replace(targetIf.getStartPosition(), targetIf.getLength(), interfaceName);

        } else {

            while (true) {
                int token = scanner.getNextToken();
                if (token == ITerminalSymbols.TokenNameimplements) {

                    buffer.replace(scanner.getCurrentTokenEndPosition() + 1, 0, " " + interfaceName + ",");
                    break;
                }
            }

        }

    }

    private String getSimpleInterfaceName(final String interfaceName) {
        if (interfaceName.indexOf('<') == -1) {
            return interfaceName;
        } else {
            return interfaceName.substring(0, interfaceName.indexOf('<'));
        }
    }

    public boolean isImplementedInSupertype(final IType objectClass, final String interfaceName)
            throws JavaModelException {

        String simpleName = getSimpleInterfaceName(interfaceName);

        ITypeHierarchy typeHierarchy = objectClass.newSupertypeHierarchy(null);
        IType[] interfaces = typeHierarchy.getAllInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getElementName().equals(simpleName)) {
                IType in = interfaces[i];
                IType[] types = typeHierarchy.getImplementingClasses(in);
                for (int j = 0; j < types.length; j++) {
                    if (!types[j].getFullyQualifiedName().equals(objectClass.getFullyQualifiedName())) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    public boolean isImplementedOrExtendedInSupertype(final IType objectClass, final String interfaceName)
            throws JavaModelException {

        if (isImplementedInSupertype(objectClass, interfaceName))
            return true;

        String simpleName = getSimpleInterfaceName(interfaceName);

        ITypeHierarchy typeHierarchy = objectClass.newSupertypeHierarchy(null);
        IType[] interfaces = typeHierarchy.getAllInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getElementName().equals(simpleName)) {
                IType in = interfaces[i];
                IType[] types = typeHierarchy.getExtendingInterfaces(in);
                for (int j = 0; j < types.length; j++) {
                    if (!types[j].getFullyQualifiedName().equals(objectClass.getFullyQualifiedName())) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

}
