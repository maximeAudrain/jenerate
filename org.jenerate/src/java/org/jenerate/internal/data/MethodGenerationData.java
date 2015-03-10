package org.jenerate.internal.data;

import org.eclipse.jdt.core.IField;

/**
 * Defines the default data needed for the generation of methods. This mainly concerns fields that are common to all
 * method generation.
 * 
 * @author maudrain
 */
public interface MethodGenerationData extends GenerationData {

    /**
     * @return the fields of the class where the method will be generated
     */
    IField[] getFields();

    /**
     * @return {@code true} if handling should be done because the super class already has an implementation of the
     *         method, {@code false} otherwise
     */
    boolean isAppendSuper();

    /**
     * @return {@code true} if the comments should be generated for the method, {@code false} otherwise
     */
    boolean isGenerateComment();
}
