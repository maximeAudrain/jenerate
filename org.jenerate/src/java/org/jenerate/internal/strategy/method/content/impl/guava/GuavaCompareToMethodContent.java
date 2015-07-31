package org.jenerate.internal.strategy.method.content.impl.guava;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.MethodGenerations;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.CompareToMethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

/**
 * Specific implementation of the {@link MethodContent} for {@link CompareToMethodSkeleton} using guava.
 * 
 * @author maudrain
 */
public class GuavaCompareToMethodContent
        extends AbstractMethodContent<CompareToMethodSkeleton, CompareToGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String LIBRARY_TO_IMPORT = "com.google.common.collect.ComparisonChain";

    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public GuavaCompareToMethodContent(PreferencesManager preferencesManager,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(CompareToMethodSkeleton.class, MethodContentStrategyIdentifier.USE_GUAVA, preferencesManager);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public String getMethodContent(IType objectClass, CompareToGenerationData data) throws Exception {
        boolean generify = MethodGenerations.generifyCompareTo(objectClass,
                isComparableImplementedOrExtendedInSupertype(objectClass), getPreferencesManager());
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

        content.append("return ComparisonChain.start()");
        IField[] checkedFields = data.getCheckedFields();
        for (IField checkedField : checkedFields) {
            content.append(".compare(");
            String fieldName = MethodContentGenerations.getFieldAccessorString(checkedField,
                    data.useGettersInsteadOfFields());
            content.append(fieldName);
            content.append(", ");
            content.append(other);
            content.append(".");
            content.append(fieldName);
            content.append(")");
        }
        content.append(".result();\n");
        return content.toString();
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(CompareToGenerationData data) {
        return MethodContentGenerations.createSingletonLinkedHashSet(LIBRARY_TO_IMPORT);
    }

    private boolean isComparableImplementedOrExtendedInSupertype(IType objectClass) throws Exception {
        return javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable");
    }

}
