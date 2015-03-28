package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.Flags;
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

public class CommonsLangToStringMethodContent extends
        AbstractMethodContent<ToStringMethodSkeleton, ToStringGenerationData> {

    public CommonsLangToStringMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager) {
        super(methodContentStrategyIdentifier, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, ToStringGenerationData data) throws JavaModelException {
        String cachingField = cacheToString(objectClass, data);
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

    /**
     * XXX same as cacheHashCode in the hashCode content strategy
     */
    private String cacheToString(IType objectClass, ToStringGenerationData data) throws JavaModelException {
        boolean cacheToString = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.CACHE_TOSTRING)).booleanValue();
        boolean isCacheable = cacheToString && areAllFinalFields(data.getCheckedFields());
        String cachingField = "";
        if (isCacheable) {
            cachingField = (String) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.TOSTRING_CACHING_FIELD);
        }

        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            IJavaElement currentPosition = data.getElementPosition();
            String fieldSrc = "private transient String " + cachingField + ";\n\n";
            objectClass.createField(fieldSrc, currentPosition, true, null);
        }
        return cachingField;
    }

    private boolean areAllFinalFields(IField[] fields) throws JavaModelException {
        for (int i = 0; i < fields.length; i++) {
            if (!Flags.isFinal(fields[i].getFlags())) {
                return false;
            }
        }

        return true;
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
