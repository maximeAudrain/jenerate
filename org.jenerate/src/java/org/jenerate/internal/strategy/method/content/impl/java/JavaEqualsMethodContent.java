package org.jenerate.internal.strategy.method.content.impl.java;

import java.util.LinkedHashSet;
import java.util.Objects;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.MethodContent;
import org.jenerate.internal.strategy.method.content.impl.AbstractMethodContent;
import org.jenerate.internal.strategy.method.content.impl.MethodContentGenerations;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;

/**
 * Specific implementation of the {@link MethodContent} for {@link EqualsMethodSkeleton} using JDK 7 {@link Objects}.
 * 
 * @author maudrain
 */
public class JavaEqualsMethodContent extends AbstractMethodContent<EqualsMethodSkeleton, EqualsHashCodeGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String LIBRARY_TO_IMPORT = "java.util.Objects";

    public JavaEqualsMethodContent(PreferencesManager preferencesManager) {
        super(MethodContentStrategyIdentifier.USE_JAVA, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws Exception {
        StringBuffer content = new StringBuffer();
        content.append(MethodContentGenerations.createEqualsContentPrefix(data, objectClass));
        content.append(MethodContentGenerations.createEqualsContent("equals", data, objectClass));
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
