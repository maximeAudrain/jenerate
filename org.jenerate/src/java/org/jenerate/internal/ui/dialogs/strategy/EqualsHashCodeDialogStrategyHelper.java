package org.jenerate.internal.ui.dialogs.strategy;

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
import org.jenerate.internal.ui.dialogs.FieldDialog;

/**
 * Helper class that contains common UI code for equals and hashCode dialog strategies
 * 
 * @author maudrain
 */
public final class EqualsHashCodeDialogStrategyHelper {

    /**
     * Dialog settings
     */
    public static final String EQUALS_SETTINGS_SECTION = "EqualsDialog";
    private static final String SETTINGS_COMPARE_REFERENCES = "CompareReferences";
    private static final String SETTINGS_CLASS_COMPARISON = "ClassComparison";
    private static final String SETTINGS_CLASS_CAST = "ClassCast";

    private IDialogSettings equalsDialogSettings;

    private boolean compareReferences;
    private boolean classComparison;
    private Group equalsGroup;
    private boolean classCast;

    public void configureSpecificDialogSettings(IDialogSettings dialogSettings) {
        IDialogSettings equalsSettings = dialogSettings.getSection(EQUALS_SETTINGS_SECTION);
        if (equalsSettings == null) {
            equalsSettings = dialogSettings.addNewSection(EQUALS_SETTINGS_SECTION);
        }
        this.equalsDialogSettings = equalsSettings;

        compareReferences = equalsSettings.getBoolean(SETTINGS_COMPARE_REFERENCES);
        classComparison = equalsSettings.getBoolean(SETTINGS_CLASS_COMPARISON);
        classCast = equalsSettings.getBoolean(SETTINGS_CLASS_CAST);
    }

    public void callbackBeforeDialogClosing() {
        equalsDialogSettings.put(SETTINGS_COMPARE_REFERENCES, compareReferences);
        equalsDialogSettings.put(SETTINGS_CLASS_COMPARISON, classComparison);
        equalsDialogSettings.put(SETTINGS_CLASS_CAST, classCast);
    }

    public void createSpecificComponents(FieldDialog<EqualsHashCodeGenerationData> fieldDialog) {
        if (equalsGroup == null) {
            addEqualsOptions(fieldDialog);
        }
    }

    public void disposeSpecificComponents() {
        if (equalsGroup != null) {
            equalsGroup.dispose();
            equalsGroup = null;
        }
    }

    public EqualsHashCodeGenerationDataImpl.Builder getDataBuilder(MethodGenerationData methodGenerationData) {
        //@formatter:off
        return new EqualsHashCodeGenerationDataImpl.Builder()
                .withCheckedFields(methodGenerationData.getCheckedFields())
                .withSelectedContentStrategy(methodGenerationData.getSelectedStrategyIdentifier())
                .withElementPosition(methodGenerationData.getElementPosition())
                .withAppendSuper(methodGenerationData.appendSuper())
                .withGenerateComment(methodGenerationData.generateComment())
                .withUseBlockInIfStatements(methodGenerationData.useBlockInIfStatements())
                .withUseGettersInsteadOfFields(methodGenerationData.useGettersInsteadOfFields())
                .withUseSimplePrimitiveComparison(methodGenerationData.useSimplePrimitiveComparison())
                .withUseDeepArrayComparison(methodGenerationData.useDeepArrayComparison())
                .withCompareReferences(compareReferences)
                .withClassComparison(classComparison)
                .withClassCast(classCast);
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
        createAndAddClassCastButton(equalsGroup);

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

    private void createAndAddClassCastButton(Group group) {
        Button comparisonButton = new Button(group, SWT.CHECK);
        comparisonButton.setText("Use Class#cast(Object) and Class#isInstance(Object) instead of native cast and instanceOf");
        comparisonButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        comparisonButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                classCast = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        comparisonButton.setSelection(classCast);
    }
}
