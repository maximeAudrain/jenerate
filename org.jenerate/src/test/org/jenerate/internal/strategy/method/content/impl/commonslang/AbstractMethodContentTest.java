package org.jenerate.internal.strategy.method.content.impl.commonslang;

import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.Signature;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Abstract test for the {@link MethodContent}
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodContent} under test
 * @param <U> the type of {@link MethodSkeleton} related to the {@link MethodContent}
 * @param <V> the type of data for this {@link MethodContent}
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public abstract class AbstractMethodContentTest<T extends MethodContent<U, V>, U extends MethodSkeleton<V>, V extends MethodGenerationData> {

    protected static final String TEST_ELEMENT_NAME = "Test";

    @Mock
    protected PreferencesManager preferencesManager;

    @Mock
    protected IType objectClass;
    @Mock
    protected IJavaElement elementPosition;
    @Mock
    private IJavaProject project;

    @Mock
    protected IField field1;
    @Mock
    protected IField field2;

    protected IField[] fields = new IField[] { field1, field2 };

    protected T methodContent;
    protected V data;

    @Before
    public void setUp() throws Exception {
        data = getConcreteData();
        mockFields();
        mockData();
        mockObjectClass();
        mockIsSourceLevelAbove5(false);
        mockAddOverrideAnnotation(false);
        callbackAfterSetUp();
    }

    private void mockData() {
        when(data.generateComment()).thenReturn(false);
        when(data.getCheckedFields()).thenReturn(fields);
        when(data.appendSuper()).thenReturn(false);
        when(data.useBlockInIfStatements()).thenReturn(false);
        when(data.getElementPosition()).thenReturn(elementPosition);
        when(data.useGettersInsteadOfFields()).thenReturn(false);
    }

    private void mockFields() throws Exception {
        when(field1.getElementName()).thenReturn("field1");
        when(field2.getElementName()).thenReturn("field2");
        when(field1.getTypeSignature()).thenReturn(Signature.SIG_BOOLEAN);
        fields = new IField[] { field1, field2 };
    }

    private void mockObjectClass() throws Exception {
        when(objectClass.getElementName()).thenReturn(TEST_ELEMENT_NAME);
    }

    protected void mockIsSourceLevelAbove5(boolean sourceLevelAbove5) {
        when(objectClass.getJavaProject()).thenReturn(project);
        when(project.getOption(JavaCore.COMPILER_SOURCE, true)).thenReturn(sourceLevelAbove5 ? "1.7" : "1.4");
    }

    protected void mockAddOverrideAnnotation(boolean addOverride) throws Exception {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.ADD_OVERRIDE_ANNOTATION))
                .thenReturn(addOverride);
    }

    /**
     * @return the concrete instance of the {@link MethodGenerationData} for the {@link MethodSkeleton} under test
     */
    public abstract V getConcreteData();

    /**
     * Callback after the Junit Before annotated setUp method is called
     * 
     * @throws Exception if an exception occur
     */
    public abstract void callbackAfterSetUp() throws Exception;

}
