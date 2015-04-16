package org.jenerate.internal.ui.dialogs.strategy.commonslang;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.data.impl.EqualsHashCodeGenerationDataImpl.Builder;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.strategy.DialogStrategy;

/**
 * Defines specific dialog behaviors for the equals and hashCode methods generation using the guava method content
 * strategies.
 * 
 * @author maudrain
 */
public final class GuavaEqualsHashCodeDialogStrategy implements DialogStrategy<EqualsHashCodeGenerationData> {

    private final EqualsHashCodeDialogStrategyHelper equalsHashCodeDialogStrategyHelper = new EqualsHashCodeDialogStrategyHelper();

    @Override
    public CommandIdentifier getCommandIdentifier() {
        return MethodsGenerationCommandIdentifier.EQUALS_HASH_CODE;
    }

    @Override
    public StrategyIdentifier getStrategyIdentifier() {
        return MethodContentStrategyIdentifier.USE_GUAVA;
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
