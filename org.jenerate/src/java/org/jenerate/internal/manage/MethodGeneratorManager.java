package org.jenerate.internal.manage;

import org.jenerate.internal.data.JenerateDialogData;
import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.MethodGenerator;
import org.jenerate.internal.ui.dialogs.JenerateDialog;

public interface MethodGeneratorManager {

    MethodGenerator<? extends MethodSkeleton<?>, ? extends JenerateDialog<?>, ? extends JenerateDialogData> getGenericGenerator(
            UserActionIdentifier userActionIdentifier);

}
