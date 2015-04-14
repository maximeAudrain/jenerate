package org.jenerate.internal.manage;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.ui.dialogs.strategy.DialogStrategy;

/**
 * Manager responsible for retrieving a {@link DialogStrategy} for a given {@link CommandIdentifier} and
 * {@link StrategyIdentifier}
 * 
 * @author maudrain
 */
public interface DialogStrategyManager {

    /**
     * Get the {@link DialogStrategy} for the given parameters
     * 
     * @param commandIdentifier the unique identifier of a command
     * @param strategyIdentifier the unique identifier of a strategy
     * @return the {@link DialogStrategy} for the provided parameters
     * @throws IllegalStateException if no {@link DialogStrategy} could be found for a provided parameters
     */
    <U extends MethodGenerationData> DialogStrategy<U> getDialogStrategy(CommandIdentifier commandIdentifier,
            StrategyIdentifier strategyIdentifier);

}
