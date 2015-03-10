package org.jenerate.internal.data.impl;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;

/**
 * XXX Needs protection and consider using builder pattern
 * 
 * @author maudrain
 */
public class CompareToMethodGenerationData extends AbstractMethodGenerationData {

    private final IType objectClass;
    private final boolean generify;

    public CompareToMethodGenerationData(IField[] checkedFields, IType objectClass, boolean appendSuper,
            boolean generateComment, boolean generify) {
        super(checkedFields, appendSuper, generateComment);
        this.objectClass = objectClass;
        this.generify = generify;
    }

    public IType getObjectClass() {
        return objectClass;
    }

    public boolean isGenerify() {
        return generify;
    }
}