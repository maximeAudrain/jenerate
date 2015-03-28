package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.ToStringMethodSkeleton;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;

public class CommonsLangToStringMethodContent extends
        AbstractMethodContent<ToStringMethodSkeleton, ToStringGenerationData> {

    public CommonsLangToStringMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethodContent(IType objectClass, ToStringGenerationData data) throws JavaModelException {
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
        return createToStringMethodContent(data, cachingField);
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(ToStringGenerationData data) {
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        String toStringBuilderLibrary = CommonsLangMethodContentLibraries.getToStringBuilderLibrary(useCommonsLang3);
        linkedHashSet.add(toStringBuilderLibrary);
        if (!CommonsLangToStringStyle.NO_STYLE.equals(data.getToStringStyle())) {
            String styleLibrary = CommonsLangToStringStyle.getToStringStyleLibrary(useCommonsLang3);
            linkedHashSet.add(styleLibrary);
        }
        return linkedHashSet;
    }

    @Override
    public Class<ToStringMethodSkeleton> getRelatedMethodSkeletonClass() {
        return ToStringMethodSkeleton.class;
    }

    private String createToStringMethodContent(ToStringGenerationData data, String cachingField)
            throws JavaModelException {
        StringBuffer content = new StringBuffer();
        String toStringBuilderString = createToStringBuilderString(data);

        if (cachingField.isEmpty()) {
            content.append("return ");
            content.append(toStringBuilderString);

        } else {
            content.append("if (" + cachingField + "== null) {\n");
            content.append(cachingField + " = ");
            content.append(toStringBuilderString);
            content.append("}\n");
            content.append("return " + cachingField + ";\n");
        }
        return content.toString();
    }

    private static String createToStringBuilderString(ToStringGenerationData data) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        CommonsLangToStringStyle toStringStyle = data.getToStringStyle();
        if (CommonsLangToStringStyle.NO_STYLE.equals(toStringStyle)) {
            content.append("new ToStringBuilder(this)");
        } else {
            content.append("new ToStringBuilder(this, ");
            content.append(toStringStyle.getFullStyle());
            content.append(")");
        }
        if (data.getAppendSuper()) {
            content.append(".appendSuper(super.toString())");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(\"");
            content.append(checkedFields[i].getElementName());
            content.append("\", ");
            content.append(MethodContentGenerations.generateFieldAccessor(checkedFields[i],
                    data.getUseGettersInsteadOfFields()));
            content.append(")");
        }
        content.append(".toString();\n");

        return content.toString();
    }
}
