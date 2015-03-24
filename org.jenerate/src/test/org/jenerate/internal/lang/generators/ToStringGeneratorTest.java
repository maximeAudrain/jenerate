package org.jenerate.internal.lang.generators;

import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.ui.dialogs.ToStringDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ToStringGenerator}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class ToStringGeneratorTest {

    private static final String TO_STRING_FIELD_NAME = "toString";
    private static final String FORMATTED_CODE = "formatted_code";
    private static final String TO_STRING_STYLE = "TO.STRING.STYLE";
    @Mock
    private PreferencesManager preferencesManager;
    @Mock
    private JavaUiCodeAppender javaUiCodeAppender;
    @Mock
    private DialogProvider<ToStringDialog> dialogProvider;
    @Mock
    private JeneratePluginCodeFormatter jeneratePluginCodeFormatter;
    @Mock
    private GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    @Mock
    private Shell parentShell;
    @Mock
    private IType objectClass;

    @Mock
    private IField field1;
    @Mock
    private IField field2;

    @Mock
    private IJavaElement iJavaElement;

    @Mock
    private ToStringDialog fieldDialog;
    @Mock
    private ICompilationUnit compilationUnit;
    @Mock
    private IMethod createdMethod;
    @Mock
    private IMethod toStringMethod;
    @Mock
    private IField toStringCachingField;
    @Mock
    private JavaModelException javaModelException;

    private IField[] fields = new IField[] { field1, field2 };

    private ToStringGenerator toStringGenerator;

    @Before
    public void SetUp() throws RuntimeException, CoreException, BadLocationException {
        mockObjectClass();
        mockPreferencesManager();
        mockFieldDialog();
        mockJeneratePluginCodeFormatter();
        mockGeneratorsCommonMethodsDelegate();
        mockToStringMethodExists(false);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, true,
                        preferencesManager)).thenReturn(fieldDialog);
        toStringGenerator = new ToStringGenerator(javaUiCodeAppender, preferencesManager, dialogProvider,
                jeneratePluginCodeFormatter, generatorsCommonMethodsDelegate);

    }

    @Test
    public void verifyGeneratedCodeDefault() throws RuntimeException, CoreException {
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeToStringExists() throws RuntimeException, CoreException {
        mockToStringMethodExists(true);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.singleton(toStringMethod), fields, true,
                        preferencesManager)).thenReturn(fieldDialog);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithDisplayedFieldsOfSuperclass() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.DISPLAY_FIELDS_OF_SUPERCLASSES))
                .thenReturn(true);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithOverridenInSuperclass() throws RuntimeException, CoreException {
        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, TO_STRING_FIELD_NAME,
                        new String[0], null)).thenReturn(true);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, false,
                        preferencesManager)).thenReturn(fieldDialog);
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

        toStringGenerator.generate(parentShell, objectClass);

        verify(toStringCachingField, times(1)).delete(true, null);
        verify(objectClass, times(1)).createField(FORMATTED_CODE, createdMethod, true, null);
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
        when(fieldDialog.getGenerateComment()).thenReturn(true);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithAppendSuper() throws RuntimeException, CoreException {
        when(fieldDialog.getAppendSuper()).thenReturn(true);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }
    
    @Test
    public void verifyGeneratedCodeWithNoStyleConstant() throws RuntimeException, CoreException {
        when(fieldDialog.getToStringStyle()).thenReturn("");
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX message dialog are currently untestable. Find a solution at some point, probably delegation and verify.
     */
    @Ignore
    @Test
    public void verifyGeneratedCodeFormatFails() throws RuntimeException, CoreException {
        when(generatorsCommonMethodsDelegate.getNonStaticNonCacheFields(objectClass, preferencesManager)).thenThrow(
                javaModelException);
        toStringGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(true);
    }

    private void verifyCodeAppended(boolean useCommonsLang3) throws JavaModelException, PartInitException {
        verify(compilationUnit, times(1)).createImport(
                CommonsLangLibraryUtils.getToStringBuilderLibrary(useCommonsLang3), null, null);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, createdMethod);
    }

    /**
     * XXX TO BE REVIEWED : here is a trick that the first time format is called, the passed argument is returned, and
     * the second time {@link ToStringGeneratorTest#FORMATTED_CODE} is returned. This is done to verify that the to
     * string field is created in case it is required.
     */
    private void mockJeneratePluginCodeFormatter() throws JavaModelException, BadLocationException {
        when(jeneratePluginCodeFormatter.formatCode(any(IType.class), anyString())).then(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                String generatedCode = invocation.getArgumentAt(1, String.class);
                when(objectClass.createMethod(generatedCode, iJavaElement, true, null)).thenReturn(createdMethod);
                return generatedCode;
            }
        }).thenReturn(FORMATTED_CODE);
    }

    private void mockPreferencesManager() {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.DISPLAY_FIELDS_OF_SUPERCLASSES))
                .thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.TOSTRING_CACHING_FIELD)).thenReturn(
                TO_STRING_FIELD_NAME);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.CACHE_TOSTRING)).thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION))
                .thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).thenReturn(false);
    }

    private void mockGeneratorsCommonMethodsDelegate() throws JavaModelException {
        when(generatorsCommonMethodsDelegate.getNonStaticNonCacheFields(objectClass, preferencesManager)).thenReturn(
                fields);
        when(
                generatorsCommonMethodsDelegate.getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(
                        objectClass, preferencesManager)).thenReturn(fields);
        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, TO_STRING_FIELD_NAME,
                        new String[0], null)).thenReturn(false);
        when(generatorsCommonMethodsDelegate.areAllFinalFields(fields)).thenReturn(false);
        when(generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)).thenReturn(false);
    }

    private void mockFieldDialog() {
        when(fieldDialog.open()).thenReturn(Window.OK);
        when(fieldDialog.getToStringStyle()).thenReturn(TO_STRING_STYLE);
        when(fieldDialog.getCheckedFields()).thenReturn(fields);
        when(fieldDialog.getElementPosition()).thenReturn(iJavaElement);
        when(fieldDialog.getGenerateComment()).thenReturn(false);
        when(fieldDialog.getAppendSuper()).thenReturn(false);
    }

    private void mockObjectClass() throws CoreException {
        when(toStringCachingField.exists()).thenReturn(false);
        when(objectClass.getField("")).thenReturn(toStringCachingField);

        when(field1.getElementName()).thenReturn("field1");
        when(field2.getElementName()).thenReturn("field2");
        fields = new IField[] { field1, field2 };
        when(objectClass.getFields()).thenReturn(fields);

        when(objectClass.getCompilationUnit()).thenReturn(compilationUnit);
    }

    private void mockToStringMethodExists(boolean exists) {
        when(toStringMethod.exists()).thenReturn(exists);
        when(objectClass.getMethod(TO_STRING_FIELD_NAME, new String[0])).thenReturn(toStringMethod);
    }

}
