package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;

/**
 * Specific implementation of the {@link MethodContent} for {@link HashCodeMethodSkeleton} using commons-lang[3].
 * 
 * @author maudrain
 */
public class CommonsLangHashCodeMethodContent
        extends AbstractMethodContent<HashCodeMethodSkeleton, EqualsHashCodeGenerationData> {

    public CommonsLangHashCodeMethodContent(StrategyIdentifier strategyIdentifier,
            PreferencesManager preferencesManager) {
        super(HashCodeMethodSkeleton.class, strategyIdentifier, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws JavaModelException {
        boolean cacheProperty = getPreferencesManager().getCurrentPreferenceValue(JeneratePreferences.CACHE_HASHCODE)
                .booleanValue();
        String cachingField = getPreferencesManager()
                .getCurrentPreferenceValue(JeneratePreferences.HASHCODE_CACHING_FIELD);
        boolean isCacheable = MethodContentGenerations.createField(objectClass, data, cacheProperty, cachingField,
                int.class);
        return createHashCodeMethodContent(data, isCacheable, cachingField);
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        return MethodContentGenerations.createSingletonLinkedHashSet(
                CommonsLangMethodContentLibraries.getHashCodeBuilderLibrary(getStrategyIdentifier()));
    }

    private String createHashCodeMethodContent(EqualsHashCodeGenerationData data, boolean isCacheable,
            String cachingField) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        String hashCodeBuilderString = createHashCodeBuilderString(data);

        if (isCacheable) {
            content.append("if (" + cachingField + "== 0) {\n");
            content.append(cachingField + " = ");
            content.append(hashCodeBuilderString);
            content.append("}\n");
            content.append("return " + cachingField + ";\n");
        } else {
            content.append("return ");
            content.append(hashCodeBuilderString);
        }
        return content.toString();
    }

    private static String createHashCodeBuilderString(EqualsHashCodeGenerationData data) throws JavaModelException {
        StringBuffer content = new StringBuffer();
        content.append("new HashCodeBuilder(");
        content.append(data.getInitMultNumbers().getValue());
        content.append(")");
        if (data.appendSuper()) {
            content.append(".appendSuper(super.hashCode())");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            content.append(MethodContentGenerations.getFieldAccessorString(checkedFields[i],
                    data.useGettersInsteadOfFields()));
            content.append(")");
        }
        content.append(".toHashCode();\n");

        return content.toString();
    }
}
