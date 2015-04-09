// $Id$
package org.jenerate.internal.ui.dialogs.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
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

/**
 * This class contains some code from org.eclipse.jdt.internal.ui.dialogs.SourceActionDialog
 * 
 * @author jiayun
 */
public abstract class AbstractFieldDialog<T extends MethodGenerationData> extends Dialog implements FieldDialog<T> {

    /**
     * Used for testing
     */
    public static final String ABSTRACT_SETTINGS_SECTION = "AbstractFieldDialog";
    public static final String SETTINGS_APPEND_SUPER = "AppendSuper";

    private static final String SETTINGS_INSERT_POSITION = "InsertPosition";

    private static final String SETTINGS_GENERATE_COMMENT = "GenerateComment";

    private static final String SETTINGS_USE_GETTERS = "UseGetters";

    protected CLabel messageLabel;

    private String title;

    protected CheckboxTableViewer fieldViewer;

    private IField[] fields;

    private IField[] checkedFields;

    private List<IJavaElement> insertPositions;

    private List<String> insertPositionLabels;
    private int currentPositionIndex;

    private StrategyIdentifier currentStrategy;

    private LinkedHashSet<StrategyIdentifier> possibleStrategies;

    private boolean disableAppendSuper;

    private boolean appendSuper;

    private boolean generateComment;

    private boolean useGettersInsteadOfFields;

    private boolean useBlockInIfStatements;

    private IDialogSettings settings;

    private final PreferencesManager preferencesManager;

    public AbstractFieldDialog(Shell parentShell, String dialogTitle, IType objectClass, IField[] fields,
            Set<IMethod> excludedMethods, LinkedHashSet<StrategyIdentifier> possibleStrategies,
            PreferencesManager preferencesManager, IDialogSettings dialogSettings) throws JavaModelException {
        this(parentShell, dialogTitle, objectClass, fields, excludedMethods, possibleStrategies, false,
                preferencesManager, dialogSettings);
    }

    /**
     * @param parentShell
     * @param dialogTitle
     * @param objectClass
     * @param fields
     * @param excludedMethods methods not to be listed in the insertion point combo
     * @throws JavaModelException
     */
    public AbstractFieldDialog(Shell parentShell, String dialogTitle, IType objectClass, IField[] fields,
            Set<IMethod> excludedMethods, LinkedHashSet<StrategyIdentifier> possibleStrategies,
            boolean disableAppendSuper, PreferencesManager preferencesManager, IDialogSettings dialogSettings)
            throws JavaModelException {
        super(parentShell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        this.title = dialogTitle;
        this.fields = fields;
        this.disableAppendSuper = disableAppendSuper;
        this.preferencesManager = preferencesManager;
        this.possibleStrategies = possibleStrategies;

        settings = dialogSettings.getSection(ABSTRACT_SETTINGS_SECTION);
        if (settings == null) {
            settings = dialogSettings.addNewSection(ABSTRACT_SETTINGS_SECTION);
        }

        try {
            currentPositionIndex = settings.getInt(SETTINGS_INSERT_POSITION);
        } catch (NumberFormatException e) {
            currentPositionIndex = 0;
        }

        if (disableAppendSuper) {
            appendSuper = false;
        } else {
            appendSuper = settings.getBoolean(SETTINGS_APPEND_SUPER);
        }
        generateComment = settings.getBoolean(SETTINGS_GENERATE_COMMENT);
        useGettersInsteadOfFields = settings.getBoolean(SETTINGS_USE_GETTERS);

        insertPositions = new ArrayList<>();
        insertPositionLabels = new ArrayList<>();

        IJavaElement[] members = filterOutExcludedElements(objectClass.getChildren(), excludedMethods).toArray(
                new IJavaElement[0]);
        IMethod[] methods = filterOutExcludedElements(objectClass.getMethods(), excludedMethods)
                .toArray(new IMethod[0]);

        insertPositions.add(methods.length > 0 ? methods[0] : null); // first
        insertPositions.add(null); // last

        insertPositionLabels.add("First method");
        insertPositionLabels.add("Last method");

        for (int i = 0; i < methods.length; i++) {
            IMethod curr = methods[i];
            String methodLabel = getMethodLabel(curr);
            insertPositionLabels.add("After " + methodLabel);
            insertPositions.add(findSibling(curr, members));
        }
        insertPositions.add(null);
    }

    private String getMethodLabel(final IMethod method) {
        StringBuffer result = new StringBuffer("`");

        String[] params = method.getParameterTypes();

        result.append(method.getElementName());
        result.append("(");
        for (int i = 0; i < params.length; i++) {
            if (i != 0) {
                result.append(", ");
            }
            result.append(Signature.toString(params[i]));
        }
        result.append(")`");

        return result.toString();
    }

    private Collection<IJavaElement> filterOutExcludedElements(IJavaElement[] src, Set<IMethod> excludedElements) {

        if (excludedElements == null || excludedElements.size() == 0)
            return Arrays.asList(src);

        Collection<IJavaElement> result = new ArrayList<>();
        for (int i = 0, size = src.length; i < size; i++) {
            if (!excludedElements.contains(src[i])) {
                result.add(src[i]);
            }
        }

        return result;
    }

    private IJavaElement findSibling(final IMethod curr, final IJavaElement[] members) throws JavaModelException {
        IJavaElement res = null;
        int methodStart = curr.getSourceRange().getOffset();
        for (int i = members.length - 1; i >= 0; i--) {
            IMember member = (IMember) members[i];
            if (methodStart >= member.getSourceRange().getOffset()) {
                return res;
            }
            res = member;
        }
        return null;
    }

    @Override
    public boolean close() {
        List<Object> list = Arrays.asList(fieldViewer.getCheckedElements());
        checkedFields = list.toArray(new IField[list.size()]);

        if (currentPositionIndex == 0 || currentPositionIndex == 1) {
            settings.put(SETTINGS_INSERT_POSITION, currentPositionIndex);
        }

        if (!disableAppendSuper) {
            settings.put(SETTINGS_APPEND_SUPER, appendSuper);
        }
        settings.put(SETTINGS_GENERATE_COMMENT, generateComment);
        settings.put(SETTINGS_USE_GETTERS, useGettersInsteadOfFields);

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
        fieldSelectionLabel.setText("&Select fields to use in the generated method:");
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

        Composite optionComposite = createOptionComposite(composite);
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
        fieldViewer.setInput(fields);
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
        final StrategyIdentifier[] identifiers = possibleStrategies
                .toArray(new StrategyIdentifier[possibleStrategies.size()]);
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
        currentStrategy = preferedStrategy;
        combo.select(preferedStrategy.ordinal());
        return composite;
    }

    protected Composite createOptionComposite(final Composite composite) {
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

        final Combo combo = new Combo(composite, SWT.READ_ONLY);
        combo.setItems(insertPositionLabels.toArray(new String[insertPositionLabels.size()]));
        combo.select(currentPositionIndex);

        data = new GridData(GridData.FILL_HORIZONTAL);
        combo.setLayoutData(data);
        combo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                currentPositionIndex = combo.getSelectionIndex();
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
        gettersButton.setText("Use &getters instead of fields");
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
        return checkedFields;
    }

    public boolean getAppendSuper() {
        return appendSuper;
    }

    public boolean getGenerateComment() {
        return generateComment;
    }

    /*
     * Determine which strategy to use for the method content generation
     */
    public StrategyIdentifier getStrategyIdentifier() {
        return currentStrategy;
    }

    /*
     * Determine where in the file to enter the newly created methods.
     */
    public IJavaElement getElementPosition() {
        return insertPositions.get(currentPositionIndex);
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
        return fields;
    }
}
