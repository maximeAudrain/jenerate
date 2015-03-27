package org.jenerate.internal.strategy.method.content.impl.commonslang;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * Abstract test for the {@link MethodContent}
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodContent} under test
 * @param <U> the type of {@link MethodSkeleton} related to the {@link MethodContent}
 * @param <V> the type of data for this {@link MethodContent}
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractMethodContentTest<T extends MethodContent<U, V>, U extends MethodSkeleton<V>, V extends MethodGenerationData> {

    protected static final String TEST_ELEMENT_NAME = "Test";

    @Mock
    protected PreferencesManager preferencesManager;
    @Mock
    protected GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    @Mock
    protected IType objectClass;

    @Mock
    private IField field1;
    @Mock
    private IField field2;

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
        when(data.getGenerateComment()).thenReturn(false);
        when(data.getCheckedFields()).thenReturn(fields);
        when(data.getAppendSuper()).thenReturn(false);
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
        when(generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)).thenReturn(
                sourceLevelAbove5);
    }

    protected void mockAddOverrideAnnotation(boolean addOverride) throws Exception {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION)).thenReturn(
                addOverride);
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
