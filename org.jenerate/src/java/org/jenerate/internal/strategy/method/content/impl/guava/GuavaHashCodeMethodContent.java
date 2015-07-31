package org.jenerate.internal.strategy.method.content.impl.guava;

import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
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
public class GuavaHashCodeMethodContent
        extends AbstractMethodContent<HashCodeMethodSkeleton, EqualsHashCodeGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String LIBRARY_TO_IMPORT = "com.google.common.base.Objects";

    public GuavaHashCodeMethodContent(PreferencesManager preferencesManager) {
        super(HashCodeMethodSkeleton.class, MethodContentStrategyIdentifier.USE_GUAVA, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws Exception {
        return MethodContentGenerations.createHashCodeContent("hashCode", data);
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        return MethodContentGenerations.createSingletonLinkedHashSet(LIBRARY_TO_IMPORT);
    }
}
