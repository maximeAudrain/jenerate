package org.jenerate.internal.util.impl;

import java.util.List;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
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
import org.eclipse.text.edits.MalformedTreeException;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

/**
 * Implementation of the {@link JavaInterfaceCodeAppender}. XXX Test me
 * 
 * @author maudrain
 */
public final class JavaInterfaceCodeAppenderImpl implements JavaInterfaceCodeAppender {

    @Override
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

    @Override
    public boolean isImplementedOrExtendedInSupertype(final IType objectClass, final String interfaceName)
            throws JavaModelException {

        if (isImplementedInSupertype(objectClass, interfaceName)) {
            return true;
        }

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

    @Override
    public void addSuperInterface(final IType objectClass, final String interfaceName) throws JavaModelException,
            InvalidInputException, MalformedTreeException {

        if (isImplementedOrExtendedInSupertype(objectClass, interfaceName)) {
            return;
        }

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
            List<?> ifTypes = classNode.superInterfaceTypes();
            Type targetIf = null;
            for (int i = 0; i < ifTypes.size(); i++) {
                targetIf = (Type) ifTypes.get(i);
                if (targetIf.resolveBinding().getName().startsWith(simpleName)) {
                    break;
                }
            }

            if (targetIf != null) {
                buffer.replace(targetIf.getStartPosition(), targetIf.getLength(), interfaceName);
            }

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
        }
        return interfaceName.substring(0, interfaceName.indexOf('<'));
    }

}
