package org.jenerate.internal.manage;

import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.generate.method.MethodGenerator;
import org.jenerate.internal.strategy.method.skeleton.MethodSkeleton;
import org.jenerate.internal.ui.dialogs.FieldDialog;

/**
 * Manager responsible for retrieving a {@link MethodGenerator} given a unique {@link CommandIdentifier}.
 * 
 * @author maudrain
 */
public interface MethodGeneratorManager {

    /**
     * Get a {@link MethodGenerator} for the given parameter
     * 
     * @param commandIdentifier the unique identifier of a specific command
     * @return the {@link MethodGenerator} for the given parameter
     * @throws IllegalStateException if no {@link MethodGenerator} could be found for a given {@link CommandIdentifier}
     */
    <T extends MethodSkeleton<V>, U extends FieldDialog<V>, V extends MethodGenerationData> MethodGenerator<T, U, V> getMethodGenerator(
            CommandIdentifier commandIdentifier);

}
