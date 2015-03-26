package org.jenerate.internal.domain.method.content.tostring;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.data.ToStringDialogData;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.CommonsLangLibraryUtils;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

public class CommonsLangToStringMethodContent extends AbstractMethodContent<ToStringDialogData> {
    
    private final boolean useCommonsLang3;

    public CommonsLangToStringMethodContent(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate, boolean useCommonsLang3) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
        this.useCommonsLang3 = useCommonsLang3;
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
        Set<String> libraries = new HashSet<String>();
        String toStringBuilderLibrary = CommonsLangLibraryUtils.getToStringBuilderLibrary(useCommonsLang3);
        libraries.add(toStringBuilderLibrary);
        if (!ToStringStyle.NO_STYLE.equals(data.getToStringStyle())) {
            String styleLibrary = ToStringStyle.getToStringStyleLibrary(useCommonsLang3);
            libraries.add(styleLibrary);
        }
        return libraries;
    }
}
