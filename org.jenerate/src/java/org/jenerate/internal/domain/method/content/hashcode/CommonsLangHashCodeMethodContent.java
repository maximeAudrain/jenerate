package org.jenerate.internal.domain.method.content.hashcode;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.CommonsLangLibraryUtils;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class CommonsLangHashCodeMethodContent extends AbstractMethodContent<EqualsHashCodeDialogData> {

    private final boolean useCommonsLang3;

    public CommonsLangHashCodeMethodContent(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate, boolean useCommonsLang3) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
        this.useCommonsLang3 = useCommonsLang3;
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeDialogData data) throws JavaModelException {
        boolean cacheHashCode = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).booleanValue();
        boolean isCacheable = cacheHashCode
                && generatorsCommonMethodsDelegate.areAllFinalFields(data.getCheckedFields());
        String cachingField = "";
        if (isCacheable) {
            cachingField = (String) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.HASHCODE_CACHING_FIELD);
        }

        IJavaElement currentPosition = data.getElementPosition();
        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient int " + cachingField + ";\n\n";
            objectClass.createField(fieldSrc, currentPosition, true, null);
        }

        return MethodGenerations.generateHashCodeMethodContent(data, cachingField);
    }

    @Override
    public Set<String> getLibrariesToImport(EqualsHashCodeDialogData data) {
        return Collections.singleton(CommonsLangLibraryUtils.getHashCodeBuilderLibrary(useCommonsLang3));
    }

}
