//$Id$
package org.jenerate.internal.ui.dialogs;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/*
 * This class contains some code from
 *      org.eclipse.ui.externaltools.internal.ui.BuilderPropertyPage
 */
/**
 * @author jiayun
 */
public class OrderableFieldDialog extends FieldDialog {

    private Button upButton;

    private Button downButton;

    public OrderableFieldDialog(Shell parentShell, String dialogTitle,
            IType objectClass, IField[] fields, Set<IMethod> excludedMethods)
            throws JavaModelException {
        this(parentShell, dialogTitle, objectClass, fields, excludedMethods,
                false);
    }

    public OrderableFieldDialog(Shell parentShell, String dialogTitle,
            IType objectClass, IField[] fields, Set<IMethod> excludedMethods,
            boolean disableAppendSuper) throws JavaModelException {
        super(parentShell, dialogTitle, objectClass, fields, excludedMethods,
                disableAppendSuper);
    }

    public void create() {
        super.create();

        Table fieldTable = fieldViewer.getTable();
        fieldTable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleTableSelectionChanged();
            }
        });
    }

    private void handleTableSelectionChanged() {
        Table fieldTable = fieldViewer.getTable();
        TableItem[] items = fieldTable.getSelection();
        boolean validSelection = items != null && items.length > 0;
        boolean enableUp = validSelection;
        boolean enableDown = validSelection;
        if (validSelection) {
            int indices[] = fieldTable.getSelectionIndices();
            int max = fieldTable.getItemCount();
            enableUp = indices[0] != 0;
            enableDown = indices[indices.length - 1] < max - 1;
        }
        upButton.setEnabled(enableUp);
        downButton.setEnabled(enableDown);
    }

    protected void addButtons(final Composite buttonComposite) {
        super.addButtons(buttonComposite);

        GridData data;
        upButton = new Button(buttonComposite, SWT.PUSH);
        upButton.setText("&Up");
        upButton.setEnabled(false);
        upButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                moveSelectionUp();
                handleTableSelectionChanged();
            }
        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        upButton.setLayoutData(data);

        downButton = new Button(buttonComposite, SWT.PUSH);
        downButton.setText("Do&wn");
        downButton.setEnabled(false);
        downButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                moveSelectionDown();
                handleTableSelectionChanged();
            }
        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        downButton.setLayoutData(data);
    }

    /**
     * Move the current selection in the field list up.
     */
    private void moveSelectionUp() {
        Table builderTable = fieldViewer.getTable();
        int indices[] = builderTable.getSelectionIndices();
        int newSelection[] = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            if (index > 0) {
                move(builderTable.getItem(index), index - 1);
                newSelection[i] = index - 1;
            }
        }
        builderTable.setSelection(newSelection);
    }

    /**
     * Move the current selection in the field list down.
     */
    private void moveSelectionDown() {
        Table builderTable = fieldViewer.getTable();
        int indices[] = builderTable.getSelectionIndices();
        if (indices.length < 1) {
            return;
        }
        int newSelection[] = new int[indices.length];
        int max = builderTable.getItemCount() - 1;
        for (int i = indices.length - 1; i >= 0; i--) {
            int index = indices[i];
            if (index < max) {
                move(builderTable.getItem(index), index + 1);
                newSelection[i] = index + 1;
            }
        }
        builderTable.setSelection(newSelection);
    }

    /**
     * Moves an entry in the field table to the given index.
     */
    private void move(TableItem item, int index) {
        Object data = item.getData();
        boolean checked = fieldViewer.getChecked(data);
        item.dispose();
        fieldViewer.insert(data, index);
        fieldViewer.setChecked(data, checked);
    }
}