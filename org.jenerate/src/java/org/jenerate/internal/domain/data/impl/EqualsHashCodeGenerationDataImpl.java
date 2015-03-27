package org.jenerate.internal.domain.data.impl;

import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.hashcode.IInitMultNumbers;

public class EqualsHashCodeGenerationDataImpl extends AbstractMethodGenerationData implements EqualsHashCodeGenerationData {

    private final boolean compareReferences;
    private final IInitMultNumbers initMultNumbers;

    private EqualsHashCodeGenerationDataImpl(Builder builder) {
        super(builder);
        this.compareReferences = builder.builderCompareReferences;
        this.initMultNumbers = builder.builderInitMultNumbers;
    }

    @Override
    public boolean getCompareReferences() {
        return compareReferences;
    }

    @Override
    public IInitMultNumbers getInitMultNumbers() {
        return initMultNumbers;
    }

    public static class Builder extends AbstractMethodGenerationData.Builder<Builder> {

        private boolean builderCompareReferences;
        private IInitMultNumbers builderInitMultNumbers;

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder withCompareReferences(boolean compareReferences) {
            this.builderCompareReferences = compareReferences;
            return getThis();
        }

        public Builder withInitMultNumbers(IInitMultNumbers initMultNumbers) {
            this.builderInitMultNumbers = initMultNumbers;
            return getThis();
        }

        public EqualsHashCodeGenerationData build() {
            return new EqualsHashCodeGenerationDataImpl(getThis());
        }

    }

}
