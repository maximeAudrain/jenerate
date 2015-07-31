package org.jenerate.internal.strategy.method.skeleton;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;

/**
 * Defines the {@link MethodSkeleton} strategy of a method. A {@link MethodSkeleton} is related to a
 * {@link CommandIdentifier} in the principle that one {@link CommandIdentifier} can generate N {@link MethodSkeleton}s.
 * A {@link MethodSkeleton} has a name and 1..N method arguments as well as an ordered set of libraries to be imported
 * for this skeleton to compile once generated. One can generate the full method string using the
 * {@link MethodSkeleton#getMethod(IType, MethodGenerationData, String)}.
 * 
 * @author maudrain
 * @param <T> the type of {@link MethodGenerationData} related to this {@link MethodSkeleton}
 */
public interface MethodSkeleton<T extends MethodGenerationData> {

    /**
     * Gets the full method string given the provided arguments
     * 
     * @param objectClass the object class where the method will be generated
     * @param data the generation data containing information provided by the user on how the method should be generated
     * @param methodContent the string content of the method
     * @return the full generated string of the method
     * @throws Exception if a problem occurs during the code generation
     */
    String getMethod(IType objectClass, T data, String methodContent) throws Exception;

    /**
     * @return the command identifier for this {@link MethodSkeleton}
     */
    CommandIdentifier getCommandIdentifier();

    /**
     * @return the name of the method
     */
    String getMethodName();

    /**
     * Gets the arguments of the method. Those arguments can be different depending on the object class, e.g generic
     * arguments, etc...
     * 
     * @param objectClass the object class where the method will be generated
     * @return the arguments of the method
     * @throws Exception if a problem occurs when retrieving the method arguments
     */
    String[] getMethodArguments(IType objectClass) throws Exception;

}
