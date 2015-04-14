package org.jenerate.internal.strategy.method.content.impl.guava;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.ToStringMethodSkeleton;

/**
 * Specific implementation of the {@link MethodContent} for {@link ToStringMethodSkeleton} using guava.
 * 
 * @author maudrain
 */
public class GuavaToStringMethodContent extends AbstractMethodContent<ToStringMethodSkeleton, ToStringGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String LIBRARY_TO_IMPORT = "com.google.common.base.MoreObjects";

    public GuavaToStringMethodContent(StrategyIdentifier strategyIdentifier, PreferencesManager preferencesManager) {
        super(strategyIdentifier, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, ToStringGenerationData data) throws Exception {
        StringBuffer content = new StringBuffer();
        content.append("return MoreObjects.toStringHelper(this)");
        if (data.getAppendSuper()) {
            content.append(".add(\"super\", super.toString())");
        }
        IField[] checkedFields = data.getCheckedFields();
        for (int i = 0; i < checkedFields.length; i++) {
            content.append(".add(\"");
            content.append(checkedFields[i].getElementName());
            content.append("\", ");
            content.append(MethodContentGenerations.getFieldAccessorString(checkedFields[i],
                    data.getUseGettersInsteadOfFields()));
            content.append(")");
        }
        content.append(".toString();\n");
        return content.toString();
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(ToStringGenerationData data) {
        LinkedHashSet<String> toReturn = new LinkedHashSet<String>();
        toReturn.add(LIBRARY_TO_IMPORT);
        return toReturn;
    }

    @Override
    public Class<ToStringMethodSkeleton> getRelatedMethodSkeletonClass() {
        return ToStringMethodSkeleton.class;
    }

}
