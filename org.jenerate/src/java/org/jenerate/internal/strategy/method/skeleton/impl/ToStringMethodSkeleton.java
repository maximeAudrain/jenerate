package org.jenerate.internal.strategy.method.skeleton.impl;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;

public class ToStringMethodSkeleton extends AbstractMethodSkeleton<ToStringGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String TO_STRING_METHOD_NAME = "toString";

    public ToStringMethodSkeleton(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethod(IType objectClass, ToStringGenerationData data, String methodContent)
            throws JavaModelException {
        boolean addOverride = addOverride(objectClass);
        return createToStringMethod(data, addOverride, methodContent);
    }

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.TO_STRING;
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
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#toString()\n");
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
