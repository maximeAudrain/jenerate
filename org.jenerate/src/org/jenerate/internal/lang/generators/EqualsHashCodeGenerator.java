//$Id$
package org.jenerate.internal.lang.generators;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.jenerate.Commons4ePlugin;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.util.JavaUtils;
import org.jenerate.internal.util.PreferenceUtils;

/**
 * @author jiayun
 */
public final class EqualsHashCodeGenerator implements ILangGenerator {

    private static final ILangGenerator instance = new EqualsHashCodeGenerator();

    private EqualsHashCodeGenerator() {
    }

    public static ILangGenerator getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jiayun.commons4e.internal.lang.generators.ILangGenerator#generate(org.eclipse.swt.widgets.Shell,
     *      org.eclipse.jdt.core.IType)
     */
    public void generate(Shell parentShell, IType objectClass) {

        IMethod existingEquals = objectClass.getMethod("equals",
                new String[] { "QObject;" });
        IMethod existingHashCode = objectClass.getMethod("hashCode",
                new String[0]);
        Set excludedMethods = new HashSet();
        if (existingEquals.exists()) {
            excludedMethods.add(existingEquals);
        }
        if (existingHashCode.exists()) {
            excludedMethods.add(existingHashCode);
        }
        try {
            IField[] fields;
            if (PreferenceUtils.getDisplayFieldsOfSuperclasses()) {
                fields = JavaUtils
                        .getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass);
            } else {
                fields = JavaUtils.getNonStaticNonCacheFields(objectClass);
            }

            boolean disableAppendSuper = JavaUtils
                    .isDirectSubclassOfObject(objectClass)
                    || !JavaUtils.isEqualsOverriddenInSuperclass(objectClass)
                    || !JavaUtils.isHashCodeOverriddenInSuperclass(objectClass);

            EqualsHashCodeDialog dialog = new EqualsHashCodeDialog(parentShell,
                    "Generate Equals and HashCode", objectClass, fields,
                    excludedMethods, disableAppendSuper);
            int returnCode = dialog.open();
            if (returnCode == Window.OK) {

                if (existingEquals.exists()) {
                    existingEquals.delete(true, null);
                }
                if (existingHashCode.exists()) {
                    existingHashCode.delete(true, null);
                }

                IField[] checkedFields = dialog.getCheckedFields();
                IJavaElement insertPosition = dialog.getElementPosition();
                boolean appendSuper = dialog.getAppendSuper();
                boolean generateComment = dialog.getGenerateComment();
                boolean compareReferences = dialog.getCompareReferences();
                boolean useGettersInsteadOfFields = dialog.getUseGettersInsteadOfFields();
                boolean useBlocksInIfStatements = dialog.getUseBlockInIfStatements();
                IInitMultNumbers imNumbers = dialog.getInitMultNumbers();

                IJavaElement created = generateHashCode(parentShell,
                        objectClass, checkedFields, insertPosition,
                        appendSuper, generateComment, imNumbers,
                        useGettersInsteadOfFields);

                created = generateEquals(parentShell, objectClass,
                        checkedFields, created, appendSuper, generateComment,
                        compareReferences, useGettersInsteadOfFields,
                        useBlocksInIfStatements);

                ICompilationUnit cu = objectClass.getCompilationUnit();
                IEditorPart javaEditor = JavaUI.openInEditor(cu);
                JavaUI.revealInEditor(javaEditor, (IJavaElement) created);
            }

        } catch (CoreException e) {
            MessageDialog.openError(parentShell, "Method Generation Failed", e
                    .getMessage());
        }

    }

    private IJavaElement generateEquals(final Shell parentShell,
            final IType objectClass, final IField[] checkedFields,
            final IJavaElement insertPosition, final boolean appendSuper,
            final boolean generateComment, final boolean compareReferences,
            final boolean useGettersInsteadOfFields,
            final boolean useBlocksInIfStatements)
                    throws PartInitException, JavaModelException {

        boolean addOverride = PreferenceUtils.getAddOverride()
                && PreferenceUtils
                        .isSourceLevelGreaterThanOrEqualTo5(objectClass
                                .getJavaProject());

        String source = createEqualsMethod(objectClass, checkedFields,
                appendSuper, generateComment, compareReferences, addOverride,
                useGettersInsteadOfFields, useBlocksInIfStatements);

        String formattedContent = JavaUtils.formatCode(parentShell,
                objectClass, source);

        objectClass.getCompilationUnit().createImport(
                CommonsLangLibraryUtils.getEqualsBuilderLibrary(), null, null);
        IJavaElement created = objectClass.createMethod(formattedContent,
                insertPosition, true, null);

        return created;
    }

    private String createEqualsMethod(final IType objectClass,
            final IField[] checkedFields, final boolean appendSuper,
            final boolean generateComment, final boolean compareReferences,
            final boolean addOverride, final boolean useGettersInsteadOfFields,
            final boolean useBlocksInIfStatements) throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (generateComment) {
            content.append("/* (non-Javadoc)\n");
            content
                    .append(" * @see java.lang.Object#equals(java.lang.Object)\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public boolean equals(final Object other) {\n");
        if (compareReferences) {
            content.append("if (this == other)");
            content.append(useBlocksInIfStatements ? "{\n" : "");
            content.append(" return true;");
            content.append(useBlocksInIfStatements ? "\n}\n" : "");
        }
        content.append("if ( !(other instanceof ");
        content.append(objectClass.getElementName());
        content.append(") )");
        content.append(useBlocksInIfStatements ? "{\n" : "");
        content.append(" return false;");
        content.append(useBlocksInIfStatements ? "\n}\n" : "");
        content.append(objectClass.getElementName());
        content.append(" castOther = (");
        content.append(objectClass.getElementName());
        content.append(") other;\n");
        content.append("return new EqualsBuilder()");
        if (appendSuper) {
            content.append(".appendSuper(super.equals(other))");
        }
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(JavaUtils.generateFieldAccessor(checkedFields[i], useGettersInsteadOfFields));
            content.append(", castOther.");
            content.append(JavaUtils.generateFieldAccessor(checkedFields[i], useGettersInsteadOfFields));
            content.append(")");
        }
        content.append(".isEquals();\n");
        content.append("}\n\n");

        return content.toString();
    }

    private IJavaElement generateHashCode(final Shell parentShell,
            final IType objectClass, final IField[] checkedFields,
            final IJavaElement insertPosition, final boolean appendSuper,
            final boolean generateComment, final IInitMultNumbers imNumbers,
            final boolean useGettersInsteadOfFields)
            throws PartInitException, JavaModelException {

        boolean isCacheable = PreferenceUtils.getCacheHashCode()
                && JavaUtils.areAllFinalFields(checkedFields);

        boolean addOverride = PreferenceUtils.getAddOverride()
                && PreferenceUtils
                        .isSourceLevelGreaterThanOrEqualTo5(objectClass
                                .getJavaProject());

        String source = createHashCodeMethod(objectClass, checkedFields,
                appendSuper, generateComment, imNumbers, isCacheable,
                addOverride, useGettersInsteadOfFields);

        String formattedContent = JavaUtils.formatCode(parentShell,
                objectClass, source);

        objectClass.getCompilationUnit().createImport(
                CommonsLangLibraryUtils.getHashCodeBuilderLibrary(), null, null);
        IJavaElement created = objectClass.createMethod(formattedContent,
                insertPosition, true, null);

        String cachingField = PreferenceUtils.getHashCodeCachingField();
        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient int " + cachingField + ";\n\n";
            String formattedFieldSrc = JavaUtils.formatCode(parentShell,
                    objectClass, fieldSrc);
            created = objectClass.createField(formattedFieldSrc, created, true,
                    null);
        }

        return created;
    }

    private String createHashCodeMethod(final IType objectClass,
            final IField[] checkedFields, final boolean appendSuper,
            final boolean generateComment, final IInitMultNumbers imNumbers,
            final boolean isCacheable, final boolean addOverride,
            final boolean useGettersInsteadOfFields) throws JavaModelException {

        StringBuffer content = new StringBuffer();
        if (generateComment) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#hashCode()\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public int hashCode() {\n");
        if (isCacheable) {
            String cachingField = PreferenceUtils.getHashCodeCachingField();
            content.append("if (" + cachingField + "== 0) {\n");
            content.append(cachingField + " = ");
            content.append(createHashCodeBuilderString(checkedFields,
                    appendSuper, imNumbers, useGettersInsteadOfFields));
            content.append("}\n");
            content.append("return " + cachingField + ";\n");

        } else {
            content.append("return ");
            content.append(createHashCodeBuilderString(checkedFields,
                    appendSuper, imNumbers, useGettersInsteadOfFields));
        }
        content.append("}\n\n");

        return content.toString();
    }

    private String createHashCodeBuilderString(final IField[] checkedFields,
            final boolean appendSuper, final IInitMultNumbers imNumbers,
            final boolean useGettersInsteadOfFields) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        content.append("new HashCodeBuilder(");
        content.append(imNumbers.getValue());
        content.append(")");
        if (appendSuper) {
            content.append(".appendSuper(super.hashCode())");
        }
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(JavaUtils.generateFieldAccessor(checkedFields[i], useGettersInsteadOfFields));
            content.append(")");
        }
        content.append(".toHashCode();\n");

        return content.toString();
    }

    private static class EqualsHashCodeDialog extends FieldDialog {

        private boolean compareReferences;

        private Button imButtons[] = new Button[3];

        private Text initText;

        private Text multText;

        private IInitMultNumbers imNumbers[] = new IInitMultNumbers[] {
                new DefaultInitMultNumbers(), new RandomInitMultNumbers(),
                new CustomInitMultNumbers() };

        private int initMultType;

        private int initialNumber;

        private int multiplierNumber;

        private IDialogSettings equalsSettings;

        private IDialogSettings hashCodeSettings;

        private static final String EQUALS_SETTINGS_SECTION = "EqualsDialog";

        private static final String HASHCODE_SETTINGS_SECTION = "HashCodeDialog";

        private static final String SETTINGS_COMPARE_REFERENCES = "CompareReferences";

        private static final String SETTINGS_INIT_MULT_TYPE = "InitMultType";

        private static final String SETTINGS_INITIAL_NUMBER = "InitialNumber";

        private static final String SETTINGS_MULTIPLIER_NUMBER = "MultiplierNumber";

        public EqualsHashCodeDialog(final Shell parentShell,
                final String dialogTitle, final IType objectClass,
                final IField[] fields, final Set excludedMethods,
                final boolean disableAppendSuper) throws JavaModelException {

            super(parentShell, dialogTitle, objectClass, fields,
                    excludedMethods, disableAppendSuper);

            IDialogSettings dialogSettings = Commons4ePlugin.getDefault()
                    .getDialogSettings();
            equalsSettings = dialogSettings.getSection(EQUALS_SETTINGS_SECTION);
            if (equalsSettings == null) {
                equalsSettings = dialogSettings
                        .addNewSection(EQUALS_SETTINGS_SECTION);
            }

            compareReferences = equalsSettings
                    .getBoolean(SETTINGS_COMPARE_REFERENCES);

            hashCodeSettings = dialogSettings
                    .getSection(HASHCODE_SETTINGS_SECTION);
            if (hashCodeSettings == null) {
                hashCodeSettings = dialogSettings
                        .addNewSection(HASHCODE_SETTINGS_SECTION);
            }

            try {
                initMultType = hashCodeSettings.getInt(SETTINGS_INIT_MULT_TYPE);
            } catch (NumberFormatException e) {
                initMultType = 0;
            }

            try {
                initialNumber = hashCodeSettings
                        .getInt(SETTINGS_INITIAL_NUMBER);
            } catch (NumberFormatException e) {
                initialNumber = 17;
            }

            try {
                multiplierNumber = hashCodeSettings
                        .getInt(SETTINGS_MULTIPLIER_NUMBER);
            } catch (NumberFormatException e) {
                multiplierNumber = 37;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.window.Window#close()
         */
        public boolean close() {
            equalsSettings.put(SETTINGS_COMPARE_REFERENCES, compareReferences);
            imNumbers[initMultType].setNumbers(initialNumber, multiplierNumber);
            hashCodeSettings.put(SETTINGS_INIT_MULT_TYPE, initMultType);
            hashCodeSettings.put(SETTINGS_INITIAL_NUMBER, initialNumber);
            hashCodeSettings.put(SETTINGS_MULTIPLIER_NUMBER, multiplierNumber);
            return super.close();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.window.Window#create()
         */
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

        /*
         * (non-Javadoc)
         * 
         * @see org.jiayun.commons4e.internal.ui.dialogs.FieldDialog#createOptionComposite(org.eclipse.swt.widgets.Composite)
         */
        protected Composite createOptionComposite(Composite composite) {
            Composite optionComposite = super.createOptionComposite(composite);
            addCompareReferencesOption(optionComposite);
            addInitialMultiplierOptions(optionComposite);
            return optionComposite;
        }

        private Composite addCompareReferencesOption(final Composite composite) {
            Group group = new Group(composite, SWT.NONE);
            group.setText("Equals");
            group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
            GridLayout layout = new GridLayout(1, false);
            group.setLayout(layout);

            Button button = new Button(group, SWT.CHECK);
            button.setText("Compare object &references");
            button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

            button.addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent e) {
                    compareReferences = (((Button) e.widget).getSelection());
                }

                public void widgetDefaultSelected(SelectionEvent e) {
                    widgetSelected(e);
                }
            });
            button.setSelection(compareReferences);

            return composite;
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

                public void modifyText(ModifyEvent e) {
                    checkInput();
                }

            });
            data = new GridData(GridData.FILL_HORIZONTAL);
            multText.setLayoutData(data);

            imButtons[0].addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    Button button = (Button) e.widget;
                    if (button.getSelection()) {
                        initMultType = 0;
                    }
                }
            });

            imButtons[1].addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    Button button = (Button) e.widget;
                    if (button.getSelection()) {
                        initMultType = 1;
                    }
                }
            });

            imButtons[2].addSelectionListener(new SelectionAdapter() {

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
            messageLabel.setImage(JFaceResources
                    .getImage(Dialog.DLG_IMG_MESSAGE_ERROR));
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

        public boolean getCompareReferences() {
            return compareReferences;
        }

        public IInitMultNumbers getInitMultNumbers() {
            return imNumbers[initMultType];
        }
    }

    private static interface IInitMultNumbers {

        void setNumbers(int initial, int multiplier);

        String getValue();
    }

    private static class DefaultInitMultNumbers implements IInitMultNumbers {

        public void setNumbers(int initial, int multiplier) {
        }

        public String getValue() {
            return "";
        }

    }

    private static class RandomInitMultNumbers implements IInitMultNumbers {

        private static Random random = new Random();

        public void setNumbers(int initial, int multiplier) {
        }

        public String getValue() {

            int initial = random.nextInt();
            int multiplier = random.nextInt();

            initial = initial % 2 == 0 ? initial + 1 : initial;
            multiplier = multiplier % 2 == 0 ? multiplier + 1 : multiplier;

            return String.valueOf(initial) + ", " + String.valueOf(multiplier);
        }

    }

    private static class CustomInitMultNumbers implements IInitMultNumbers {

        int initial;

        int multiplier;

        public void setNumbers(int initial, int multiplier) {
            this.initial = initial;
            this.multiplier = multiplier;
        }

        public String getValue() {
            return String.valueOf(initial) + ", " + String.valueOf(multiplier);
        }

    }

    private static class IntegerVerifyListener implements VerifyListener {

        private Text inputText;

        public IntegerVerifyListener(Text inputText) {
            super();
            this.inputText = inputText;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
         */
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
