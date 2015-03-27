package org.jenerate.internal.lang.generators;

import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jface.window.Window;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangMethodContentLibraries;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.jenerate.internal.ui.dialogs.impl.CompareToDialog;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link CompareToGenerator}. XXX to be refactored with the new strategy
 * 
 * @author maudrain
 */
@Ignore
public class CompareToGeneratorTest extends AbstractGeneratorTest {

    private static final String TEST_ELEMENT_NAME = "Test";
    private static final String COMPARE_TO_FIELD_NAME = "compareTo";
    @Mock
    private IMethod compareToMethod;
    @Mock
    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;
    @Mock
    private DialogFactory<CompareToDialog, CompareToGenerationData> dialogProvider;
    @Mock
    private CompareToDialog fieldDialog;
    @Mock
    private CompareToGenerationData data;

    private CompareToGenerator compareToGenerator;

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockSpecificObjectClass();
        mockIsOverriddenInSuperclass(true);
        mockJavaInterfaceCodeAppender();
        mockCommonFieldDialog();
        mockSpecificPreferencesManager();
        mockCompareToNonGenericMethodExists(false);
        mockCompareToGenericMethodExists(false);
        when(objectClass.createMethod(FORMATTED_CODE_1, elementPosition, true, null)).thenReturn(createdMethod1);
        when(dialogProvider.createDialog(parentShell, objectClass, Collections.<IMethod> emptySet())).thenReturn(
                fieldDialog);

        compareToGenerator = new CompareToGenerator(javaUiCodeAppender, preferencesManager, dialogProvider,
                jeneratePluginCodeFormatter, generatorsCommonMethodsDelegate, javaInterfaceCodeAppender);
    }

    @Test
    public void verifyGeneratedCodeDefault() throws Exception {
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeCompareToNonGenericMethodExists() throws Exception {
        mockCompareToNonGenericMethodExists(true);
        when(dialogProvider.createDialog(parentShell, objectClass, Collections.singleton(compareToMethod))).thenReturn(
                fieldDialog);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeCompareToGenericMethodExists() throws Exception {
        mockCompareToGenericMethodExists(true);
        when(dialogProvider.createDialog(parentShell, objectClass, Collections.singleton(compareToMethod))).thenReturn(
                fieldDialog);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX Move test to dialog provider
     */
    @Ignore
    @Test
    public void verifyGeneratedCodeIsImplementedInSupertypeDisableAppendSuper() throws Exception {
        when(javaInterfaceCodeAppender.isImplementedInSupertype(objectClass, "Comparable")).thenReturn(false);
        // when(
        // dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, true,
        // preferencesManager)).thenReturn(fieldDialog);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX Move test to dialog provider
     */
    @Ignore
    @Test
    public void verifyGeneratedCodeIsOverridenInSuperclassDisableAppendSuper() throws Exception {
        mockIsOverriddenInSuperclass(true);
        // when(
        // dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, true,
        // preferencesManager)).thenReturn(fieldDialog);
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
    public void verifyGeneratedCodeWithAddGenericInterface() throws Exception {
        when(javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable")).thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).thenReturn(true);
        when(generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verify(javaInterfaceCodeAppender, times(1)).addSuperInterface(objectClass,
                "Comparable<" + TEST_ELEMENT_NAME + ">");
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithAddNonGenericInterface() throws Exception {
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
    public void verifyGeneratedCodeWithCommonsLang3() throws Exception {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(true);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithComment() throws Exception {
        when(data.getGenerateComment()).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithAppendSuper() throws Exception {
        when(data.getAppendSuper()).thenReturn(true);
        compareToGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    private void verifyCodeAppended(boolean useCommonsLang3) throws Exception {
        verify(compilationUnit, times(1)).createImport(
                CommonsLangMethodContentLibraries.getCompareToBuilderLibrary(useCommonsLang3), null, null);
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
