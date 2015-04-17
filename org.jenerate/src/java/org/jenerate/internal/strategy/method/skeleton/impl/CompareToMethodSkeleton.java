package org.jenerate.internal.strategy.method.skeleton.impl;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodsGenerationCommandIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.MethodGenerations;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

/**
 * Specific implementation of the {@link MethodSkeleton} for the compareTo method.
 * 
 * @author maudrain
 */
public class CompareToMethodSkeleton extends AbstractMethodSkeleton<CompareToGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String COMPARE_TO_METHOD_NAME = "compareTo";

    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CompareToMethodSkeleton(PreferencesManager preferencesManager,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(preferencesManager);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public String getMethod(IType objectClass, CompareToGenerationData data, String methodContent) throws Exception {
        boolean implementedOrExtendedInSuperType = isComparableImplementedOrExtendedInSupertype(objectClass);
        boolean generify = MethodGenerations.generifyCompareTo(objectClass, implementedOrExtendedInSuperType,
                preferencesManager);
        boolean addOverride = addOverride(objectClass);
        if (!implementedOrExtendedInSuperType) {
            String interfaceName = "Comparable";
            if (generify) {
                interfaceName = "Comparable<" + objectClass.getElementName() + ">";
            }
            javaInterfaceCodeAppender.addSuperInterface(objectClass, interfaceName);
        }
        return createCompareToMethod(objectClass, data, generify, addOverride, methodContent);
    }

    @Override
    public MethodsGenerationCommandIdentifier getCommandIdentifier() {
        return MethodsGenerationCommandIdentifier.COMPARE_TO;
    }

    @Override
    public String getMethodName() {
        return COMPARE_TO_METHOD_NAME;
    }

    @Override
    public String[] getMethodArguments(IType objectClass) throws Exception {
        if (MethodGenerations.generifyCompareTo(objectClass, isComparableImplementedOrExtendedInSupertype(objectClass),
                preferencesManager)) {
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

    private boolean isComparableImplementedOrExtendedInSupertype(IType objectClass) throws Exception {
        return javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(objectClass, "Comparable");
    }

    private String createCompareToMethod(IType objectClass, CompareToGenerationData data, boolean generify,
            boolean addOverride, String methodContent) {

        StringBuffer content = new StringBuffer();
        if (data.generateComment()) {
            content.append("/**\n");
            content.append(" * {@inheritDoc}\n");
            content.append(" */\n");
        }

        if (addOverride) {
            content.append("@Override\n");
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
