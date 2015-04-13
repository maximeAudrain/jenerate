package org.jenerate.internal.ui.dialogs.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.data.impl.EqualsHashCodeGenerationDataImpl;
import org.jenerate.internal.domain.hashcode.IInitMultNumbers;
import org.jenerate.internal.domain.hashcode.impl.InitMultNumbersCustom;
import org.jenerate.internal.domain.hashcode.impl.InitMultNumbersDefault;
import org.jenerate.internal.domain.hashcode.impl.InitMultNumbersRandom;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;

/**
 * Default implementation of the dialog for the generation of the equals and hashCode methods. Defines specific GUI
 * components for the customization of the HashCodeBuilder odd numbers, and the comparison of references for the equals
 * method.
 * 
 * @author jiayun
 */
public class EqualsHashCodeDialog extends AbstractFieldDialog<EqualsHashCodeGenerationData> {

    /**
     * Dialog settings
     */
    public static final String EQUALS_SETTINGS_SECTION = "EqualsDialog";
    public static final String HASHCODE_SETTINGS_SECTION = "HashCodeDialog";
    private static final String SETTINGS_COMPARE_REFERENCES = "CompareReferences";
    private static final String SETTINGS_INIT_MULT_TYPE = "InitMultType";
    private static final String SETTINGS_INITIAL_NUMBER = "InitialNumber";
    private static final String SETTINGS_MULTIPLIER_NUMBER = "MultiplierNumber";
    private static final String SETTINGS_CLASS_COMPARISON = "ClassComparison";

    private final IDialogSettings equalsDialogSettings;
    private final IDialogSettings hashCodeDialogSettings;
    private final Button imButtons[] = new Button[3];

    private Text initText;
    private Text multText;
    private IInitMultNumbers imNumbers[] = new IInitMultNumbers[] { new InitMultNumbersDefault(),
            new InitMultNumbersRandom(), new InitMultNumbersCustom() };
    private int initMultType;
    private int initialNumber;
    private int multiplierNumber;

    private boolean compareReferences;
    private boolean classComparison;

    public EqualsHashCodeDialog(final Shell parentShell, final String dialogTitle, final IField[] fields,
            LinkedHashSet<StrategyIdentifier> possibleStrategies, final boolean disableAppendSuper,
            PreferencesManager preferencesManager, IDialogSettings dialogSettings,
            LinkedHashMap<String, IJavaElement> insertPositions) {

        super(parentShell, dialogTitle, fields, possibleStrategies, disableAppendSuper, preferencesManager,
                dialogSettings, insertPositions);

        IDialogSettings equalsSettings = dialogSettings.getSection(EQUALS_SETTINGS_SECTION);
        if (equalsSettings == null) {
            equalsSettings = dialogSettings.addNewSection(EQUALS_SETTINGS_SECTION);
        }
        this.equalsDialogSettings = equalsSettings;

        compareReferences = equalsSettings.getBoolean(SETTINGS_COMPARE_REFERENCES);
        
        classComparison = equalsSettings.getBoolean(SETTINGS_CLASS_COMPARISON);

        IDialogSettings hashCodeSettings = dialogSettings.getSection(HASHCODE_SETTINGS_SECTION);
        if (hashCodeSettings == null) {
            hashCodeSettings = dialogSettings.addNewSection(HASHCODE_SETTINGS_SECTION);
        }
        this.hashCodeDialogSettings = hashCodeSettings;

        try {
            initMultType = hashCodeSettings.getInt(SETTINGS_INIT_MULT_TYPE);
        } catch (NumberFormatException e) {
            initMultType = 0;
        }

        try {
            initialNumber = hashCodeSettings.getInt(SETTINGS_INITIAL_NUMBER);
        } catch (NumberFormatException e) {
            initialNumber = 17;
        }

        try {
            multiplierNumber = hashCodeSettings.getInt(SETTINGS_MULTIPLIER_NUMBER);
        } catch (NumberFormatException e) {
            multiplierNumber = 37;
        }
    }

    @Override
    public boolean close() {
        equalsDialogSettings.put(SETTINGS_COMPARE_REFERENCES, compareReferences);
        equalsDialogSettings.put(SETTINGS_CLASS_COMPARISON, classComparison);
        imNumbers[initMultType].setNumbers(initialNumber, multiplierNumber);
        hashCodeDialogSettings.put(SETTINGS_INIT_MULT_TYPE, initMultType);
        hashCodeDialogSettings.put(SETTINGS_INITIAL_NUMBER, initialNumber);
        hashCodeDialogSettings.put(SETTINGS_MULTIPLIER_NUMBER, multiplierNumber);
        return super.close();
    }

    @Override
    public void create() {
        super.create();

        imButtons[initMultType].setSelection(true);
        initText.setText(String.valueOf(initialNumber));
        multText.setText(String.valueOf(multiplierNumber));
        if (initMultType != 2) {
            initText.setEnabled(false);
            multText.setEnabled(false);
        }
        fieldViewer.getTable().setFocus();
    }

    @Override
    protected Composite createInsertPositionsComposite(Composite composite) {
        Composite optionComposite = super.createInsertPositionsComposite(composite);
        addEqualsOptions(optionComposite);
        addInitialMultiplierOptions(optionComposite);
        return optionComposite;
    }

    private Composite addEqualsOptions(final Composite composite) {
        Group group = new Group(composite, SWT.NONE);
        group.setText("Equals");
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        GridLayout layout = new GridLayout(1, false);
        group.setLayout(layout);

        createAndAddCompareReferencesButton(group);
        createAndAddClassComparisonButton(group);

        return composite;
    }

    private void createAndAddCompareReferencesButton(Group group) {
        Button button = new Button(group, SWT.CHECK);
        button.setText("Compare object &references");
        button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        button.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                compareReferences = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        button.setSelection(compareReferences);
    }

    private void createAndAddClassComparisonButton(Group group) {
        Button button = new Button(group, SWT.CHECK);
        button.setText("Use c&lass comparison");
        button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        button.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                classComparison = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        button.setSelection(classComparison);
    }

    private void addInitialMultiplierOptions(final Composite composite) {
        Group group = new Group(composite, SWT.NONE);
        group.setText("HashCode - Initial and multiplier numbers");
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(4, false);
        group.setLayout(layout);

        imButtons[0] = new Button(group, SWT.RADIO);
        imButtons[0].setText("D&efault");
        GridData data = new GridData();
        data.horizontalSpan = 4;
        imButtons[0].setLayoutData(data);

        imButtons[1] = new Button(group, SWT.RADIO);
        imButtons[1].setText("&Random generate");
        data = new GridData();
        data.horizontalSpan = 4;
        imButtons[1].setLayoutData(data);

        imButtons[2] = new Button(group, SWT.RADIO);
        imButtons[2].setText("C&ustom:");
        data = new GridData();
        data.horizontalSpan = 4;
        imButtons[2].setLayoutData(data);

        Label initLabel = new Label(group, SWT.NONE);
        initLabel.setText("Initial:");
        data = new GridData();
        data.horizontalIndent = 30;
        initLabel.setLayoutData(data);

        initText = new Text(group, SWT.SINGLE | SWT.RIGHT | SWT.BORDER);
        initText.addVerifyListener(new IntegerVerifyListener(initText));
        initText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkInput();
            }

        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        initText.setLayoutData(data);

        Label multLabel = new Label(group, SWT.NONE);
        multLabel.setText("Multiplier:");
        data = new GridData();
        data.horizontalIndent = 20;
        multLabel.setLayoutData(data);

        multText = new Text(group, SWT.SINGLE | SWT.RIGHT | SWT.BORDER);
        multText.addVerifyListener(new IntegerVerifyListener(multText));
        multText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkInput();
            }

        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        multText.setLayoutData(data);

        imButtons[0].addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                if (button.getSelection()) {
                    initMultType = 0;
                }
            }
        });

        imButtons[1].addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                if (button.getSelection()) {
                    initMultType = 1;
                }
            }
        });

        imButtons[2].addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                if (button.getSelection()) {
                    initMultType = 2;
                    initText.setEnabled(true);
                    multText.setEnabled(true);
                } else {
                    initText.setEnabled(false);
                    multText.setEnabled(false);
                }
            }
        });

    }

    private void checkInput() {
        String text = initText.getText();
        int init;
        try {
            init = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showNotOddMessage("Initial number");
            return;
        }
        if (init % 2 == 0) {
            showNotOddMessage("Initial Number");
            return;
        }
        initialNumber = init;

        text = multText.getText();
        int mult;
        try {
            mult = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showNotOddMessage("Multiplier number");
            return;
        }
        if (mult % 2 == 0) {
            showNotOddMessage("Multiplier Number");
            return;
        }
        multiplierNumber = mult;
        clearMessage();
    }

    private void showNotOddMessage(String title) {
        messageLabel.setImage(JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_ERROR));
        messageLabel.setText(title + " must be an odd number.");
        messageLabel.setVisible(true);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    private void clearMessage() {
        messageLabel.setImage(null);
        messageLabel.setText(null);
        messageLabel.setVisible(false);
        getButton(IDialogConstants.OK_ID).setEnabled(true);
    }

    @Override
    public EqualsHashCodeGenerationData getData() {
        //@formatter:off
        return new EqualsHashCodeGenerationDataImpl.Builder()
                .withCheckedFields(getCheckedFields())
                .withSelectedContentStrategy(getStrategyIdentifier())
                .withElementPosition(getElementPosition())
                .withAppendSuper(getAppendSuper())
                .withGenerateComment(getGenerateComment())
                .withUseBlockInIfStatements(getUseBlockInIfStatements())
                .withUseGettersInsteadOfFields(getUseGettersInsteadOfFields())
                .withCompareReferences(compareReferences)
                .withClassComparison(classComparison)
                .withInitMultNumbers(imNumbers[initMultType])
                .build();
        //@formatter:on
    }

    @Override
    public Dialog getDialog() {
        return this;
    }

    private static class IntegerVerifyListener implements VerifyListener {

        private Text inputText;

        public IntegerVerifyListener(Text inputText) {
            super();
            this.inputText = inputText;
        }

        @Override
        public void verifyText(VerifyEvent e) {

            StringBuffer number = new StringBuffer(inputText.getText());
            number.insert(e.start, e.text);

            try {
                Integer.parseInt(number.toString());
            } catch (NumberFormatException nfe) {
                e.doit = false;
                return;
            }
        }

    }
}