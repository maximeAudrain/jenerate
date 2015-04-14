package org.jenerate.internal.strategy.method.content.impl.guava;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.HashCodeMethodSkeleton;

/**
 * Specific implementation of the {@link MethodContent} for {@link HashCodeMethodSkeleton} using guava.
 * 
 * @author maudrain
 */
public class GuavaHashCodeMethodContent extends
        AbstractMethodContent<HashCodeMethodSkeleton, EqualsHashCodeGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String LIBRARY_TO_IMPORT = "com.google.common.base.Objects";

    public GuavaHashCodeMethodContent(StrategyIdentifier strategyIdentifier, PreferencesManager preferencesManager) {
        super(strategyIdentifier, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws Exception {
        StringBuffer content = new StringBuffer();
        content.append("return Objects.hashCode(");
        if (data.getAppendSuper()) {
            content.append("super.hashCode(), ");
        }
        IField[] checkedFields = data.getCheckedFields();
        String prefix = "";
        for (IField checkedField : checkedFields) {
            content.append(prefix);
            prefix = ", ";
            content.append(MethodContentGenerations.getFieldAccessorString(checkedField,
                    data.getUseGettersInsteadOfFields()));
        }
        content.append(");\n");
        return content.toString();
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        LinkedHashSet<String> toReturn = new LinkedHashSet<String>();
        toReturn.add(LIBRARY_TO_IMPORT);
        return toReturn;
    }

    @Override
    public Class<HashCodeMethodSkeleton> getRelatedMethodSkeletonClass() {
        return HashCodeMethodSkeleton.class;
    }

}
