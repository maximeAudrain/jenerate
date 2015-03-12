package org.jenerate.internal.ui.dialogs.provider;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public interface DialogProvider<T extends FieldDialog> {

    T getDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods, IField[] fields,
            boolean disableAppendSuper, PreferencesManager preferencesManager) throws JavaModelException;

}
