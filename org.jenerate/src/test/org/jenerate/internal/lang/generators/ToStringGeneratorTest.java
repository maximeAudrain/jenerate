package org.jenerate.internal.lang.generators;

import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.data.ToStringDialogData;
import org.jenerate.internal.domain.method.content.CommonsLangLibraryUtils;
import org.jenerate.internal.domain.method.content.tostring.ToStringStyle;
import org.jenerate.internal.ui.dialogs.ToStringDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ToStringGenerator} XXX to be refactored with the new strategy
 * 
 * @author maudrain
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ToStringGeneratorTest extends AbstractGeneratorTest {

    private static final String TO_STRING_FIELD_NAME = "toString";
    private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.DEFAULT_STYLE;

    @Mock
    private DialogProvider<ToStringDialog, ToStringDialogData> dialogProvider;
    @Mock
    private ToStringDialog fieldDialog;
    @Mock
    private ToStringDialogData data;
    @Mock
    private IMethod toStringMethod;
    @Mock
    private IField toStringCachingField;
    @Mock
    private JavaModelException javaModelException;

    private ToStringGenerator toStringGenerator;

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockSpecificPreferencesManager();
        mockSpecificObjectClass();
        mockSpecificGeneratorsCommonMethodsDelegate();
        mockCommonFieldDialog();
        mockSpecificFieldDialog();
        mockToStringMethodExists(false);
        when(objectClass.createMethod(FORMATTED_CODE_1, elementPosition, true, null)).thenReturn(createdMethod1);
        when(dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet())).thenReturn(
                fieldDialog);
        toStringGenerator = new ToStringGenerator(javaUiCodeAppender, preferencesManager, dialogProvider,
                jeneratePluginCodeFormatter, generatorsCommonMethodsDelegate);
    }

    @Test
    public void verifyGeneratedCodeDefault() throws RuntimeException, CoreException {
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeToStringExists() throws Exception {
        mockToStringMethodExists(true);
        when(dialogProvider.getDialog(parentShell, objectClass, Collections.singleton(toStringMethod))).thenReturn(
                fieldDialog);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX Move test to dialog provider
     */
    @Ignore
    @Test
    public void verifyGeneratedCodeWithOverridenInSuperclass() throws RuntimeException, CoreException {
        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, TO_STRING_FIELD_NAME,
                        new String[0], null)).thenReturn(true);
        // when(
        // dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, false,
        // preferencesManager)).thenReturn(fieldDialog);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void testDialogCancelPressed() throws RuntimeException {
        when(fieldDialog.open()).thenReturn(Window.CANCEL);
        toStringGenerator.generate(parentShell, objectClass);
        verifyNoMoreInteractions(javaUiCodeAppender, compilationUnit);
    }

    @Test
    public void verifyGeneratedCodeWithCachedToString() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.CACHE_TOSTRING)).thenReturn(Boolean.TRUE);
        when(generatorsCommonMethodsDelegate.areAllFinalFields(fields)).thenReturn(true);

        when(toStringCachingField.exists()).thenReturn(true);
        when(objectClass.getField(TO_STRING_FIELD_NAME)).thenReturn(toStringCachingField);
        when(objectClass.createMethod(FORMATTED_CODE_2, elementPosition, true, null)).thenReturn(createdMethod1);
        toStringGenerator.generate(parentShell, objectClass);

        verify(toStringCachingField, times(1)).delete(true, null);
        verify(objectClass, times(1)).createField("private transient String " + TO_STRING_FIELD_NAME + ";\n\n",
                elementPosition, true, null);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithOverrideAnnotation() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION)).thenReturn(true);
        when(generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)).thenReturn(true);

        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithCommonsLang3() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).thenReturn(true);

        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(true);
    }

    @Test
    public void verifyGeneratedCodeWithComment() throws RuntimeException, CoreException {
        when(data.getGenerateComment()).thenReturn(true);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithAppendSuper() throws RuntimeException, CoreException {
        when(data.getAppendSuper()).thenReturn(true);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithNoStyleConstant() throws RuntimeException, CoreException {
        when(data.getToStringStyle()).thenReturn(ToStringStyle.NO_STYLE);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithGettersInsteadOfFields() throws RuntimeException, CoreException {
        when(data.getUseGettersInsteadOfFields()).thenReturn(true);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX message dialog are currently untestable. Find a solution at some point, probably delegation and verify.
     */
    @Ignore
    @Test
    public void verifyGeneratedCodeFormatFails() throws RuntimeException, CoreException {
        when(generatorsCommonMethodsDelegate.getObjectClassFields(objectClass, preferencesManager)).thenThrow(
                javaModelException);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(true);
    }

    private void verifyCodeAppended(boolean useCommonsLang3) throws JavaModelException, PartInitException {
        verify(compilationUnit, times(1)).createImport(
                CommonsLangLibraryUtils.getToStringBuilderLibrary(useCommonsLang3), null, null);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, createdMethod1);
    }

    private void mockSpecificPreferencesManager() {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.TOSTRING_CACHING_FIELD)).thenReturn(
                TO_STRING_FIELD_NAME);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.CACHE_TOSTRING)).thenReturn(false);
    }

    private void mockSpecificObjectClass() {
        when(toStringCachingField.exists()).thenReturn(false);
        when(objectClass.getField("")).thenReturn(toStringCachingField);
    }

    private void mockSpecificGeneratorsCommonMethodsDelegate() throws JavaModelException {
        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, TO_STRING_FIELD_NAME,
                        new String[0], null)).thenReturn(false);
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

    private void mockSpecificFieldDialog() {
        when(data.getToStringStyle()).thenReturn(TO_STRING_STYLE);
    }

    private void mockToStringMethodExists(boolean exists) {
        when(toStringMethod.exists()).thenReturn(exists);
        when(objectClass.getMethod(TO_STRING_FIELD_NAME, new String[0])).thenReturn(toStringMethod);
    }
}
