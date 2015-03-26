package org.jenerate.internal.data;

import org.eclipse.jdt.core.IField;

public interface FieldDialogData extends JenerateDialogData {

    IField[] getCheckedFields();

    boolean getAppendSuper();

    boolean getGenerateComment();

    boolean getUseGettersInsteadOfFields();

    boolean getUseBlockInIfStatements();

}
