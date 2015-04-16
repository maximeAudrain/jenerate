package org.jenerate.internal.domain.data.impl;

import org.jenerate.internal.domain.data.CompareToGenerationData;

/**
 * Specific implementation of the {@link CompareToGenerationData}
 * 
 * @author maudrain
 */
public class CompareToGenerationDataImpl extends AbstractMethodGenerationData implements CompareToGenerationData {

    private CompareToGenerationDataImpl(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbstractMethodGenerationData.Builder<Builder> {

        @Override
        public Builder getThis() {
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public CompareToGenerationData build() {
            return new CompareToGenerationDataImpl(getThis());
        }
    }

}
