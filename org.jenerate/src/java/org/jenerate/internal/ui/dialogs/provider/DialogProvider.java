package org.jenerate.internal.ui.dialogs.provider;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.preferences.PreferencesManager;

/**
 * Defines a provider of dialog to be used when generating the code. This dialog provides user inputs on the preferences
 * for the code generation
 * 
 * @author maudrain
 * @param <T> the type of {@link FieldDialog} this provider provides.
 */
public interface DialogProvider<T extends FieldDialog> {

    /**
     * @param parentShell the parent shell of the dialog
     * @param objectClass the object class
     * @param excludedMethods the excluded method XXX find out why this guy needs it
     * @param fields the different fields of the class where the code will be generated
     * @param disableAppendSuper XXX find out why this guy needs it
     * @param preferencesManager the preference manager
     * @return the dialog
     * @throws JavaModelException in case something wrong happen when the dialog is provided
     */
    T getDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods, IField[] fields,
            boolean disableAppendSuper, PreferencesManager preferencesManager) throws JavaModelException;

}
