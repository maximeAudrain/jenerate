package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;

/**
 * Specific implementation of the {@link MethodContent} for {@link EqualsMethodSkeleton} using commons-lang[3].
 *
 * @author maudrain
 */
public class CommonsLangEqualsMethodContent extends
        AbstractMethodContent<EqualsMethodSkeleton, EqualsHashCodeGenerationData> {

    public CommonsLangEqualsMethodContent(StrategyIdentifier strategyIdentifier, PreferencesManager preferencesManager) {
        super(strategyIdentifier, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws JavaModelException {
        return createEqualsMethodContent(data, objectClass);
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        linkedHashSet.add(CommonsLangMethodContentLibraries.getEqualsBuilderLibrary(getStrategyIdentifier()));
        return linkedHashSet;
    }

    @Override
    public Class<EqualsMethodSkeleton> getRelatedMethodSkeletonClass() {
        return EqualsMethodSkeleton.class;
    }

    private String createEqualsMethodContent(EqualsHashCodeGenerationData data, IType objectClass)
            throws JavaModelException {
        StringBuffer content = new StringBuffer();
        String elementName = objectClass.getElementName();
        content.append(MethodContentGenerations.createEqualsContentPrefix(data, objectClass));
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
            String fieldName = MethodContentGenerations.getFieldAccessorString(checkedFields[i],
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
