package org.jenerate.internal.util.impl;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.text.edits.TextEdit;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;

public final class JeneratePluginCodeFormatterImpl implements JeneratePluginCodeFormatter {

    @Override
    public String formatCode(IType objectClass, String source) throws JavaModelException, BadLocationException {
        String lineDelim = getLineDelimiterUsed(objectClass);
        int indent = getIndentUsed(objectClass) + 1;
        TextEdit textEdit = ToolFactory.createCodeFormatter(null).format(CodeFormatter.K_CLASS_BODY_DECLARATIONS,
                source, 0, source.length(), indent, lineDelim);
        if (textEdit == null) {
            return source;
        }
        Document document = new Document(source);
        textEdit.apply(document);
        return document.get();
    }

    /**
     * Examines a string and returns the first line delimiter found.
     */
    private static String getLineDelimiterUsed(IJavaElement elem) throws JavaModelException {
        ICompilationUnit cu = (ICompilationUnit) elem.getAncestor(IJavaElement.COMPILATION_UNIT);
        if (cu != null && cu.exists()) {
            IBuffer buf = cu.getBuffer();
            int length = buf.getLength();
            for (int i = 0; i < length; i++) {
                char ch = buf.getChar(i);
                if (ch == SWT.CR) {
                    if (i + 1 < length) {
                        if (buf.getChar(i + 1) == SWT.LF) {
                            return "\r\n"; //$NON-NLS-1$
                        }
                    }
                    return "\r"; //$NON-NLS-1$
                } else if (ch == SWT.LF) {
                    return "\n"; //$NON-NLS-1$
                }
            }
        }
        return System.getProperty("line.separator", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Evaluates the indention used by a Java element. (in tabulators)
     */
    private int getIndentUsed(IJavaElement elem) throws JavaModelException {
        if (elem instanceof ISourceReference) {
            ICompilationUnit cu = (ICompilationUnit) elem.getAncestor(IJavaElement.COMPILATION_UNIT);
            if (cu != null) {
                IBuffer buf = cu.getBuffer();
                int offset = ((ISourceReference) elem).getSourceRange().getOffset();
                int i = offset;
                // find beginning of line
                while (i > 0 && !isLineDelimiterChar(buf.getChar(i - 1))) {
                    i--;
                }
                return computeIndent(buf.getText(i, offset - i), getTabWidth());
            }
        }
        return 0;
    }

    private int getTabWidth() {
        return Platform.getPreferencesService().getInt("org.eclipse.jdt.core",
                DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, 4, null);
    }

    /**
     * Indent char is a space char but not a line delimiters.
     * <code>== Character.isWhitespace(ch) && ch != '\n' && ch != '\r'</code>
     */
    private boolean isIndentChar(char ch) {
        return Character.isWhitespace(ch) && !isLineDelimiterChar(ch);
    }

    /**
     * Line delimiter chars are '\n' and '\r'.
     */
    private boolean isLineDelimiterChar(char ch) {
        return ch == '\n' || ch == '\r';
    }

    /**
     * Returns the indent of the given string.
     * 
     * @param line the text line
     * @param tabWidth the width of the '\t' character.
     */
    private int computeIndent(String line, int tabWidth) {
        int result = 0;
        int blanks = 0;
        int size = line.length();
        for (int i = 0; i < size; i++) {
            char c = line.charAt(i);
            if (c == '\t') {
                result++;
                blanks = 0;
            } else if (isIndentChar(c)) {
                blanks++;
                if (blanks == tabWidth) {
                    result++;
                    blanks = 0;
                }
            } else {
                return result;
            }
        }
        return result;
    }

}
