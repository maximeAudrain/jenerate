package org.jenerate.internal.strategy.method.skeleton.impl;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;

/**
 * Specific implementation of the {@link MethodSkeleton} for the toString method.
 * 
 * @author maudrain
 */
public class ToStringMethodSkeleton extends AbstractMethodSkeleton<ToStringGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String TO_STRING_METHOD_NAME = "toString";

    public ToStringMethodSkeleton(PreferencesManager preferencesManager) {
        super(preferencesManager);
    }

    @Override
    public String getMethod(IType objectClass, ToStringGenerationData data, String methodContent)
            throws JavaModelException {
        boolean addOverride = addOverride(objectClass);
        return createToStringMethod(data, addOverride, methodContent);
    }

    @Override
    public MethodsGenerationCommandIdentifier getCommandIdentifier() {
        return MethodsGenerationCommandIdentifier.TO_STRING;
    }

    @Override
    public String getMethodName() {
        return TO_STRING_METHOD_NAME;
    }

    @Override
    public String[] getMethodArguments(IType objectClass) throws Exception {
        return new String[0];
    }

    private String createToStringMethod(ToStringGenerationData data, boolean addOverride, String methodContent) {

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/**\n");
            content.append(" * {@inheritDoc}\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public String toString() {\n");
        content.append(methodContent);
        content.append("}\n\n");

        return content.toString();
    }

}
