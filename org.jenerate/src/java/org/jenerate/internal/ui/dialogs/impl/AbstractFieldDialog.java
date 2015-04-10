// $Id$
package org.jenerate.internal.ui.dialogs.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.impl.DialogFactoryHelperImpl;

/**
 * An abstract {@link Dialog} allowing configuration of the different parameters for the method generation. It contains
 * for example the different fields present in the class where code will be generated. This class contains some code from
 * org.eclipse.jdt.internal.ui.dialogs.SourceActionDialog
 * 
 * @author jiayun, maudrain
 */
public abstract class AbstractFieldDialog<T extends MethodGenerationData> extends Dialog implements FieldDialog<T> {

    /**
     * Dialog settings
     */
    public static final String ABSTRACT_SETTINGS_SECTION = "AbstractFieldDialog";
    public static final String SETTINGS_APPEND_SUPER = "AppendSuper";
    private static final String SETTINGS_INSERT_POSITION = "InsertPosition";
    private static final String SETTINGS_GENERATE_COMMENT = "GenerateComment";
    private static final String SETTINGS_USE_GETTERS = "UseGetters";

    /**
     * Immutable parameters
     */
    private final String title;
    private final IField[] allFields;
    private final boolean disableAppendSuper;
    private final PreferencesManager preferencesManager;
    private final LinkedHashSet<StrategyIdentifier> possibleStrategies;
    private final LinkedHashMap<String, IJavaElement> insertPositions;
    private final IDialogSettings dialogSettings;

    /**
     * User configurable generation data
     */
    private IField[] selectedFields;
    private StrategyIdentifier currentStrategy;
    private String currentPosition;
    private boolean appendSuper;
    private boolean generateComment;
    private boolean useGettersInsteadOfFields;
    private boolean useBlockInIfStatements;

    /**
     * GUI components
     */
    protected CLabel messageLabel;
    protected CheckboxTableViewer fieldViewer;

    public AbstractFieldDialog(Shell parentShell, String dialogTitle, IField[] fields,
            LinkedHashSet<StrategyIdentifier> possibleStrategies, PreferencesManager preferencesManager,
            IDialogSettings dialogSettings, LinkedHashMap<String, IJavaElement> insertPositions) {
        this(parentShell, dialogTitle, fields, possibleStrategies, false, preferencesManager, dialogSettings,
                insertPositions);
    }

    public AbstractFieldDialog(Shell parentShell, String dialogTitle, IField[] fields,
            LinkedHashSet<StrategyIdentifier> possibleStrategies, boolean disableAppendSuper,
            PreferencesManager preferencesManager, IDialogSettings dialogSettings,
            LinkedHashMap<String, IJavaElement> insertPositions) {
        super(parentShell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        this.title = dialogTitle;
        this.allFields = fields;
        this.disableAppendSuper = disableAppendSuper;
        this.preferencesManager = preferencesManager;
        this.possibleStrategies = possibleStrategies;
        this.insertPositions = insertPositions;

        IDialogSettings settings = dialogSettings.getSection(ABSTRACT_SETTINGS_SECTION);
        if (settings == null) {
            settings = dialogSettings.addNewSection(ABSTRACT_SETTINGS_SECTION);
        }
        this.dialogSettings = settings;

        currentPosition = settings.get(SETTINGS_INSERT_POSITION);
        if (currentPosition == null || currentPosition.isEmpty()) {
            currentPosition = DialogFactoryHelperImpl.FIRST_METHOD_POSITION;
        }

        if (disableAppendSuper) {
            appendSuper = false;
        } else {
            appendSuper = settings.getBoolean(SETTINGS_APPEND_SUPER);
        }
        generateComment = settings.getBoolean(SETTINGS_GENERATE_COMMENT);
        useGettersInsteadOfFields = settings.getBoolean(SETTINGS_USE_GETTERS);

    }

    @Override
    public boolean close() {
        List<Object> list = Arrays.asList(fieldViewer.getCheckedElements());
        selectedFields = list.toArray(new IField[list.size()]);

        if (DialogFactoryHelperImpl.FIRST_METHOD_POSITION.equals(currentPosition)
                || DialogFactoryHelperImpl.LAST_METHOD_POSITION.equals(currentPosition)) {
            dialogSettings.put(SETTINGS_INSERT_POSITION, currentPosition);
        }

        if (!disableAppendSuper) {
            dialogSettings.put(SETTINGS_APPEND_SUPER, appendSuper);
        }
        dialogSettings.put(SETTINGS_GENERATE_COMMENT, generateComment);
        dialogSettings.put(SETTINGS_USE_GETTERS, useGettersInsteadOfFields);

        return super.close();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(title);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = (GridLayout) composite.getLayout();
        layout.numColumns = 2;

        Label fieldSelectionLabel = new Label(composite, SWT.LEFT);
        fieldSelectionLabel.setText("&Select allFields to use in the generated method:");
        GridData data = new GridData();
        data.horizontalSpan = 2;
        fieldSelectionLabel.setLayoutData(data);

        Composite fieldComposite = createFieldComposite(composite);
        data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 350;
        data.heightHint = 250;
        fieldComposite.setLayoutData(data);

        Composite buttonComposite = createButtonComposite(composite);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING);
        data.widthHint = 150;
        buttonComposite.setLayoutData(data);

        Composite strategySelectionComposite = createStrategySelectionComposite(composite);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        strategySelectionComposite.setLayoutData(data);

        Composite optionComposite = createInsertPositionsComposite(composite);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        optionComposite.setLayoutData(data);
        addAppendSuperOption(optionComposite);

        Composite commentComposite = createCommentSelection(composite);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        commentComposite.setLayoutData(data);

        Composite gettersComposite = createGettersSelection(composite);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        gettersComposite.setLayoutData(data);

        Composite blocksInIfComposite = createBlocksInIfSelection(composite);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        blocksInIfComposite.setLayoutData(data);

        messageLabel = new CLabel(composite, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        messageLabel.setLayoutData(data);
        messageLabel.setVisible(false);

        return composite;
    }

    private Composite createFieldComposite(final Composite composite) {
        Composite fieldComposite = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        fieldComposite.setLayout(layout);

        fieldViewer = CheckboxTableViewer.newCheckList(fieldComposite, SWT.MULTI | SWT.TOP | SWT.BORDER);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.grabExcessHorizontalSpace = true;

        fieldViewer.getTable().setLayoutData(data);

        fieldViewer.setLabelProvider(new JavaElementLabelProvider());
        fieldViewer.setContentProvider(new ArrayContentProvider());
        fieldViewer.setInput(allFields);
        selectAllNonTransientFields();
        return fieldComposite;
    }

    private void selectAllNonTransientFields() {
        int size = fieldViewer.getTable().getItemCount();
        for (int i = 0; i < size; i++) {
            IField f = (IField) fieldViewer.getElementAt(i);
            try {
                if (!Flags.isTransient(f.getFlags())) {
                    fieldViewer.setChecked(f, true);
                }
            } catch (JavaModelException e) {
                // ignore
            }
        }
    }

    private Composite createButtonComposite(final Composite composite) {
        Composite buttonComposite = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        buttonComposite.setLayout(layout);

        addButtons(buttonComposite);

        return buttonComposite;
    }

    protected void addButtons(final Composite buttonComposite) {
        GridData data;
        Button selectAllButton = new Button(buttonComposite, SWT.PUSH);
        selectAllButton.setText("Select &All");
        selectAllButton.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                fieldViewer.setAllChecked(true);
            }
        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        selectAllButton.setLayoutData(data);

        Button deselectAllButton = new Button(buttonComposite, SWT.PUSH);
        deselectAllButton.setText("&Deselect All");
        deselectAllButton.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                fieldViewer.setAllChecked(false);
            }
        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        deselectAllButton.setLayoutData(data);
    }

    protected Composite createStrategySelectionComposite(final Composite composite) {
        Composite optionComposite = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        optionComposite.setLayout(layout);

        addStrategySelectionChoices(optionComposite);

        return optionComposite;
    }

    private Composite addStrategySelectionChoices(final Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText("&Content strategy to use:");

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        label.setLayoutData(data);

        final Combo combo = new Combo(composite, SWT.READ_ONLY);
        final StrategyIdentifier[] identifiers = possibleStrategies.toArray(new StrategyIdentifier[possibleStrategies
                .size()]);
        String[] identifierNames = new String[identifiers.length];
        for (int i = 0; i < identifiers.length; i++) {
            identifierNames[i] = identifiers[i].toString();
        }
        combo.setItems(identifierNames);

        data = new GridData(GridData.FILL_HORIZONTAL);
        combo.setLayoutData(data);
        combo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                currentStrategy = identifiers[combo.getSelectionIndex()];
            }
        });

        MethodContentStrategyIdentifier preferedStrategy = preferencesManager
                .getCurrentPreferenceValue(JeneratePreferences.PREFERED_COMMON_METHODS_CONTENT_STRATEGY);
        if (possibleStrategies.contains(preferedStrategy)) {
            combo.select(preferedStrategy.ordinal());
            currentStrategy = preferedStrategy;
        } else {
            MessageDialog.openWarning(getParentShell(), "Warning", "The prefered method content strategy '"
                    + preferedStrategy + "' is not defined for the current method generation. "
                    + "Setting to the first possible method content strategy.");
            combo.select(0);
            currentStrategy = possibleStrategies.iterator().next();
        }
        return composite;
    }

    protected Composite createInsertPositionsComposite(final Composite composite) {
        Composite optionComposite = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        optionComposite.setLayout(layout);

        addPositionChoices(optionComposite);

        return optionComposite;
    }

    private Composite addPositionChoices(final Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText("&Insertion point:");

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        label.setLayoutData(data);

        Combo combo = new Combo(composite, SWT.READ_ONLY);
        Collection<String> insertPositionLabels = insertPositions.keySet();
        combo.setItems(insertPositionLabels.toArray(new String[insertPositionLabels.size()]));
        combo.select(combo.indexOf(currentPosition));

        data = new GridData(GridData.FILL_HORIZONTAL);
        combo.setLayoutData(data);
        combo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                Combo combobox = (Combo) event.widget;
                currentPosition = combobox.getItem(combobox.getSelectionIndex());
            }
        });

        return composite;
    }

    private Composite addAppendSuperOption(final Composite composite) {

        Button appendButton = new Button(composite, SWT.CHECK);
        appendButton.setText("A&ppend super");
        appendButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        appendButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                appendSuper = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        if (disableAppendSuper) {
            appendButton.setSelection(false);
            appendButton.setEnabled(false);
        } else {
            appendButton.setSelection(appendSuper);
        }

        return composite;
    }

    protected Composite createCommentSelection(final Composite composite) {
        Composite commentComposite = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        commentComposite.setLayout(layout);

        Button commentButton = new Button(commentComposite, SWT.CHECK);
        commentButton.setText("Generate method &comment");
        commentButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        commentButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                generateComment = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        commentButton.setSelection(generateComment);

        return commentComposite;
    }

    protected Composite createGettersSelection(final Composite composite) {
        Composite gettersComposite = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        gettersComposite.setLayout(layout);

        Button gettersButton = new Button(gettersComposite, SWT.CHECK);
        gettersButton.setText("Use &getters instead of allFields");
        gettersButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        gettersButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                useGettersInsteadOfFields = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        useGettersInsteadOfFields = preferencesManager.getCurrentPreferenceValue(
                JeneratePreferences.USE_GETTERS_INSTEAD_OF_FIELDS).booleanValue();
        gettersButton.setSelection(useGettersInsteadOfFields);

        return gettersComposite;
    }

    protected Composite createBlocksInIfSelection(final Composite composite) {
        Composite blocksInIfComposite = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        blocksInIfComposite.setLayout(layout);

        Button blocksInIfButton = new Button(blocksInIfComposite, SWT.CHECK);
        blocksInIfButton.setText("&Use blocks in 'if' statments");
        blocksInIfButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        blocksInIfButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                useBlockInIfStatements = (((Button) e.widget).getSelection());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        useBlockInIfStatements = preferencesManager.getCurrentPreferenceValue(
                JeneratePreferences.USE_BLOCKS_IN_IF_STATEMENTS).booleanValue();
        blocksInIfButton.setSelection(useBlockInIfStatements);

        return blocksInIfComposite;
    }

    public IField[] getCheckedFields() {
        return selectedFields;
    }

    public boolean getAppendSuper() {
        return appendSuper;
    }

    public boolean getGenerateComment() {
        return generateComment;
    }

    public StrategyIdentifier getStrategyIdentifier() {
        return currentStrategy;
    }

    public IJavaElement getElementPosition() {
        return insertPositions.get(currentPosition);
    }

    public boolean getUseGettersInsteadOfFields() {
        return useGettersInsteadOfFields;
    }

    public boolean getUseBlockInIfStatements() {
        return useBlockInIfStatements;
    }

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public IField[] getFields() {
        return allFields;
    }

    public LinkedHashSet<StrategyIdentifier> getPossibleStrategies() {
        return possibleStrategies;
    }
}
