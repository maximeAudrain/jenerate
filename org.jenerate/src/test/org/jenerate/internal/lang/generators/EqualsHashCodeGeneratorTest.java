package org.jenerate.internal.lang.generators;

import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.window.Window;
import org.jenerate.internal.data.IInitMultNumbers;
import org.jenerate.internal.ui.dialogs.EqualsHashCodeDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogProvider;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EqualsHashCodeGenerator}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class EqualsHashCodeGeneratorTest extends AbstractGeneratorTest {

    private static final String HASH_CODE_FIELD_NAME = "hashCode";
    private static final String EQUALS_FIELD_NAME = "equals";

    @Mock
    private IMethod hashCodeMethod;
    @Mock
    private IMethod equalsMethod;
    @Mock
    private IField hashCodeCachingField;
    @Mock
    protected IMethod createdMethod2;

    @Mock
    private DialogProvider<EqualsHashCodeDialog> dialogProvider;
    @Mock
    private EqualsHashCodeDialog fieldDialog;
    @Mock
    private IInitMultNumbers iInitMultNumbers;

    private EqualsHashCodeGenerator equalsHashCodeGenerator;

    @Override
    public void callbackAfterSetUp() throws CoreException, BadLocationException {
        mockSpecificPreferencesManager();
        mockSpecificObjectClass();
        mockSpecificGeneratorsCommonMethodsDelegate();
        mockCommonFieldDialog();
        mockSpecificFieldDialog();
        mockHashCodeMethodExists(false);
        mockEqualsMethodExists(false);

        when(
                dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, true,
                        preferencesManager)).thenReturn(fieldDialog);

        equalsHashCodeGenerator = new EqualsHashCodeGenerator(javaUiCodeAppender, preferencesManager, dialogProvider,
                jeneratePluginCodeFormatter, generatorsCommonMethodsDelegate);
    }

    @Test
    public void verifyGeneratedCodeDefault() throws RuntimeException, CoreException {
        when(objectClass.createMethod(FORMATTED_CODE_1, elementPosition, true, null)).thenReturn(createdMethod1);
        when(objectClass.createMethod(FORMATTED_CODE_2, createdMethod1, true, null)).thenReturn(createdMethod2);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verify(compilationUnit, times(1)).createImport(CommonsLangLibraryUtils.getHashCodeBuilderLibrary(false), null,
                null);
        verify(compilationUnit, times(1)).createImport(CommonsLangLibraryUtils.getEqualsBuilderLibrary(false), null,
                null);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, createdMethod2);
    }

    private void mockHashCodeMethodExists(boolean exists) {
        when(hashCodeMethod.exists()).thenReturn(exists);
        when(objectClass.getMethod(HASH_CODE_FIELD_NAME, new String[0])).thenReturn(hashCodeMethod);
    }

    private void mockEqualsMethodExists(boolean exists) {
        when(equalsMethod.exists()).thenReturn(exists);
        when(objectClass.getMethod(EQUALS_FIELD_NAME, new String[] { "QObject;" })).thenReturn(equalsMethod);
    }

    private void mockSpecificPreferencesManager() {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.HASHCODE_CACHING_FIELD)).thenReturn(
                HASH_CODE_FIELD_NAME);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).thenReturn(false);
    }

    private void mockSpecificObjectClass() throws JavaModelException {
        when(hashCodeCachingField.exists()).thenReturn(false);
        when(objectClass.getField("")).thenReturn(hashCodeCachingField);

        when(objectClass.getSuperclassName()).thenReturn(null);
    }

    private void mockSpecificGeneratorsCommonMethodsDelegate() throws JavaModelException {
        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "hashCode", new String[0],
                        "java.lang.Object")).thenReturn(false);

        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "equals",
                        new String[] { "QObject;" }, "java.lang.Object")).thenReturn(false);
    }

    private void mockCommonFieldDialog() {
        when(fieldDialog.open()).thenReturn(Window.OK);
        when(fieldDialog.getCheckedFields()).thenReturn(fields);
        when(fieldDialog.getElementPosition()).thenReturn(elementPosition);
        when(fieldDialog.getGenerateComment()).thenReturn(false);
        when(fieldDialog.getAppendSuper()).thenReturn(false);
        when(fieldDialog.getUseGettersInsteadOfFields()).thenReturn(false);
    }

    private void mockSpecificFieldDialog() {
        when(fieldDialog.getCompareReferences()).thenReturn(false);
        when(fieldDialog.getUseBlockInIfStatements()).thenReturn(false);
        when(fieldDialog.getInitMultNumbers()).thenReturn(iInitMultNumbers);
    }

}
