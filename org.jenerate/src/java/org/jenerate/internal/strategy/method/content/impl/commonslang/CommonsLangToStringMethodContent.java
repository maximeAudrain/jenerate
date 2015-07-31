package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.ToStringMethodSkeleton;

/**
 * Specific implementation of the {@link MethodContent} for {@link ToStringMethodSkeleton} using commons-lang[3].
 * 
 * @author maudrain
 */
public class CommonsLangToStringMethodContent
        extends AbstractMethodContent<ToStringMethodSkeleton, ToStringGenerationData> {

    public CommonsLangToStringMethodContent(StrategyIdentifier strategyIdentifier,
            PreferencesManager preferencesManager) {
        super(ToStringMethodSkeleton.class, strategyIdentifier, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, ToStringGenerationData data) throws JavaModelException {
        boolean cacheProperty = getPreferencesManager().getCurrentPreferenceValue(JeneratePreferences.CACHE_TOSTRING)
                .booleanValue();
        String cachingField = getPreferencesManager()
                .getCurrentPreferenceValue(JeneratePreferences.TOSTRING_CACHING_FIELD);
        boolean isCacheable = MethodContentGenerations.createField(objectClass, data, cacheProperty, cachingField,
                String.class);
        return createToStringMethodContent(data, isCacheable, cachingField);
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(ToStringGenerationData data) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        String toStringBuilderLibrary = CommonsLangMethodContentLibraries
                .getToStringBuilderLibrary(getStrategyIdentifier());
        linkedHashSet.add(toStringBuilderLibrary);
        if (!CommonsLangToStringStyle.NO_STYLE.equals(data.getToStringStyle())) {
            String styleLibrary = CommonsLangMethodContentLibraries.getToStringStyleLibrary(getStrategyIdentifier());
            linkedHashSet.add(styleLibrary);
        }
        return linkedHashSet;
    }

    private String createToStringMethodContent(ToStringGenerationData data, boolean isCacheable, String cachingField)
            throws JavaModelException {
        StringBuffer content = new StringBuffer();
        String toStringBuilderString = createToStringBuilderString(data);

        if (isCacheable) {
            content.append("if (" + cachingField + "== null) {\n");
            content.append(cachingField + " = ");
            content.append(toStringBuilderString);
            content.append("}\n");
            content.append("return " + cachingField + ";\n");
        } else {
            content.append("return ");
            content.append(toStringBuilderString);
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
        if (data.appendSuper()) {
            content.append(".appendSuper(super.toString())");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(\"");
            content.append(checkedFields[i].getElementName());
            content.append("\", ");
            content.append(MethodContentGenerations.getFieldAccessorString(checkedFields[i],
                    data.useGettersInsteadOfFields()));
            content.append(")");
        }
        content.append(".toString();\n");

        return content.toString();
    }
}
