package org.jenerate.internal.lang.generators;

import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.method.Method;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.ui.dialogs.FieldDialog;

public interface MethodGenerator<T extends MethodSkeleton<V>, U extends FieldDialog<V>, V extends JenerateDialogData> {

    void generate(Shell parentShell, IType objectClass, Set<Method<T, V>> methods);

}
