package org.jenerate.internal.domain.method.content.equals;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.domain.method.content.CommonsLangLibraryUtils;
import org.jenerate.internal.domain.method.skeleton.impl.EqualsMethod;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class CommonsLangEqualsMethodContent extends AbstractMethodContent<EqualsMethod, EqualsHashCodeDialogData> {

    public CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeDialogData data) throws JavaModelException {
        return MethodGenerations.generateEqualsMethodContent(data, objectClass);
    }

    @Override
    public Set<String> getLibrariesToImport(EqualsHashCodeDialogData data) {
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        return Collections.singleton(CommonsLangLibraryUtils.getEqualsBuilderLibrary(useCommonsLang3));
    }

    @Override
    public Class<EqualsMethod> getRelatedMethodSkeletonClass() {
        return EqualsMethod.class;
    }
}
