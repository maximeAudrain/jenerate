package org.jenerate.internal.data.impl;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;

/**
 * XXX Needs protection and consider using builder pattern
 * 
 * @author maudrain
 */
public class EqualsMethodGenerationData extends AbstractMethodGenerationData {
    
    private final boolean addOverride;
    private final boolean useGettersInsteadOfFields;
    
    private final IType objectClass;
    private final boolean compareReferences;
    private final boolean useBlocksInIfStatements;

    public EqualsMethodGenerationData(IField[] checkedFields, IType objectClass, boolean appendSuper,
            boolean generateComment, boolean compareReferences, boolean addOverride, boolean useGettersInsteadOfFields,
            boolean useBlocksInIfStatements) {
        super(checkedFields, appendSuper, generateComment);
        this.objectClass = objectClass;
        this.compareReferences = compareReferences;
        this.addOverride = addOverride;
        this.useGettersInsteadOfFields = useGettersInsteadOfFields;
        this.useBlocksInIfStatements = useBlocksInIfStatements;
    }

    public IType getObjectClass() {
        return objectClass;
    }

    public boolean isCompareReferences() {
        return compareReferences;
    }

    public boolean isAddOverride() {
        return addOverride;
    }

    public boolean isUseGettersInsteadOfFields() {
        return useGettersInsteadOfFields;
    }

    public boolean isUseBlocksInIfStatements() {
        return useBlocksInIfStatements;
    }
}