package org.jenerate.internal.strategy.method.skeleton.impl;

import org.eclipse.jdt.core.IType;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.util.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class CompareToMethodSkeleton extends AbstractMethodSkeleton<CompareToGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String COMPARE_TO_METHOD_NAME = "compareTo";

    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CompareToMethodSkeleton(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public String getMethod(IType objectClass, CompareToGenerationData data, String methodContent) throws Exception {
        boolean implementedOrExtendedInSuperType = isComparableImplementedOrExtendedInSupertype(objectClass);
        boolean generify = isGenerifyCompareTo(objectClass, implementedOrExtendedInSuperType);
        if (!implementedOrExtendedInSuperType) {
            String interfaceName = "Comparable";
            if (generify) {
                interfaceName = "Comparable<" + objectClass.getElementName() + ">";
            }
            javaInterfaceCodeAppender.addSuperInterface(objectClass, interfaceName);
        }
        return createCompareToMethod(objectClass, data, generify, methodContent);
    }

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.COMPARE_TO;
    }

    @Override
    public String getMethodName() {
        return COMPARE_TO_METHOD_NAME;
    }

    @Override
    public String[] getMethodArguments(IType objectClass) throws Exception {
        if (isGenerifyCompareTo(objectClass, isComparableImplementedOrExtendedInSupertype(objectClass))) {
            String elementName = objectClass.getElementName();
            return new String[] { createArgument(elementName) };
        }
        return new String[] { createArgument("Object") };
    }

    /**
     * package private for testing
     */
    String createArgument(String elementName) {
        return "Q" + elementName + ";";
    }

    private boolean isGenerifyCompareTo(IType objectClass, boolean implementedOrExtendedInSuperType) {
        boolean generifyPreference = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).booleanValue();
        return generifyPreference && generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)
                && !implementedOrExtendedInSuperType;
    }

    private boolean isComparableImplementedOrExtendedInSupertype(IType objectClass) throws Exception {
        return javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable");
    }

    private String createCompareToMethod(IType objectClass, CompareToGenerationData data, boolean generify,
            String methodContent) {

        StringBuffer content = new StringBuffer();
        if (data.getGenerateComment()) {
            content.append("/* (non-Javadoc)\n");
            content.append(" * @see java.lang.Comparable#compareTo(java.lang.Object)\n");
            content.append(" */\n");
        }

        String className = objectClass.getElementName();
        if (generify) {
            content.append("public int compareTo(final " + className + " other) {\n");
        } else {
            content.append("public int compareTo(final Object other) {\n");
        }
        content.append(methodContent);
        content.append("}\n\n");

        return content.toString();
    }
}
