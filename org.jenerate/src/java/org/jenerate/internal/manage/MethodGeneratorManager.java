package org.jenerate.internal.manage;

import org.jenerate.internal.domain.UserActionIdentifier;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.method.skeleton.MethodSkeleton;
import org.jenerate.internal.lang.generators.MethodGenerator;
import org.jenerate.internal.ui.dialogs.FieldDialog;

/**
 * Manager responsible for retrieving a {@link MethodGenerator} given a unique {@link UserActionIdentifier}.
 * 
 * @author maudrain
 */
public interface MethodGeneratorManager {

    /**
     * Get a {@link MethodGenerator} for the given parameter
     * 
     * @param userActionIdentifier the unique identifier of a specific user action
     * @return the {@link MethodGenerator} for the given parameter
     * @throws IllegalStateException if no {@link MethodGenerator} could be found for a given
     *             {@link UserActionIdentifier}
     */
    <T extends MethodSkeleton<V>, U extends FieldDialog<V>, V extends MethodGenerationData> MethodGenerator<T, U, V> getMethodGenerator(
            UserActionIdentifier userActionIdentifier);

}
