package org.jenerate.internal.domain.data.impl;

import org.jenerate.internal.domain.data.CompareToGenerationData;

public class CompareToGenerationDataImpl extends AbstractMethodGenerationData implements CompareToGenerationData {

    private CompareToGenerationDataImpl(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbstractMethodGenerationData.Builder<Builder> {

        @Override
        public Builder getThis() {
            return this;
        }

        public CompareToGenerationData build() {
            return new CompareToGenerationDataImpl(getThis());
        }
    }

}
