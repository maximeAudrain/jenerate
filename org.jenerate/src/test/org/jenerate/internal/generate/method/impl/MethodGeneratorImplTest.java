package org.jenerate.internal.generate.method.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.generate.method.util.JavaCodeFormatter;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;
import org.jenerate.internal.manage.MethodContentManager;
import org.jenerate.internal.manage.MethodSkeletonManager;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link MethodGeneratorImpl}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class MethodGeneratorImplTest {

    private static final String METHOD_1_ARGUMENT = "argument1";
    private static final String METHOD_1 = "method1";
    private static final String[] METHOD_1_ARGUMENTS = new String[] { METHOD_1_ARGUMENT };
    private static final String METHOD1_FULL_METHOD = "method1FullMethod";
    private static final String METHOD1_CONTENT = "method1Content";

    private static final String METHOD_2 = "method2";
    private static final String[] METHOD_2_ARGUMENTS = new String[0];
    private static final String METHOD2_FULL_METHOD = "method2FullMethod";
    private static final String METHOD2_LIBRARY_1 = "method1Library1";
    private static final String METHOD2_LIBRARY_2 = "method1Library2";
    private static final String METHOD2_CONTENT = "method2Content";

    @Mock
    private DialogFactory<MethodGenerationData> dialogFactory;
    @Mock
    private FieldDialog<MethodGenerationData> testDialog;
    @Mock
    private Dialog dialog;

    @Mock
    private JavaUiCodeAppender javaUiCodeAppender;
    @Mock
    private JavaCodeFormatter jeneratePluginCodeFormatter;

    @Mock
    private MethodSkeletonManager methodSkeletonManager;
    @Mock
    private MethodContentManager methodContentManager;

    @Mock
    private StrategyIdentifier strategyIdentifier1;
    @Mock
    private StrategyIdentifier strategyIdentifier2;
    @Mock
    private CommandIdentifier commandIdentifier;
    @Mock
    private Shell parentShell;
    @Mock
    private IType objectClass;
    @Mock
    private ICompilationUnit compilationUnit;
    @Mock
    private MethodGenerationData data;
    @Mock
    private IJavaElement elementPosition;
    @Mock
    private IMethod newPositionAfterMethod1;
    @Mock
    private IMethod newPositionAfterMethod2;
    @Mock
    private IMethod excludedMethod1;
    @Mock
    private IMethod excludedMethod2;

    @Mock
    private MethodSkeleton<MethodGenerationData> method1Skeleton;
    @Mock
    private MethodSkeleton<MethodGenerationData> method2Skeleton;

    @Mock
    private MethodContent<MethodSkeleton<MethodGenerationData>, MethodGenerationData> method1Content;
    @Mock
    private MethodContent<MethodSkeleton<MethodGenerationData>, MethodGenerationData> method2Content;

    @Mock
    private Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData> method1;
    @Mock
    private Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData> method2;

    private MethodGeneratorImpl<MethodSkeleton<MethodGenerationData>, MethodGenerationData> methodGenerator;

    @Before
    public void setUp() throws Exception {
        mockJavaFormatter();
        mockObjectClass();
        mockData();
        mockMethod1();
        mockMethod2();
        methodGenerator = new MethodGeneratorImpl<MethodSkeleton<MethodGenerationData>, MethodGenerationData>(
                dialogFactory, javaUiCodeAppender, jeneratePluginCodeFormatter, methodSkeletonManager,
                methodContentManager);
    }

    @Test
    public void testGenerateWithUnknownIdentifier() throws Exception {
        CommandIdentifier unknownCommandIdentifier = mock(CommandIdentifier.class);
        LinkedHashSet<MethodSkeleton<MethodGenerationData>> methodSkeletons = mockGetMethodSkeletons(unknownCommandIdentifier);
        LinkedHashSet<StrategyIdentifier> possibleStrategies = mockGetPossibleStrategies(methodSkeletons);
        mockDialog(Collections.<IMethod> emptySet(), possibleStrategies);
        mockGetMethods(methodSkeletons);

        methodGenerator.generate(parentShell, objectClass, unknownCommandIdentifier);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, elementPosition);
    }

    @Test
    public void testGenerateDialogResultIsNotOk() throws Exception {
        LinkedHashSet<MethodSkeleton<MethodGenerationData>> methodSkeletons = mockGetMethodSkeletons(commandIdentifier,
                method1Skeleton);
        LinkedHashSet<StrategyIdentifier> possibleStrategies = mockGetPossibleStrategies(methodSkeletons,
                strategyIdentifier1);
        mockDialog(Collections.<IMethod> emptySet(), possibleStrategies);

        when(dialog.open()).thenReturn(Window.CANCEL);
        methodGenerator.generate(parentShell, objectClass, commandIdentifier);
        verifyNoMoreInteractions(javaUiCodeAppender, compilationUnit);
    }

    @Test
    public void testGenerateWithMethod1() throws Exception {
        LinkedHashSet<MethodSkeleton<MethodGenerationData>> methodSkeletons = mockGetMethodSkeletons(commandIdentifier,
                method1Skeleton);
        LinkedHashSet<StrategyIdentifier> possibleStrategies = mockGetPossibleStrategies(methodSkeletons,
                strategyIdentifier1);
        mockDialog(Collections.<IMethod> emptySet(), possibleStrategies);
        mockGetMethods(methodSkeletons, method1);

        when(objectClass.createMethod(METHOD1_FULL_METHOD, elementPosition, true, null)).thenReturn(
                newPositionAfterMethod1);
        methodGenerator.generate(parentShell, objectClass, commandIdentifier);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, newPositionAfterMethod1);
        verifyNoMoreInteractions(compilationUnit);
    }

    @Test
    public void testGenerateWithMethod2() throws Exception {
        LinkedHashSet<MethodSkeleton<MethodGenerationData>> methodSkeletons = mockGetMethodSkeletons(commandIdentifier,
                method2Skeleton);
        LinkedHashSet<StrategyIdentifier> possibleStrategies = mockGetPossibleStrategies(methodSkeletons,
                strategyIdentifier1);
        mockDialog(Collections.<IMethod> emptySet(), possibleStrategies);
        mockGetMethods(methodSkeletons, method2);

        when(objectClass.createMethod(METHOD2_FULL_METHOD, elementPosition, true, null)).thenReturn(
                newPositionAfterMethod2);
        methodGenerator.generate(parentShell, objectClass, commandIdentifier);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, newPositionAfterMethod2);
        verify(compilationUnit, times(1)).createImport(METHOD2_LIBRARY_1, null, null);
        verify(compilationUnit, times(1)).createImport(METHOD2_LIBRARY_2, null, null);
    }

    @Test
    public void testGenerateWithTwoMethods() throws Exception {
        LinkedHashSet<MethodSkeleton<MethodGenerationData>> methodSkeletons = mockGetMethodSkeletons(commandIdentifier,
                method1Skeleton, method2Skeleton);
        LinkedHashSet<StrategyIdentifier> possibleStrategies = mockGetPossibleStrategies(methodSkeletons,
                strategyIdentifier1);
        mockDialog(Collections.<IMethod> emptySet(), possibleStrategies);
        mockGetMethods(methodSkeletons, method1, method2);

        when(objectClass.createMethod(METHOD1_FULL_METHOD, elementPosition, true, null)).thenReturn(
                newPositionAfterMethod1);
        when(objectClass.createMethod(METHOD2_FULL_METHOD, newPositionAfterMethod1, true, null)).thenReturn(
                newPositionAfterMethod2);
        methodGenerator.generate(parentShell, objectClass, commandIdentifier);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, newPositionAfterMethod2);
        verify(compilationUnit, times(1)).createImport(METHOD2_LIBRARY_1, null, null);
        verify(compilationUnit, times(1)).createImport(METHOD2_LIBRARY_2, null, null);
    }

    @Test
    public void testGenerateWithTwoMethodsAlreadyExists() throws Exception {
        LinkedHashSet<MethodSkeleton<MethodGenerationData>> methodSkeletons = mockGetMethodSkeletons(commandIdentifier,
                method1Skeleton, method2Skeleton);
        LinkedHashSet<StrategyIdentifier> possibleStrategies = mockGetPossibleStrategies(methodSkeletons,
                strategyIdentifier1);
        mockDialog(Collections.<IMethod> emptySet(), possibleStrategies);
        mockGetMethods(methodSkeletons, method1, method2);

        Set<IMethod> excludedMethods = new HashSet<IMethod>();
        excludedMethods.add(excludedMethod1);
        excludedMethods.add(excludedMethod2);
        when(excludedMethod1.exists()).thenReturn(true);
        when(excludedMethod2.exists()).thenReturn(true);
        when(objectClass.createMethod(METHOD1_FULL_METHOD, elementPosition, true, null)).thenReturn(
                newPositionAfterMethod1);
        when(objectClass.createMethod(METHOD2_FULL_METHOD, newPositionAfterMethod1, true, null)).thenReturn(
                newPositionAfterMethod2);
        when(dialogFactory.createDialog(parentShell, objectClass, excludedMethods, possibleStrategies)).thenReturn(
                testDialog);
        methodGenerator.generate(parentShell, objectClass, commandIdentifier);
        verify(javaUiCodeAppender, times(1)).revealInEditor(objectClass, newPositionAfterMethod2);
        verify(compilationUnit, times(1)).createImport(METHOD2_LIBRARY_1, null, null);
        verify(compilationUnit, times(1)).createImport(METHOD2_LIBRARY_2, null, null);
        verify(excludedMethod1, times(1)).delete(true, null);
        verify(excludedMethod2, times(1)).delete(true, null);
    }

    @SafeVarargs
    private final LinkedHashSet<MethodSkeleton<MethodGenerationData>> mockGetMethodSkeletons(
            CommandIdentifier identifier, MethodSkeleton<MethodGenerationData>... skeletons) {
        LinkedHashSet<MethodSkeleton<MethodGenerationData>> methodSkeletons = new LinkedHashSet<>();
        for (MethodSkeleton<MethodGenerationData> skeleton : skeletons) {
            methodSkeletons.add(skeleton);
        }
        when(methodSkeletonManager.getMethodSkeletons(identifier)).thenReturn(methodSkeletons);
        return methodSkeletons;
    }

    @SafeVarargs
    private final LinkedHashSet<Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData>> mockGetMethods(
            LinkedHashSet<MethodSkeleton<MethodGenerationData>> skeletons,
            Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData>... methods) {
        LinkedHashSet<Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData>> allMethods = new LinkedHashSet<>();
        for (Method<MethodSkeleton<MethodGenerationData>, MethodGenerationData> method : methods) {
            allMethods.add(method);
        }
        when(methodContentManager.getAllMethods(skeletons, strategyIdentifier1)).thenReturn(allMethods);
        return allMethods;
    }

    @SafeVarargs
    private final LinkedHashSet<StrategyIdentifier> mockGetPossibleStrategies(
            LinkedHashSet<MethodSkeleton<MethodGenerationData>> skeletons, StrategyIdentifier... identifiers) {
        LinkedHashSet<StrategyIdentifier> possibleStrategies = new LinkedHashSet<StrategyIdentifier>();
        for (StrategyIdentifier strategyIdentifier : identifiers) {
            possibleStrategies.add(strategyIdentifier);
        }
        when(methodContentManager.getStrategiesIntersection(skeletons)).thenReturn(possibleStrategies);
        return possibleStrategies;
    }

    private void mockDialog(Set<IMethod> excludedMethods, LinkedHashSet<StrategyIdentifier> possibleStrategies)
            throws Exception {
        when(dialogFactory.createDialog(parentShell, objectClass, excludedMethods, possibleStrategies)).thenReturn(
                testDialog);
        when(dialog.open()).thenReturn(Window.OK);
        when(testDialog.getDialog()).thenReturn(dialog);
        when(testDialog.getData()).thenReturn(data);
    }

    private void mockJavaFormatter() throws Exception {
        when(jeneratePluginCodeFormatter.formatCode(any(IType.class), anyString())).thenAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(1, String.class);
            }
        });
    }

    private void mockData() {
        when(data.getSelectedStrategyIdentifier()).thenReturn(strategyIdentifier1);
        when(data.getElementPosition()).thenReturn(elementPosition);
    }

    private void mockObjectClass() {
        when(excludedMethod1.exists()).thenReturn(false);
        when(excludedMethod2.exists()).thenReturn(false);
        when(objectClass.getMethod(METHOD_1, METHOD_1_ARGUMENTS)).thenReturn(excludedMethod1);
        when(objectClass.getMethod(METHOD_2, METHOD_2_ARGUMENTS)).thenReturn(excludedMethod2);
        when(objectClass.getCompilationUnit()).thenReturn(compilationUnit);
    }

    private void mockMethod1() throws Exception {
        when(method1Skeleton.getMethodName()).thenReturn(METHOD_1);
        when(method1Skeleton.getMethodArguments(objectClass)).thenReturn(METHOD_1_ARGUMENTS);
        when(method1Skeleton.getLibrariesToImport()).thenReturn(new LinkedHashSet<String>());
        when(method1Skeleton.getMethod(objectClass, data, METHOD1_CONTENT)).thenReturn(METHOD1_FULL_METHOD);
        when(method1.getMethodSkeleton()).thenReturn(method1Skeleton);

        when(method1Content.getLibrariesToImport(data)).thenReturn(new LinkedHashSet<String>());
        when(method1Content.getMethodContent(objectClass, data)).thenReturn(METHOD1_CONTENT);
        when(method1.getMethodContent()).thenReturn(method1Content);
    }

    private void mockMethod2() throws Exception {
        when(method2Skeleton.getMethodName()).thenReturn(METHOD_2);
        when(method2Skeleton.getMethodArguments(objectClass)).thenReturn(METHOD_2_ARGUMENTS);
        LinkedHashSet<String> skeletonLibraries = new LinkedHashSet<String>();
        skeletonLibraries.add(METHOD2_LIBRARY_1);
        when(method2Skeleton.getLibrariesToImport()).thenReturn(skeletonLibraries);
        when(method2Skeleton.getMethod(objectClass, data, METHOD2_CONTENT)).thenReturn(METHOD2_FULL_METHOD);
        when(method2.getMethodSkeleton()).thenReturn(method2Skeleton);

        LinkedHashSet<String> contentLibraries = new LinkedHashSet<String>();
        contentLibraries.add(METHOD2_LIBRARY_2);
        when(method2Content.getLibrariesToImport(data)).thenReturn(contentLibraries);
        when(method2Content.getMethodContent(objectClass, data)).thenReturn(METHOD2_CONTENT);
        when(method2.getMethodContent()).thenReturn(method2Content);
    }

}
