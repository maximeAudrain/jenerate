package org.jenerate.internal.strategy.method.skeleton.impl;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;

public class EqualsMethodSkeleton extends AbstractMethodSkeleton<EqualsHashCodeGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String EQUALS_METHOD_NAME = "equals";

    public EqualsMethodSkeleton(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethod(IType objectClass, EqualsHashCodeGenerationData data, String methodContent)
            throws JavaModelException {
        boolean addOverride = addOverride(objectClass);
        return createEqualsMethod(data, addOverride, methodContent);
    }

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.EQUALS_HASH_CODE;
    }

    @Override
    public String getMethodName() {
        return EQUALS_METHOD_NAME;
    }

    @Override
    public String[] getMethodArguments(IType objectClass) throws Exception {
        return new String[] { "QObject;" };
    }

    private String createEqualsMethod(EqualsHashCodeGenerationData data, boolean addOverride, String methodContent) {

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Object#equals(java.lang.Object)\n");
            content.append(" */\n");
        }
        if (addOverride) {
            content.append("@Override\n");
        }
        content.append("public boolean equals(final Object other) {\n");
        content.append(methodContent);
        content.append("}\n\n");

        return content.toString();
    }
}
