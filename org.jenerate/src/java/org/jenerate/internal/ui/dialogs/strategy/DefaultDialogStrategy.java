package org.jenerate.internal.ui.dialogs.strategy;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.data.impl.AbstractMethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;

public class DefaultDialogStrategy<U extends MethodGenerationData> implements DialogStrategy<U> {

    private CommandIdentifier commandIdentifier;
    private final StrategyIdentifier strategyIdentifier;
    private AbstractMethodGenerationData.Builder<?> dataBuilder;

    public DefaultDialogStrategy(AbstractMethodGenerationData.Builder<?> dataBuilder,
            CommandIdentifier commandIdentifier, StrategyIdentifier strategyIdentifier) {
        this.dataBuilder = dataBuilder;
        this.commandIdentifier = commandIdentifier;
        this.strategyIdentifier = strategyIdentifier;
    }

    @Override
    public CommandIdentifier getCommandIdentifier() {
        return this.commandIdentifier;
    }

    @Override
    public StrategyIdentifier getStrategyIdentifier() {
        return this.strategyIdentifier;
    }

    @Override
    public void configureSpecificDialogSettings(IDialogSettings dialogSettings) {
        /* Nothing additional to be done here */
    }

    @Override
    public void callbackBeforeDialogClosing() {
        /* Nothing additional to be done here */
    }

    @Override
    public void createSpecificComponents(FieldDialog<U> fieldDialog) {
        /* Nothing additional to be done here */
    }

    @Override
    public void disposeSpecificComponents() {
        /* Nothing additional to be done here */
    }

    @Override
    public U getData(MethodGenerationData methodGenerationData) {
        return dataBuilder.withCheckedFields(methodGenerationData.getCheckedFields())
                .withSelectedContentStrategy(methodGenerationData.getSelectedContentStrategy())
                .withElementPosition(methodGenerationData.getElementPosition())
                .withAppendSuper(methodGenerationData.getAppendSuper())
                .withGenerateComment(methodGenerationData.getGenerateComment())
                .withUseBlockInIfStatements(methodGenerationData.getUseBlockInIfStatements())
                .withUseGettersInsteadOfFields(methodGenerationData.getUseGettersInsteadOfFields()).build();
    }
}
