package org.jenerate.internal.lang.generators;

import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PartInitException;
import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.data.IInitMultNumbers;
import org.jenerate.internal.domain.method.content.CommonsLangLibraryUtils;
import org.jenerate.internal.ui.dialogs.EqualsHashCodeDialog;
import org.jenerate.internal.ui.dialogs.provider.DialogFactory;
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
 * Unit tests for the {@link EqualsHashCodeGenerator} XXX to be refactored with the new strategy
 * 
 * @author maudrain
 */
@Ignore
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
    private DialogFactory<EqualsHashCodeDialog, EqualsHashCodeDialogData> dialogProvider;
    @Mock
    private EqualsHashCodeDialog fieldDialog;
    @Mock
    private EqualsHashCodeDialogData data;
    @Mock
    private IInitMultNumbers iInitMultNumbers;

    private EqualsHashCodeGenerator equalsHashCodeGenerator;

    @Override
    public void callbackAfterSetUp() throws Exception {
        mockSpecificPreferencesManager();
        mockSpecificObjectClass();
        mockSpecificGeneratorsCommonMethodsDelegate();
        mockCommonFieldDialog();
        mockSpecificFieldDialog();
        mockHashCodeMethodExists(false);
        mockEqualsMethodExists(false);
        mockAppendGeneratedCode();

        when(dialogProvider.createDialog(parentShell, objectClass, Collections.<IMethod> emptySet())).thenReturn(
                fieldDialog);

        equalsHashCodeGenerator = new EqualsHashCodeGenerator(javaUiCodeAppender, preferencesManager, dialogProvider,
                jeneratePluginCodeFormatter, generatorsCommonMethodsDelegate);
    }

    @Test
    public void verifyGeneratedCodeDefault() throws RuntimeException, CoreException {
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeHashCodeMethodExists() throws Exception {
        mockHashCodeMethodExists(true);
        when(dialogProvider.createDialog(parentShell, objectClass, Collections.singleton(hashCodeMethod))).thenReturn(
                fieldDialog);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
        verify(hashCodeMethod, times(1)).delete(true, null);
    }

    @Test
    public void verifyGeneratedCodeEqualsMethodExists() throws Exception {
        mockEqualsMethodExists(true);
        when(dialogProvider.createDialog(parentShell, objectClass, Collections.singleton(equalsMethod))).thenReturn(
                fieldDialog);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
        verify(equalsMethod, times(1)).delete(true, null);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void testDialogCancelPressed() throws RuntimeException {
        when(fieldDialog.open()).thenReturn(Window.CANCEL);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyNoMoreInteractions(javaUiCodeAppender, compilationUnit);
    }

    @Test
    public void verifyGeneratedCodeWithCachedToString() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).thenReturn(Boolean.TRUE);
        when(generatorsCommonMethodsDelegate.areAllFinalFields(fields)).thenReturn(true);

        when(hashCodeCachingField.exists()).thenReturn(true);
        when(objectClass.getField(HASH_CODE_FIELD_NAME)).thenReturn(hashCodeCachingField);
        when(objectClass.createMethod(FORMATTED_CODE_2, elementPosition, true, null)).thenReturn(createdMethod1);
        equalsHashCodeGenerator.generate(parentShell, objectClass);

        verify(hashCodeCachingField, times(1)).delete(true, null);
        verify(objectClass, times(1)).createField("private transient int " + HASH_CODE_FIELD_NAME + ";\n\n",
                elementPosition, true, null);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeDisableAppendSuperCauseDirectSubclassOfObject() throws RuntimeException,
            CoreException {
        when(objectClass.getSuperclassName()).thenReturn("Object");
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeDisableAppendSuperCauseDirectSubclassOfJavaLangObject() throws RuntimeException,
            CoreException {
        when(objectClass.getSuperclassName()).thenReturn("java.lang.Object");
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX Move test to dialog provider
     */
    @Test
    public void verifyGeneratedCodeEnableAppendSuper() throws RuntimeException, CoreException {
        when(objectClass.getSuperclassName()).thenReturn("SomeOtherObject");
        // when(
        // dialogProvider.getDialog(parentShell, objectClass, Collections.<IMethod> emptySet(), fields, false,
        // preferencesManager)).thenReturn(fieldDialog);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeDisableAppendSuperBecauseHashCodeNotOverriden() throws RuntimeException,
            CoreException {
        when(objectClass.getSuperclassName()).thenReturn("SomeOtherObject");
        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "hashCode", new String[0],
                        "java.lang.Object")).thenReturn(false);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeDisableAppendSuperBecauseEqualsNotOverriden() throws RuntimeException, CoreException {
        when(objectClass.getSuperclassName()).thenReturn("SomeOtherObject");
        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "equals",
                        new String[] { "QObject;" }, "java.lang.Object")).thenReturn(false);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithOverrideAnnotation() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION)).thenReturn(true);
        when(generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)).thenReturn(true);

        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeCacheHashCode() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).thenReturn(Boolean.TRUE);
        when(generatorsCommonMethodsDelegate.areAllFinalFields(fields)).thenReturn(true);
        when(hashCodeCachingField.exists()).thenReturn(true);
        when(objectClass.getField(HASH_CODE_FIELD_NAME)).thenReturn(hashCodeCachingField);
        when(objectClass.createMethod(FORMATTED_CODE_1, elementPosition, true, null)).thenReturn(createdMethod1);
        when(objectClass.createMethod(FORMATTED_CODE_2, createdMethod1, true, null)).thenReturn(createdMethod2);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithCommonsLang3() throws RuntimeException, CoreException {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).thenReturn(true);

        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(true);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithComment() throws RuntimeException, CoreException {
        when(data.getGenerateComment()).thenReturn(true);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithAppendSuper() throws RuntimeException, CoreException {
        when(data.getAppendSuper()).thenReturn(true);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    /**
     * XXX same except the generator
     */
    @Test
    public void verifyGeneratedCodeWithGettersInsteadOfFields() throws RuntimeException, CoreException {
        when(data.getUseGettersInsteadOfFields()).thenReturn(true);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithCompareReferences() throws RuntimeException, CoreException {
        when(data.getCompareReferences()).thenReturn(true);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    @Test
    public void verifyGeneratedCodeWithUseBlockInIfStatements() throws RuntimeException, CoreException {
        when(data.getUseBlockInIfStatements()).thenReturn(true);
        equalsHashCodeGenerator.generate(parentShell, objectClass);
        verifyCodeAppended(false);
    }

    private void mockAppendGeneratedCode() throws JavaModelException {
        when(objectClass.createMethod(FORMATTED_CODE_1, elementPosition, true, null)).thenReturn(createdMethod1);
        when(objectClass.createMethod(FORMATTED_CODE_2, createdMethod1, true, null)).thenReturn(createdMethod2);
    }

    private void verifyCodeAppended(boolean useCommonsLang3) throws JavaModelException, PartInitException {
        verify(compilationUnit, times(1)).createImport(
                CommonsLangLibraryUtils.getHashCodeBuilderLibrary(useCommonsLang3), null, null);
        verify(compilationUnit, times(1)).createImport(
                CommonsLangLibraryUtils.getEqualsBuilderLibrary(useCommonsLang3), null, null);
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
                        "java.lang.Object")).thenReturn(true);

        when(
                generatorsCommonMethodsDelegate.isOverriddenInSuperclass(objectClass, "equals",
                        new String[] { "QObject;" }, "java.lang.Object")).thenReturn(true);
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
        when(data.getCompareReferences()).thenReturn(false);
        when(data.getUseBlockInIfStatements()).thenReturn(false);
        when(data.getInitMultNumbers()).thenReturn(iInitMultNumbers);
    }

}
