package org.jenerate.internal.data.impl;

import org.eclipse.jdt.core.IField;

/**
 * XXX Needs protection and consider using builder pattern
 * 
 * @author maudrain
 */
public class ToStringMethodGenerationData extends AbstractMethodGenerationData {
    
    private final boolean addOverride;
    private final boolean useGettersInsteadOfFields;
    
    private final String cachingField;
    
    private final String styleConstant;

    public ToStringMethodGenerationData(IField[] checkedFields, boolean appendSuper, boolean generateComment,
            String styleConstant, String cachingField, boolean addOverride, boolean useGettersInsteadOfFields) {
        super(checkedFields, appendSuper, generateComment);
        this.styleConstant = styleConstant;
        this.cachingField = cachingField;
        this.addOverride = addOverride;
        this.useGettersInsteadOfFields = useGettersInsteadOfFields;
    }

    public String getStyleConstant() {
        return styleConstant;
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