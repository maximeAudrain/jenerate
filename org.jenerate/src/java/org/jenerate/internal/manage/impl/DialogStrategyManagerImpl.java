package org.jenerate.internal.manage.impl;

import java.util.HashSet;
import java.util.Set;

import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.data.impl.CompareToGenerationDataImpl;
import org.jenerate.internal.domain.data.impl.ToStringGenerationDataImpl;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.DialogStrategyManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.strategy.DefaultDialogStrategy;
import org.jenerate.internal.ui.dialogs.strategy.DialogStrategy;
import org.jenerate.internal.ui.dialogs.strategy.commonslang.CommonsLangEqualsHashCodeDialogStrategy;
import org.jenerate.internal.ui.dialogs.strategy.commonslang.CommonsLangToStringDialogStrategy;
import org.jenerate.internal.ui.dialogs.strategy.commonslang.GuavaEqualsHashCodeDialogStrategy;

/**
 * Default implementation of the {@link DialogStrategyManager}
 * 
 * @author maudrain
 */
public final class DialogStrategyManagerImpl implements DialogStrategyManager {

    private final Set<DialogStrategy<? extends MethodGenerationData>> dialogStrategies = new HashSet<>();

    public DialogStrategyManagerImpl(PreferencesManager preferencesManager) {
        dialogStrategies.add(new CommonsLangToStringDialogStrategy(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
                preferencesManager));
        dialogStrategies.add(new CommonsLangToStringDialogStrategy(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
                preferencesManager));
        dialogStrategies.add(new DefaultDialogStrategy<ToStringGenerationData>(
                new ToStringGenerationDataImpl.Builder(), MethodsGenerationCommandIdentifier.TO_STRING,
                MethodContentStrategyIdentifier.USE_GUAVA));

        dialogStrategies.add(new CommonsLangEqualsHashCodeDialogStrategy(
                MethodContentStrategyIdentifier.USE_COMMONS_LANG));
        dialogStrategies.add(new CommonsLangEqualsHashCodeDialogStrategy(
                MethodContentStrategyIdentifier.USE_COMMONS_LANG3));
        dialogStrategies.add(new GuavaEqualsHashCodeDialogStrategy());

        dialogStrategies.add(new DefaultDialogStrategy<CompareToGenerationData>(
                new CompareToGenerationDataImpl.Builder(), MethodsGenerationCommandIdentifier.COMPARE_TO,
                MethodContentStrategyIdentifier.USE_COMMONS_LANG));
        dialogStrategies.add(new DefaultDialogStrategy<CompareToGenerationData>(
                new CompareToGenerationDataImpl.Builder(), MethodsGenerationCommandIdentifier.COMPARE_TO,
                MethodContentStrategyIdentifier.USE_COMMONS_LANG3));
        dialogStrategies.add(new DefaultDialogStrategy<CompareToGenerationData>(
                new CompareToGenerationDataImpl.Builder(), MethodsGenerationCommandIdentifier.COMPARE_TO,
                MethodContentStrategyIdentifier.USE_GUAVA));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends MethodGenerationData> DialogStrategy<U> getDialogStrategy(CommandIdentifier commandIdentifier,
            StrategyIdentifier strategyIdentifier) {
        for (DialogStrategy<? extends MethodGenerationData> dialogStrategy : dialogStrategies) {
            if (strategyIdentifier.equals(dialogStrategy.getStrategyIdentifier())
                    && commandIdentifier.equals(dialogStrategy.getCommandIdentifier())) {
                return (DialogStrategy<U>) dialogStrategy;
            }
        }
        throw new IllegalStateException("Unable to retrieve a DialogStrategy for the given command identifier '"
                + commandIdentifier + "' and strategy identifier '" + strategyIdentifier + "'");
    }

}
