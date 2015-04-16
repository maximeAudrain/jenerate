package org.jenerate.internal.strategy.method.skeleton.impl;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * Abstract test for {@link MethodSkeleton}
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodSkeleton} under test
 * @param <U> the type of {@link MethodGenerationData} attached to this {@link MethodSkeleton}
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractMethodSkeletonTest<T extends MethodSkeleton<U>, U extends MethodGenerationData> {

    protected static final String METHOD_CONTENT = "CONTENT";
    protected static final String TEST_ELEMENT_NAME = "Test";

    @Mock
    protected PreferencesManager preferencesManager;

    @Mock
    protected IType objectClass;
    @Mock
    private IJavaProject project;

    protected U data;
    protected T methodSkeleton;

    @Before
    public void setUp() throws Exception {
        methodSkeleton = getConcreteClassUnderTest();
        data = getConcreteData();
        mockData();
        mockObjectClass();
        mockIsSourceLevelAbove5(false);
        mockAddOverrideAnnotation(false);
        callbackAfterSetUp();
    }

    private void mockData() {
        when(data.generateComment()).thenReturn(false);
    }

    private void mockObjectClass() {
        when(objectClass.getElementName()).thenReturn(TEST_ELEMENT_NAME);
    }

    protected void mockIsSourceLevelAbove5(boolean sourceLevelAbove5) {
        when(objectClass.getJavaProject()).thenReturn(project);
        when(project.getOption(JavaCore.COMPILER_SOURCE, true)).thenReturn(sourceLevelAbove5 ? "1.7" : "1.4");
    }

    protected void mockAddOverrideAnnotation(boolean addOverride) throws Exception {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.ADD_OVERRIDE_ANNOTATION)).thenReturn(
                addOverride);
    }

    /**
     * @return the concrete instance of the {@link MethodSkeleton} under test
     */
    public abstract T getConcreteClassUnderTest();

    /**
     * @return the concrete instance of the {@link MethodGenerationData} for the {@link MethodSkeleton} under test
     */
    public abstract U getConcreteData();

    /**
     * Callback after the Junit Before annotated setUp method is called
     * 
     * @throws Exception if an exception occur
     */
    public abstract void callbackAfterSetUp() throws Exception;

}
