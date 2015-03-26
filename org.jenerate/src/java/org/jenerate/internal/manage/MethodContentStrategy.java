package org.jenerate.internal.manage;

import java.util.Set;

import org.jenerate.internal.domain.method.Method;

public interface MethodContentStrategy<T extends Method> {
    
    String getMethodContent();
    
    Set<String> getLibrariesToImport();
    

}
