package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class CommonsLangCompareToMethodContent extends AbstractMethodContent<CompareToMethodSkeleton, CompareToGenerationData> {

    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public String getMethodContent(IType objectClass, CompareToGenerationData data) throws Exception {
        boolean implementedOrExtendedInSuperType = javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(
                objectClass, "Comparable");
        boolean generifyPreference = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).booleanValue();
        boolean generify = generifyPreference
                && generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)
                && !implementedOrExtendedInSuperType;
        return createCompareToMethodContent(data, generify, objectClass);
    }

    @Override
    public Set<String> getLibrariesToImport(CompareToGenerationData data) {
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        return Collections.singleton(CommonsLangMethodContentLibraries.getCompareToBuilderLibrary(useCommonsLang3));
    }

    @Override
    public Class<CompareToMethodSkeleton> getRelatedMethodSkeletonClass() {
        return CompareToMethodSkeleton.class;
    }
    
    private String createCompareToMethodContent(CompareToGenerationData data, boolean generify, IType objectClass) {
        String className = objectClass.getElementName();
        StringBuffer content = new StringBuffer();
        String other;
        if (generify) {
            other = "other";
        } else {
            content.append(className);
            content.append(" castOther = (");
            content.append(className);
            content.append(") other;\n");

            other = "castOther";
        }

        content.append("return new CompareToBuilder()");
        if (data.getAppendSuper()) {
            content.append(".appendSuper(super.compareTo(other))");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            String fieldName = checkedFields[i].getElementName();
            content.append(".append(");
            content.append(fieldName);
            content.append(", " + other + ".");
            content.append(fieldName);
            content.append(")");
        }
        content.append(".toComparison();\n");
        return content.toString();
    }

}
