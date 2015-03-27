package org.jenerate.internal.strategy.method.skeleton.impl;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;

public class HashCodeMethodSkeleton extends AbstractMethodSkeleton<EqualsHashCodeGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String HASH_CODE_METHOD_NAME = "hashCode";

    public HashCodeMethodSkeleton(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethod(IType objectClass, EqualsHashCodeGenerationData data, String methodContent)
            throws JavaModelException {
        boolean addOverride = addOverride(objectClass);
        return createHashCodeMethod(data, addOverride, methodContent);
    }

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.EQUALS_HASH_CODE;
    }

    @Override
    public String getMethodName() {
        return HASH_CODE_METHOD_NAME;
    }

    @Override
    public String[] getMethodArguments(IType objectClass) throws Exception {
        return new String[0];
    }

    private String createHashCodeMethod(EqualsHashCodeGenerationData data, boolean addOverride, String methodContent) {

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#hashCode()\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public int hashCode() {\n");
        content.append(methodContent);
        content.append("}\n\n");

        return content.toString();
    }

}
