package org.jenerate.internal.ui.dialogs.strategy;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;

/**
 * Defines the specific behavior of a dialog depending on the currently selected {@link StrategyIdentifier}.
 * 
 * @author maudrain
 * @param <U> the type of {@link MethodGenerationData} for this {@link DialogStrategy}s
 */
public interface DialogStrategy<U extends MethodGenerationData> {

    /**
     * @return the unique command identifier for this {@link DialogStrategy}
     */
    CommandIdentifier getCommandIdentifier();

    /**
     * @return the unique strategy identifier for this {@link DialogStrategy}
     */
    StrategyIdentifier getStrategyIdentifier();

    /**
     * Configure the specific dialog settings
     * 
     * @param dialogSettings the global settings from where the specific settings will be configured
     */
    void configureSpecificDialogSettings(IDialogSettings dialogSettings);

    /**
     * Callback before the closing of the dialog
     */
    void callbackBeforeDialogClosing();

    /**
     * Create specific UI for this {@link DialogStrategy}
     * 
     * @param fieldDialog the field dialog where the UI will be complementeds
     */
    void createSpecificComponents(FieldDialog<U> fieldDialog);

    /**
     * Dispose of the specific UI for this {@link DialogStrategy}
     */
    void disposeSpecificComponents();

    /**
     * Get all dialog data
     * 
     * @param methodGenerationData the default dialog data
     * @return the full data for this strategys
     */
    U getData(MethodGenerationData methodGenerationData);

}
