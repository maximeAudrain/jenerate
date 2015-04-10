package org.jenerate;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link JeneratePreferenceInitializer}
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class JeneratePreferenceInitializerTest {

    @Mock
    private IEclipsePreferences iEclipsePreferences;

    private JeneratePreferenceInitializer jeneratePreferenceInitializer;

    @Before
    public void setUp() {
        jeneratePreferenceInitializer = new JeneratePreferenceInitializer();
    }

    @Test
    public void testPreferencesAreFilledUp() {
        jeneratePreferenceInitializer.initializeDefaultValues(iEclipsePreferences);
        verify(iEclipsePreferences, times(7)).putBoolean(anyString(), anyBoolean());
        verify(iEclipsePreferences, times(3)).put(anyString(), anyString());
    }
}
