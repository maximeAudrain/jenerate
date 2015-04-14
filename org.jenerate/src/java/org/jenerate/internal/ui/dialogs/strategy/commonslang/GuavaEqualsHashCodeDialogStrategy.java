package org.jenerate.internal.ui.dialogs.strategy.commonslang;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.data.impl.EqualsHashCodeGenerationDataImpl;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.strategy.DialogStrategy;

/**
 * XXX copy pasted... See what can be extracted to remove code redundancy
 * 
 * @author maudrain
 */
public class GuavaEqualsHashCodeDialogStrategy implements DialogStrategy<EqualsHashCodeGenerationData> {

    /**
     * Dialog settings
     */
    public static final String EQUALS_SETTINGS_SECTION = "EqualsDialog";
    private static final String SETTINGS_COMPARE_REFERENCES = "CompareReferences";
    private static final String SETTINGS_CLASS_COMPARISON = "ClassComparison";

    private IDialogSettings equalsDialogSettings;

    private boolean compareReferences;
    private boolean classComparison;
    private Group equalsGroup;

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
        IDialogSettings equalsSettings = dialogSettings.getSection(EQUALS_SETTINGS_SECTION);
        if (equalsSettings == null) {
            equalsSettings = dialogSettings.addNewSection(EQUALS_SETTINGS_SECTION);
        }
        this.equalsDialogSettings = equalsSettings;

        compareReferences = equalsSettings.getBoolean(SETTINGS_COMPARE_REFERENCES);
        classComparison = equalsSettings.getBoolean(SETTINGS_CLASS_COMPARISON);
    }

    @Override
    public void callbackBeforeDialogClosing() {
        equalsDialogSettings.put(SETTINGS_COMPARE_REFERENCES, compareReferences);
        equalsDialogSettings.put(SETTINGS_CLASS_COMPARISON, classComparison);
    }

    @Override
    public void createSpecificComponents(FieldDialog<EqualsHashCodeGenerationData> fieldDialog) {
        if (equalsGroup == null) {
            addEqualsOptions(fieldDialog);
        }
    }

    @Override
    public void disposeSpecificComponents() {
        if (equalsGroup != null) {
            equalsGroup.dispose();
            equalsGroup = null;
        }
    }

    @Override
    public EqualsHashCodeGenerationData getData(MethodGenerationData methodGenerationData) {
        //@formatter:off
        return new EqualsHashCodeGenerationDataImpl.Builder()
                .withCheckedFields(methodGenerationData.getCheckedFields())
                .withSelectedContentStrategy(methodGenerationData.getSelectedContentStrategy())
                .withElementPosition(methodGenerationData.getElementPosition())
                .withAppendSuper(methodGenerationData.getAppendSuper())
                .withGenerateComment(methodGenerationData.getGenerateComment())
                .withUseBlockInIfStatements(methodGenerationData.getUseBlockInIfStatements())
                .withUseGettersInsteadOfFields(methodGenerationData.getUseGettersInsteadOfFields())
                .withCompareReferences(compareReferences)
                .withClassComparison(classComparison)
                .build();
        //@formatter:on
    }

    private Composite addEqualsOptions(FieldDialog<EqualsHashCodeGenerationData> fieldDialog) {
        Composite editableComposite = fieldDialog.getEditableComposite();
        equalsGroup = new Group(editableComposite, SWT.NONE);
        equalsGroup.setText("Equals");
        equalsGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        GridLayout layout = new GridLayout(1, false);
        equalsGroup.setLayout(layout);

        createAndAddCompareReferencesButton(equalsGroup);
        createAndAddClassComparisonButton(equalsGroup);

        return editableComposite;
    }

    private void createAndAddCompareReferencesButton(Group group) {
        Button referencesButton = new Button(group, SWT.CHECK);
        referencesButton.setText("Compare object &references");
        referencesButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        referencesButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                compareReferences = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        referencesButton.setSelection(compareReferences);
    }

    private void createAndAddClassComparisonButton(Group group) {
        Button comparisonButton = new Button(group, SWT.CHECK);
        comparisonButton.setText("Use c&lass comparison instead of instanceOf");
        comparisonButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        comparisonButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                classComparison = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        comparisonButton.setSelection(classComparison);
    }
}
