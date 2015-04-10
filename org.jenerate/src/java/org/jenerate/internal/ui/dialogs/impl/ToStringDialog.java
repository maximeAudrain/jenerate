package org.jenerate.internal.ui.dialogs.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.data.impl.ToStringGenerationDataImpl;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringStyle;

/**
 * Default implementation of the dialog for the generation of the toString method. Defines specific GUI components for
 * the customization of the toStringStyle for the ToStringBuilder.
 * 
 * @author jiayun
 */
public class ToStringDialog extends AbstractOrderableFieldDialog<ToStringGenerationData> {

    public static final String SETTINGS_SECTION = "ToStringDialog";
    private static final String SETTINGS_STYLE = "CommonsLangToStringStyle";

    private final IDialogSettings toStringDialogSettings;

    private Combo styleCombo;
    private String toStringStyle;

    public ToStringDialog(final Shell parentShell, final String dialogTitle, final IField[] fields,
            LinkedHashSet<StrategyIdentifier> possibleStrategies, final boolean disableAppendSuper,
            PreferencesManager preferencesManager, IDialogSettings dialogSettings,
            LinkedHashMap<String, IJavaElement> insertPositions) {

        super(parentShell, dialogTitle, fields, possibleStrategies, disableAppendSuper, preferencesManager,
                dialogSettings, insertPositions);

        IDialogSettings toStringSettings = dialogSettings.getSection(SETTINGS_SECTION);
        if (toStringSettings == null) {
            toStringSettings = dialogSettings.addNewSection(SETTINGS_SECTION);
        }
        this.toStringDialogSettings = toStringSettings;

        toStringStyle = toStringSettings.get(SETTINGS_STYLE);
        if (toStringStyle == null) {
            toStringStyle = CommonsLangToStringStyle.NO_STYLE.getFullLibraryString(getPreferencesManager()
                    .getCurrentPreferenceValue(JeneratePreferences.PREFERED_COMMON_METHODS_CONTENT_STRATEGY));
        }
    }

    @Override
    public boolean close() {
        toStringStyle = styleCombo.getText();
        toStringDialogSettings.put(SETTINGS_STYLE, toStringStyle);
        return super.close();
    }

    @Override
    protected Composite createInsertPositionsComposite(Composite composite) {
        Composite optionComposite = super.createInsertPositionsComposite(composite);
        addStyleChoices(optionComposite);
        return optionComposite;
    }

    private Composite addStyleChoices(final Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText("&ToString style:");

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        label.setLayoutData(data);

        styleCombo = new Combo(composite, SWT.NONE);
        String[] stylesArray = getStyles();
        styleCombo.setItems(stylesArray);
        styleCombo.setText(toStringStyle);

        data = new GridData(GridData.FILL_HORIZONTAL);
        styleCombo.setLayoutData(data);

        return composite;
    }

    private String[] getStyles() {
        StrategyIdentifier currentStrategy = getStrategyIdentifier();
        List<String> styles = new ArrayList<String>();
        for (CommonsLangToStringStyle style : CommonsLangToStringStyle.values()) {
            if (CommonsLangToStringStyle.NO_STYLE.equals(style)) {
                styles.add(CommonsLangToStringStyle.NO_STYLE.name());
            } else {
                styles.add(style.getFullLibraryString(currentStrategy));
            }
        }
        String[] stylesArray = styles.toArray(new String[styles.size()]);
        return stylesArray;
    }

    @Override
    public ToStringGenerationData getData() {
        //@formatter:off
        return new ToStringGenerationDataImpl.Builder()
                .withCheckedFields(getCheckedFields())
                .withSelectedContentStrategy(getStrategyIdentifier())
                .withElementPosition(getElementPosition())
                .withAppendSuper(getAppendSuper())
                .withGenerateComment(getGenerateComment())
                .withUseBlockInIfStatements(getUseBlockInIfStatements())
                .withUseGettersInsteadOfFields(getUseGettersInsteadOfFields())
                .withToStringStyle(CommonsLangToStringStyle.getToStringStyle(toStringStyle))
                .build();
        //@formatter:on
    }

    @Override
    public Dialog getDialog() {
        return this;
    }
}
