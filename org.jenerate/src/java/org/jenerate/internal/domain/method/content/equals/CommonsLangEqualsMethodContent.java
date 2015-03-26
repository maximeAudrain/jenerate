package org.jenerate.internal.domain.method.content.equals;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.CommonsLangLibraryUtils;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class CommonsLangEqualsMethodContent extends AbstractMethodContent<EqualsHashCodeDialogData> {

    private final boolean useCommonsLang3;

    public CommonsLangEqualsMethodContent(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate, boolean useCommonsLang3) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
        this.useCommonsLang3 = useCommonsLang3;
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeDialogData data) throws JavaModelException {
        return MethodGenerations.generateEqualsMethodContent(data, objectClass);
    }

    @Override
    public Set<String> getLibrariesToImport(EqualsHashCodeDialogData data) {
        return Collections.singleton(CommonsLangLibraryUtils.getEqualsBuilderLibrary(useCommonsLang3));
    }

}
