package org.jenerate.internal.domain;

import java.util.Set;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.method.Method;

/**
 * Defines the libraries to import for a certain {@link Method}
 * 
 * @author maudrain
 */
public interface MethodLibraries {

    Set<String> getLibrariesToImport(JenerateDialogData data);

}
