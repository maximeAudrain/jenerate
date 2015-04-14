package org.jenerate.internal.strategy.method.content.impl.guava;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;

/**
 * Specific implementation of the {@link MethodContent} for {@link EqualsMethodSkeleton} using guava.
 * 
 * @author maudrain
 */
public class GuavaEqualsMethodContent extends AbstractMethodContent<EqualsMethodSkeleton, EqualsHashCodeGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String LIBRARY_TO_IMPORT = "com.google.common.base.Objects";

    public GuavaEqualsMethodContent(PreferencesManager preferencesManager) {
        super(MethodContentStrategyIdentifier.USE_GUAVA, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws Exception {
        StringBuffer content = new StringBuffer();
        String elementName = objectClass.getElementName();
        boolean useBlockInIfStatements = data.getUseBlockInIfStatements();
        content.append(MethodContentGenerations.createEqualsContentPrefix(data, objectClass));
        if (data.getAppendSuper()) {
            content.append("if (!super.equals(other))");
            content.append(useBlockInIfStatements ? "{\n" : "");
            content.append(" return false;");
            content.append(useBlockInIfStatements ? "\n}\n" : "");
        }
        content.append(elementName);
        content.append(" castOther = (");
        content.append(elementName);
        content.append(") other;\n");
        content.append("return ");
        String prefix = "";
        IField[] checkedFields = data.getCheckedFields();
        for (IField checkedField : checkedFields) {
            content.append(prefix);
            prefix = " && ";
            content.append("Objects.equal(");
            String fieldName = MethodContentGenerations.getFieldAccessorString(checkedField,
                    data.getUseGettersInsteadOfFields());
            content.append(fieldName);
            content.append(", castOther.");
            content.append(fieldName);
            content.append(")");
        }
        content.append(";\n");
        return content.toString();
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        LinkedHashSet<String> toReturn = new LinkedHashSet<String>();
        toReturn.add(LIBRARY_TO_IMPORT);
        return toReturn;
    }

    @Override
    public Class<EqualsMethodSkeleton> getRelatedMethodSkeletonClass() {
        return EqualsMethodSkeleton.class;
    }

}
