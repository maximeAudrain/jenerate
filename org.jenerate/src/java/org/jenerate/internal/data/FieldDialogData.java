package org.jenerate.internal.data;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;

public interface FieldDialogData {
    
    IField[] getCheckedFields();
    
    IJavaElement getElementPosition();
    
    boolean getAppendSuper();
    
    boolean getGenerateComment();
    
    boolean getUseGettersInsteadOfFields();
    
    boolean getUseBlockInIfStatements();

}
