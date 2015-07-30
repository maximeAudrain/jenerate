package org.jenerate.internal.ui.dialogs.strategy;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.data.impl.EqualsHashCodeGenerationDataImpl.Builder;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;

/**
 * Defines specific dialog behaviors for the equals and hashCode methods generation using the guava method content
 * strategies.
 * 
 * @author maudrain
 */
public final class EqualsHashCodeDialogStrategy implements DialogStrategy<EqualsHashCodeGenerationData> {

    private final EqualsHashCodeDialogStrategyHelper equalsHashCodeDialogStrategyHelper = new EqualsHashCodeDialogStrategyHelper();

    private final StrategyIdentifier strategyIdentifier;

    public EqualsHashCodeDialogStrategy(StrategyIdentifier strategyIdentifier) {
        this.strategyIdentifier = strategyIdentifier;
    }

    @Override
    public CommandIdentifier getCommandIdentifier() {
        return MethodsGenerationCommandIdentifier.EQUALS_HASH_CODE;
    }

    @Override
    public StrategyIdentifier getStrategyIdentifier() {
        return this.strategyIdentifier;
    }

    @Override
    public void configureSpecificDialogSettings(IDialogSettings dialogSettings) {
        equalsHashCodeDialogStrategyHelper.configureSpecificDialogSettings(dialogSettings);
    }

    @Override
    public void callbackBeforeDialogClosing() {
        equalsHashCodeDialogStrategyHelper.callbackBeforeDialogClosing();
    }

    @Override
    public void createSpecificComponents(FieldDialog<EqualsHashCodeGenerationData> fieldDialog) {
        equalsHashCodeDialogStrategyHelper.createSpecificComponents(fieldDialog);
    }

    @Override
    public void disposeSpecificComponents() {
        equalsHashCodeDialogStrategyHelper.disposeSpecificComponents();
    }

    @Override
    public EqualsHashCodeGenerationData getData(MethodGenerationData methodGenerationData) {
        Builder builder = equalsHashCodeDialogStrategyHelper.getDataBuilder(methodGenerationData);
        return builder.build();
    }
}
