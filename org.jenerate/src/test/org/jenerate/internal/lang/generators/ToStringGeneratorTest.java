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
import org.jenerate.internal.ui.dialogs.ToStringDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaUiCodeAppender;
import org.jenerate.internal.util.JeneratePluginCodeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
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
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, createdMethod);
        verify(compilationUnit, times(1)).createImport(CommonsLangLibraryUtils.getToStringBuilderLibrary(false), null,
                null);
    }

    @Test
    public void verifyGeneratedCodeToStringExists() throws RuntimeException, CoreException {
        mockToStringMethodExists(true);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.singleton(toStringMethod), fields, true,
                        preferencesManager)).thenReturn(fieldDialog);
        toStringGenerator.generate(parentShell, objectClass);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, createdMethod);
        verify(compilationUnit, times(1)).createImport(CommonsLangLibraryUtils.getToStringBuilderLibrary(false), null,
                null);
    }

    @Test
    public void verifyGeneratedCodeWithDisplayedFieldsOfSuperclass() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.DISPLAY_FIELDS_OF_SUPERCLASSES))
                .thenReturn(true);
        toStringGenerator.generate(parentShell, objectClass);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, createdMethod);
        verify(compilationUnit, times(1)).createImport(CommonsLangLibraryUtils.getToStringBuilderLibrary(false), null,
                null);
    }

    @Test
    public void verifyGeneratedCodeWithOverridenInSuperclass() throws RuntimeException, CoreException {
        when(generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "toString", new String[0], null))
                .thenReturn(true);
        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, false,
                        preferencesManager)).thenReturn(fieldDialog);
        toStringGenerator.generate(parentShell, objectClass);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, createdMethod);
        verify(compilationUnit, times(1)).createImport(CommonsLangLibraryUtils.getToStringBuilderLibrary(false), null,
                null);
    }

    @Test
    public void testDialogCancelPressed() throws RuntimeException {
        when(fieldDialog.open()).thenReturn(Window.CANCEL);
        toStringGenerator.generate(parentShell, objectClass);
        verifyNoMoreInteractions(javaUiCodeAppender, compilationUnit);
    }

    private void mockJeneratePluginCodeFormatter() throws JavaModelException, BadLocationException {
        when(jeneratePluginCodeFormatter.formatCode(any(IType.class), anyString())).thenReturn(FORMATTED_CODE);
    }

    private void mockPreferencesManager() {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.DISPLAY_FIELDS_OF_SUPERCLASSES))
                .thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.TOSTRING_CACHING_FIELD)).thenReturn(
                "toString");
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
        when(generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "toString", new String[0], null))
                .thenReturn(false);
    }

    private void mockFieldDialog() {
        when(fieldDialog.open()).thenReturn(Window.OK);
        when(fieldDialog.getToStringStyle()).thenReturn(TO_STRING_STYLE);
        when(fieldDialog.getCheckedFields()).thenReturn(fields);
        when(fieldDialog.getElementPosition()).thenReturn(iJavaElement);
    }

    private void mockObjectClass() throws CoreException {
        IField iField = mock(IField.class);
        when(iField.exists()).thenReturn(false);
        when(objectClass.getField("")).thenReturn(iField);

        when(field1.getElementName()).thenReturn("field1");
        when(field2.getElementName()).thenReturn("field2");
        fields = new IField[] { field1, field2 };
        when(objectClass.getFields()).thenReturn(fields);

        when(objectClass.getCompilationUnit()).thenReturn(compilationUnit);

        when(objectClass.createMethod(FORMATTED_CODE, iJavaElement, true, null)).thenReturn(createdMethod);

    }

    private void mockToStringMethodExists(boolean exists) {
        when(toStringMethod.exists()).thenReturn(exists);
        when(objectClass.getMethod("toString", new String[0])).thenReturn(toStringMethod);
    }

}
