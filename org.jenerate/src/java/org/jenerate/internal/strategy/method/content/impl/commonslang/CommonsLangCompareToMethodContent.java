package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.MethodGenerations;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

/**
 * Specific implementation of the {@link MethodContent} for {@link CompareToMethodSkeleton} using commons-lang[3].
 * 
 * @author maudrain
 */
public class CommonsLangCompareToMethodContent
        extends AbstractMethodContent<CompareToMethodSkeleton, CompareToGenerationData> {

    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CommonsLangCompareToMethodContent(StrategyIdentifier strategyIdentifier,
            PreferencesManager preferencesManager, JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(CompareToMethodSkeleton.class, strategyIdentifier, preferencesManager);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public String getMethodContent(IType objectClass, CompareToGenerationData data) throws Exception {
        boolean generify = MethodGenerations.generifyCompareTo(objectClass,
                isComparableImplementedOrExtendedInSupertype(objectClass), getPreferencesManager());
        return createCompareToMethodContent(data, generify, objectClass);
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(CompareToGenerationData data) {
        return MethodContentGenerations.createSingletonLinkedHashSet(
                CommonsLangMethodContentLibraries.getCompareToBuilderLibrary(getStrategyIdentifier()));
    }

    private boolean isComparableImplementedOrExtendedInSupertype(IType objectClass) throws Exception {
        return javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable");
    }

    private String createCompareToMethodContent(CompareToGenerationData data, boolean generify, IType objectClass) {
        StringBuffer content = new StringBuffer();
        String other = "other";
        if (!generify) {
            String className = objectClass.getElementName();
            content.append(className);
            content.append(" castOther = (");
            content.append(className);
            content.append(") other;\n");
            other = "castOther";
        }

        content.append("return new CompareToBuilder()");
        if (data.appendSuper()) {
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
