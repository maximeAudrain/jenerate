package org.jenerate.internal.data;

import org.eclipse.jdt.core.IJavaElement;

/**
 * Defines data provided by a dialog of the Jenerate plugin. At this stage, it can be any type of data.
 * 
 * @author maudrain
 */
public interface JenerateDialogData {

    IJavaElement getElementPosition();
}
