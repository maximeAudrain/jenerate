package org.jenerate.internal.manage;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.MethodGenerator;
import org.jenerate.internal.ui.dialogs.JenerateDialog;

public interface MethodGeneratorManager {

    <T extends MethodSkeleton<V>, U extends JenerateDialog<V>, V extends JenerateDialogData> MethodGenerator<T, U, V> getGenericGenerator(
            UserActionIdentifier userActionIdentifier);

}
