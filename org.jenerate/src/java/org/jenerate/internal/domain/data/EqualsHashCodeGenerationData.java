package org.jenerate.internal.domain.data;

import org.jenerate.internal.domain.hashcode.IInitMultNumbers;

/**
 * Extension of the {@link MethodGenerationData} for the equals and hashCode methods generation
 * 
 * @author maudrain
 */
public interface EqualsHashCodeGenerationData extends MethodGenerationData {

    /**
     * @return {@code true} if object references should be compared for the equals method generation, {@code false}
     *         otherwise.
     */
    boolean compareReferences();

    /**
     * @return {@code true} if class comparison should be used instead of instanceOf for the equals method generation,
     *         {@code false} otherwise.
     */
    boolean useClassComparison();

    /**
     * @return {@code true} if {@link Class#cast(Object)} and {@link Class#isInstance(Object)} should be used for the equals method generation,
     *         {@code false} otherwise.
     */
    boolean useClassCast();

    /**
     * @return the odd numbers to be used for the hashCode generation
     */
    IInitMultNumbers getInitMultNumbers();

}
