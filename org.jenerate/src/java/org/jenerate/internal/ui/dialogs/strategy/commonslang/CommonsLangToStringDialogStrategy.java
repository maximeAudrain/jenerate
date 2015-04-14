package org.jenerate.internal.ui.dialogs.strategy.commonslang;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.data.impl.ToStringGenerationDataImpl;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringStyle;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.strategy.DialogStrategy;

public class CommonsLangToStringDialogStrategy implements DialogStrategy<ToStringGenerationData> {

    public static final String SETTINGS_SECTION = "ToStringDialog";
    private static final String SETTINGS_STYLE = "CommonsLangToStringStyle";

    private final StrategyIdentifier strategyIdentifier;
    private final PreferencesManager preferencesManager;

    private IDialogSettings toStringDialogSettings;

    private Combo styleCombo;
    private String toStringStyle;
    private Label label;

    public CommonsLangToStringDialogStrategy(StrategyIdentifier strategyIdentifier,
            PreferencesManager preferencesManager) {
        this.strategyIdentifier = strategyIdentifier;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public CommandIdentifier getCommandIdentifier() {
        return MethodsGenerationCommandIdentifier.TO_STRING;
    }

    @Override
    public StrategyIdentifier getStrategyIdentifier() {
        return strategyIdentifier;
    }

    @Override
    public void configureSpecificDialogSettings(IDialogSettings dialogSettings) {
        IDialogSettings toStringSettings = dialogSettings.getSection(SETTINGS_SECTION);
        if (toStringSettings == null) {
            toStringSettings = dialogSettings.addNewSection(SETTINGS_SECTION);
        }
        this.toStringDialogSettings = toStringSettings;

        toStringStyle = toStringSettings.get(SETTINGS_STYLE);
        if (toStringStyle == null) {
            toStringStyle = CommonsLangToStringStyle.NO_STYLE.getFullLibraryString(preferencesManager
                    .getCurrentPreferenceValue(JeneratePreferences.PREFERED_COMMON_METHODS_CONTENT_STRATEGY));
        }
    }

    @Override
    public void callbackBeforeDialogClosing() {
        if (styleCombo != null) {
            toStringStyle = styleCombo.getText();
            toStringDialogSettings.put(SETTINGS_STYLE, toStringStyle);
        }
    }

    @Override
    public void createSpecificComponents(FieldDialog<ToStringGenerationData> fieldDialog) {
        if (label == null && styleCombo == null) {
            addStyleChoices(fieldDialog.getEditableComposite());
        }
    }

    @Override
    public void disposeSpecificComponents() {
        if (label != null && styleCombo != null) {
            label.dispose();
            styleCombo.dispose();
            label = null;
            styleCombo = null;
        }
    }

    @Override
    public ToStringGenerationData getData(MethodGenerationData methodGenerationData) {
        //@formatter:off
        return new ToStringGenerationDataImpl.Builder()
                .withCheckedFields(methodGenerationData.getCheckedFields())
                .withSelectedContentStrategy(methodGenerationData.getSelectedContentStrategy())
                .withElementPosition(methodGenerationData.getElementPosition())
                .withAppendSuper(methodGenerationData.getAppendSuper())
                .withGenerateComment(methodGenerationData.getGenerateComment())
                .withUseBlockInIfStatements(methodGenerationData.getUseBlockInIfStatements())
                .withUseGettersInsteadOfFields(methodGenerationData.getUseGettersInsteadOfFields())
                .withToStringStyle(CommonsLangToStringStyle.getToStringStyle(toStringStyle))
                .build();
        //@formatter:on
    }

    private Composite addStyleChoices(final Composite composite) {
        label = new Label(composite, SWT.NONE);
        label.setText("&ToString style:");

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        label.setLayoutData(data);

        styleCombo = new Combo(composite, SWT.NONE);
        String[] stylesArray = getStyles();
        styleCombo.setItems(stylesArray);
        styleCombo.setText(toStringStyle);

        data = new GridData(GridData.FILL_HORIZONTAL);
        styleCombo.setLayoutData(data);
        styleCombo.setToolTipText(CommonsLangToStringStyle.getToStringStyle(toStringStyle).getToolTip());

        styleCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                Combo combo = (Combo) event.getSource();
                combo.setToolTipText(CommonsLangToStringStyle.getToStringStyle(combo.getText()).getToolTip());
            }
        });

        return composite;
    }

    private String[] getStyles() {
        List<String> styles = new ArrayList<String>();
        for (CommonsLangToStringStyle style : CommonsLangToStringStyle.values()) {
            if (CommonsLangToStringStyle.NO_STYLE.equals(style)) {
                styles.add(CommonsLangToStringStyle.NO_STYLE.name());
            } else {
                styles.add(style.getFullLibraryString(strategyIdentifier));
            }
        }
        String[] stylesArray = styles.toArray(new String[styles.size()]);
        return stylesArray;
    }
}
