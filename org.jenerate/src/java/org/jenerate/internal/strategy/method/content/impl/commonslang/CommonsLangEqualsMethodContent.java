package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;

public class CommonsLangEqualsMethodContent extends
        AbstractMethodContent<EqualsMethodSkeleton, EqualsHashCodeGenerationData> {

    public CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws JavaModelException {
        return createEqualsMethodContent(data, objectClass);
    }

    @Override
    public Set<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        return Collections.singleton(CommonsLangMethodContentLibraries.getEqualsBuilderLibrary(useCommonsLang3));
    }

    @Override
    public Class<EqualsMethodSkeleton> getRelatedMethodSkeletonClass() {
        return EqualsMethodSkeleton.class;
    }

    private String createEqualsMethodContent(EqualsHashCodeGenerationData data, IType objectClass)
            throws JavaModelException {
        String elementName = objectClass.getElementName();
        boolean useBlockInIfStatements = data.getUseBlockInIfStatements();
        StringBuffer content = new StringBuffer();
        if (data.getCompareReferences()) {
            content.append("if (this == other)");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return true;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
        }
        content.append("if ( !(other instanceof ");
        content.append(elementName);
        content.append(") )");
        content.append(useBlockInIfStatements ? "{\n" : "");
        content.append(" return false;");
        content.append(useBlockInIfStatements ? "\n}\n" : "");
        content.append(elementName);
        content.append(" castOther = (");
        content.append(elementName);
        content.append(") other;\n");
        content.append("return new EqualsBuilder()");
        if (data.getAppendSuper()) {
            content.append(".appendSuper(super.equals(other))");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".append(");
            String fieldName = MethodContentGenerations.generateFieldAccessor(checkedFields[i],
                    data.getUseGettersInsteadOfFields());
            content.append(fieldName);
            content.append(", castOther.");
            content.append(fieldName);
            content.append(")");
        }
        content.append(".isEquals();\n");
        return content.toString();
    }
}
