package org.jenerate.internal.manage;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.ui.dialogs.strategy.DialogStrategy;

public interface DialogStrategyManager {

    <U extends MethodGenerationData> DialogStrategy<U> getDialogStrategy(CommandIdentifier commandIdentifier,
            StrategyIdentifier strategyIdentifier);

}
