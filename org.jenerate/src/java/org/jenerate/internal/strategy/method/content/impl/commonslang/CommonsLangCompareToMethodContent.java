package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.LinkedHashSet;

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

public class CommonsLangCompareToMethodContent extends
        AbstractMethodContent<CompareToMethodSkeleton, CompareToGenerationData> {

    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public String getMethodContent(IType objectClass, CompareToGenerationData data) throws Exception {
        boolean generify = isGenerifyCompareTo(objectClass, isComparableImplementedOrExtendedInSupertype(objectClass));
        return createCompareToMethodContent(data, generify, objectClass);
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(CompareToGenerationData data) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        linkedHashSet.add(CommonsLangMethodContentLibraries.getCompareToBuilderLibrary(useCommonsLang3));
        return linkedHashSet;
    }

    @Override
    public Class<CompareToMethodSkeleton> getRelatedMethodSkeletonClass() {
        return CompareToMethodSkeleton.class;
    }

    /**
     * XXX already there in the skeleton, extract at one point
     */
    private boolean isGenerifyCompareTo(IType objectClass, boolean implementedOrExtendedInSuperType) {
        boolean generifyPreference = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).booleanValue();
        return generifyPreference && generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)
                && !implementedOrExtendedInSuperType;
    }

    private boolean isComparableImplementedOrExtendedInSupertype(IType objectClass) throws Exception {
        return javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable");
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
