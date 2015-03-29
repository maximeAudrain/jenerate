package org.jenerate.internal.manage.impl;

import java.util.LinkedHashSet;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.MethodContentManager;
import org.jenerate.internal.manage.MethodManager;
import org.jenerate.internal.manage.MethodSkeletonManager;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.Method;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.impl.MethodImpl;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

/**
 * Default implementation of the {@link MethodManager}. This manager is holding the logic to determine which
 * {@link MethodContent} strategy is currently in effect and building up all the {@link Method}s to be used for a
 * certain {@link CommandIdentifier}
 * 
 * @author maudrain
 */
public final class MethodManagerImpl implements MethodManager {

    private final PreferencesManager preferencesManager;
    private final MethodSkeletonManager methodSkeletonManager;
    private final MethodContentManager methodContentManager;

    /**
     * Constructor
     * 
     * @param preferencesManager the preference manager
     * @param javaInterfaceCodeAppender the java interface code appender
     */
    public MethodManagerImpl(PreferencesManager preferencesManager, JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        this.preferencesManager = preferencesManager;
        this.methodSkeletonManager = new MethodSkeletonManagerImpl(preferencesManager, javaInterfaceCodeAppender);
        this.methodContentManager = new MethodContentManagerImpl(preferencesManager, javaInterfaceCodeAppender);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MethodSkeleton<U>, U extends MethodGenerationData> LinkedHashSet<Method<T, U>> getMethods(
            CommandIdentifier commandIdentifier) {
        boolean useCommonLang3 = preferencesManager.getCurrentPreferenceValue(JeneratePreferences.USE_COMMONS_LANG3)
                .booleanValue();
        StrategyIdentifier strategyIdentifier = MethodContentStrategyIdentifier.USE_COMMONS_LANG;
        if (useCommonLang3) {
            strategyIdentifier = MethodContentStrategyIdentifier.USE_COMMONS_LANG3;
        }

        LinkedHashSet<MethodSkeleton<U>> methodSkeletons = methodSkeletonManager.getMethodSkeletons(commandIdentifier);

        LinkedHashSet<Method<T, U>> methods = new LinkedHashSet<Method<T, U>>();
        for (MethodSkeleton<U> methodSkeleton : methodSkeletons) {
            MethodContent<T, U> methodContent = methodContentManager.getMethodContent(methodSkeleton,
                    strategyIdentifier);
            methods.add(new MethodImpl<T, U>((T) methodSkeleton, methodContent));
        }
        return methods;
    }

}
