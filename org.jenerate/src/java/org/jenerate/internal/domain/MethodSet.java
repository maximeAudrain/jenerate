package org.jenerate.internal.domain;

import java.util.Set;

import org.jenerate.internal.domain.method.Method;

/**
 * Defines a set of methods. A set of methods can be generated on user action.
 * 
 * @author maudrain
 */
public interface MethodSet {

    Set<Method> getMethods();
    
    String getUserActionIdentifier();
}
