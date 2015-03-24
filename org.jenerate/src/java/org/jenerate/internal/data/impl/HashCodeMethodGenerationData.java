package org.jenerate.internal.data.impl;

import org.eclipse.jdt.core.IField;
import org.jenerate.internal.data.IInitMultNumbers;

/**
 * XXX Needs protection and consider using builder pattern
 * 
 * @author maudrain
 */
public class HashCodeMethodGenerationData extends AbstractMethodGenerationData {
    
    private final boolean addOverride;
    private final boolean useGettersInsteadOfFields;
    
    private final String cachingField;
    
    private final IInitMultNumbers imNumbers;

    public HashCodeMethodGenerationData(IField[] checkedFields, boolean appendSuper, boolean generateComment,
            IInitMultNumbers imNumbers, String cachingField, boolean addOverride, boolean useGettersInsteadOfFields) {
        super(checkedFields, appendSuper, generateComment);
        this.imNumbers = imNumbers;
        this.cachingField = cachingField;
        this.addOverride = addOverride;
        this.useGettersInsteadOfFields = useGettersInsteadOfFields;
    }

    public IInitMultNumbers getImNumbers() {
        return imNumbers;
    }

    public String getCachingField() {
        return cachingField;
    }

    public boolean isAddOverride() {
        return addOverride;
    }

    public boolean isUseGettersInsteadOfFields() {
        return useGettersInsteadOfFields;
    }
}