package org.jenerate.internal.domain.method.content.equals;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.domain.method.content.MethodContentLibraries;
import org.jenerate.internal.domain.method.skeleton.impl.EqualsMethodSkeleton;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;

public class CommonsLangEqualsMethodContent extends AbstractMethodContent<EqualsMethodSkeleton, EqualsHashCodeGenerationData> {

    public CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws JavaModelException {
        return MethodGenerations.generateEqualsMethodContent(data, objectClass);
    }

    @Override
    public Set<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        return Collections.singleton(MethodContentLibraries.getEqualsBuilderLibrary(useCommonsLang3));
    }

    @Override
    public Class<EqualsMethodSkeleton> getRelatedMethodSkeletonClass() {
        return EqualsMethodSkeleton.class;
    }
}
