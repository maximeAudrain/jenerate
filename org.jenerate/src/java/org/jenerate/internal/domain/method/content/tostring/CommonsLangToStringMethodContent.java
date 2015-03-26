package org.jenerate.internal.domain.method.content.tostring;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.ToStringDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.domain.method.content.CommonsLangLibraryUtils;
import org.jenerate.internal.domain.method.skeleton.impl.ToStringMethod;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class CommonsLangToStringMethodContent extends AbstractMethodContent<ToStringMethod, ToStringDialogData> {

    public CommonsLangToStringMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethodContent(IType objectClass, ToStringDialogData data) throws JavaModelException {
        boolean cacheToString = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.CACHE_TOSTRING)).booleanValue();
        boolean isCacheable = cacheToString
                && generatorsCommonMethodsDelegate.areAllFinalFields(data.getCheckedFields());
        String cachingField = "";
        if (isCacheable) {
            cachingField = (String) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.TOSTRING_CACHING_FIELD);
        }
        IJavaElement currentPosition = data.getElementPosition();
        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient String " + cachingField + ";\n\n";
            objectClass.createField(fieldSrc, currentPosition, true, null);
        }
        return MethodGenerations.generateToStringMethodContent(data, cachingField);
    }

    @Override
    public Set<String> getLibrariesToImport(ToStringDialogData data) {
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        Set<String> libraries = new HashSet<String>();
        String toStringBuilderLibrary = CommonsLangLibraryUtils.getToStringBuilderLibrary(useCommonsLang3);
        libraries.add(toStringBuilderLibrary);
        if (!ToStringStyle.NO_STYLE.equals(data.getToStringStyle())) {
            String styleLibrary = ToStringStyle.getToStringStyleLibrary(useCommonsLang3);
            libraries.add(styleLibrary);
        }
        return libraries;
    }

    @Override
    public Class<ToStringMethod> getRelatedMethodSkeletonClass() {
        return ToStringMethod.class;
    }
}
