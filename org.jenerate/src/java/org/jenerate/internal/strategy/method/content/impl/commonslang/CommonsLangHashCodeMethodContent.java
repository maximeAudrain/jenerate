package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;

public class CommonsLangHashCodeMethodContent extends
        AbstractMethodContent<HashCodeMethodSkeleton, EqualsHashCodeGenerationData> {

    public CommonsLangHashCodeMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws JavaModelException {
        String cachingField = cacheHashCode(objectClass, data);
        return createHashCodeMethodContent(data, cachingField);
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        linkedHashSet.add(CommonsLangMethodContentLibraries.getHashCodeBuilderLibrary(useCommonsLang3));
        return linkedHashSet;
    }

    @Override
    public Class<HashCodeMethodSkeleton> getRelatedMethodSkeletonClass() {
        return HashCodeMethodSkeleton.class;
    }

    /**
     * XXX same as cacheToString in the toString content strategy
     */
    private String cacheHashCode(IType objectClass, EqualsHashCodeGenerationData data) throws JavaModelException {
        boolean cacheHashCode = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.CACHE_HASHCODE)).booleanValue();
        boolean isCacheable = cacheHashCode
                && generatorsCommonMethodsDelegate.areAllFinalFields(data.getCheckedFields());
        String cachingField = "";
        if (isCacheable) {
            cachingField = (String) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.HASHCODE_CACHING_FIELD);
        }

        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            IJavaElement currentPosition = data.getElementPosition();
            String fieldSrc = "private transient int " + cachingField + ";\n\n";
            objectClass.createField(fieldSrc, currentPosition, true, null);
        }
        return cachingField;
    }

    private String createHashCodeMethodContent(EqualsHashCodeGenerationData data, String cachingField)
            throws JavaModelException {
        StringBuffer content = new StringBuffer();
        String hashCodeBuilderString = createHashCodeBuilderString(data);

        if (cachingField.isEmpty()) {
            content.append("return ");
            content.append(hashCodeBuilderString);
        } else {
            content.append("if (" + cachingField + "== 0) {\n");
            content.append(cachingField + " = ");
            content.append(hashCodeBuilderString);
            content.append("}\n");
            content.append("return " + cachingField + ";\n");
        }
        return content.toString();
    }

    private static String createHashCodeBuilderString(EqualsHashCodeGenerationData data) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        content.append("new HashCodeBuilder(");
        content.append(data.getInitMultNumbers().getValue());
        content.append(")");
        if (data.getAppendSuper()) {
            content.append(".appendSuper(super.hashCode())");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(MethodContentGenerations.generateFieldAccessor(checkedFields[i],
                    data.getUseGettersInsteadOfFields()));
            content.append(")");
        }
        content.append(".toHashCode();\n");

        return content.toString();
    }
}
