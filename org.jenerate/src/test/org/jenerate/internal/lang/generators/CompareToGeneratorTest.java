package org.jenerate.internal.lang.generators;

import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.window.Window;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.data.CompareToDialogData;
import org.jenerate.internal.ui.dialogs.CompareToDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link CompareToGenerator}
 * 
 * @author maudrain
 */
public class CompareToGeneratorTest extends AbstractGeneratorTest {

    private static final String TEST_ELEMENT_NAME = "Test";
    private static final String COMPARE_TO_FIELD_NAME = "compareTo";
    @Mock
    private IMethod compareToMethod;
    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;
    @Mock
    private DialogProvider<CompareToDialog, CompareToDialogData> dialogProvider;
    @Mock
    private CompareToDialog fieldDialog;
    @Mock
    private CompareToDialogData data;

    private CompareToGenerator compareToGenerator;

    @Override
    public void callbackAfterSetUp() throws CoreException, BadLocationException {
        mockSpecificObjectClass();
        mockIsOverriddenInSuperclass(true);
        mockJavaInterfaceCodeAppender();
        mockCommonFieldDialog();
        mockSpecificPreferencesManager();
        mockCompareToNonGenericMethodExists(false);
        mockCompareToGenericMethodExists(false);
        when(objectClass.createMethod(FORMATTED_CODE_1, elementPosition, true, null)).thenReturn(createdMethod1);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, false,
                        preferencesManager)).thenReturn(fieldDialog);

        compareToGenerator = new CompareToGenerator(javaUiCodeAppender, preferencesManager, dialogProvider,
                jeneratePluginCodeFormatter, generatorsCommonMethodsDelegate, javaInterfaceCodeAppender);
    }

    @Test
    public void verifyGeneratedCodeDefault() throws PartInitException, JavaModelException {
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeCompareToNonGenericMethodExists() throws PartInitException, JavaModelException {
        mockCompareToNonGenericMethodExists(true);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.singleton(compareToMethod), fields,
                        false, preferencesManager)).thenReturn(fieldDialog);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeCompareToGenericMethodExists() throws PartInitException, JavaModelException {
        mockCompareToGenericMethodExists(true);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.singleton(compareToMethod), fields,
                        false, preferencesManager)).thenReturn(fieldDialog);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeIsImplementedInSupertypeDisableAppendSuper() throws PartInitException,
            JavaModelException {
        when(javaInterfaceCodeAppender.isImplementedInSupertype(objectClass, "Comparable")).thenReturn(false);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, true,
                        preferencesManager)).thenReturn(fieldDialog);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeIsOverridenInSuperclassDisableAppendSuper() throws PartInitException,
            JavaModelException {
        mockIsOverriddenInSuperclass(true);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, true,
                        preferencesManager)).thenReturn(fieldDialog);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void testDialogCancelPressed() throws RuntimeException {
        when(fieldDialog.open()).thenReturn(Window.CANCEL);
        compareToGenerator.generate(parentShell, objectClass);
        verifyNoMoreInteractions(javaUiCodeAppender, compilationUnit);
    }

    @Test
    public void verifyGeneratedCodeWithAddGenericInterface() throws PartInitException, JavaModelException,
            MalformedTreeException, InvalidInputException {
        when(javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable")).thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).thenReturn(true);
        when(generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verify(javaInterfaceCodeAppender, times(1)).addSuperInterface(objectClass,
                "Comparable<" + TEST_ELEMENT_NAME + ">");
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithAddNonGenericInterface() throws PartInitException, JavaModelException,
            MalformedTreeException, InvalidInputException {
        when(javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable")).thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).thenReturn(false);
        when(generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verify(javaInterfaceCodeAppender, times(1)).addSuperInterface(objectClass, "Comparable");
        verifyCodeAppended(false);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithCommonsLang3() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(true);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithComment() throws RuntimeException, CoreException {
        when(data.getGenerateComment()).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithAppendSuper() throws RuntimeException, CoreException {
        when(data.getAppendSuper()).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    private void verifyCodeAppended(boolean useCommonsLang3) throws JavaModelException, PartInitException {
        verify(compilationUnit, times(1)).createImport(
                CommonsLangLibraryUtils.getCompareToBuilderLibrary(useCommonsLang3), null, null);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, createdMethod1);
    }

    private void mockCompareToNonGenericMethodExists(boolean exists) {
        when(compareToMethod.exists()).thenReturn(exists);
        when(objectClass.getMethod(COMPARE_TO_FIELD_NAME, new String[] { "QObject;" })).thenReturn(compareToMethod);
    }

    private void mockCompareToGenericMethodExists(boolean exists) {
        when(compareToMethod.exists()).thenReturn(exists);
        when(objectClass.getMethod(COMPARE_TO_FIELD_NAME, new String[] { "Q" + TEST_ELEMENT_NAME + ";" })).thenReturn(
                compareToMethod);
    }

    private void mockSpecificObjectClass() {
        when(objectClass.getElementName()).thenReturn(TEST_ELEMENT_NAME);
    }

    private void mockIsOverriddenInSuperclass(boolean isOverriden) throws JavaModelException {
        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "compareTo",
                        new String[] { "QObject;" }, null)).thenReturn(isOverriden);
    }

    private void mockJavaInterfaceCodeAppender() throws JavaModelException {
        when(javaInterfaceCodeAppender.isImplementedInSupertype(objectClass, "Comparable")).thenReturn(true);
        when(javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable")).thenReturn(true);
    }

    private void mockSpecificPreferencesManager() {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).thenReturn(false);
    }

    private void mockCommonFieldDialog() {
        when(fieldDialog.open()).thenReturn(Window.OK);
        when(fieldDialog.getData()).thenReturn(data);
        when(data.getCheckedFields()).thenReturn(fields);
        when(data.getElementPosition()).thenReturn(elementPosition);
        when(data.getGenerateComment()).thenReturn(false);
        when(data.getAppendSuper()).thenReturn(false);
        when(data.getUseGettersInsteadOfFields()).thenReturn(false);
    }
}
