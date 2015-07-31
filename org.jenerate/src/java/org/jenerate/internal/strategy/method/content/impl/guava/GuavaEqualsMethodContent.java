package org.jenerate.internal.strategy.method.content.impl.guava;

import java.util.LinkedHashSet;

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
public class GuavaEqualsMethodContent
        extends AbstractMethodContent<EqualsMethodSkeleton, EqualsHashCodeGenerationData> {

    /**
     * Public for testing purpose
     */
    public static final String LIBRARY_TO_IMPORT = "com.google.common.base.Objects";

    public GuavaEqualsMethodContent(PreferencesManager preferencesManager) {
        super(EqualsMethodSkeleton.class, MethodContentStrategyIdentifier.USE_GUAVA, preferencesManager);
    }

    @Override
    public String getMethodContent(IType objectClass, EqualsHashCodeGenerationData data) throws Exception {
        StringBuffer content = new StringBuffer();
        content.append(MethodContentGenerations.createEqualsContentPrefix(data, objectClass));
        content.append(MethodContentGenerations.createEqualsContent("equal", data, objectClass));
        return content.toString();
    }

    @Override
    public LinkedHashSet<String> getLibrariesToImport(EqualsHashCodeGenerationData data) {
        return MethodContentGenerations.createSingletonLinkedHashSet(LIBRARY_TO_IMPORT);
    }
}
