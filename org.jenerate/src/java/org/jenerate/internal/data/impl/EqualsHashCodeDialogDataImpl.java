package org.jenerate.internal.data.impl;

import org.jenerate.internal.data.EqualsHashCodeDialogData;
import org.jenerate.internal.data.IInitMultNumbers;

public class EqualsHashCodeDialogDataImpl extends AbstractFieldDialogData implements EqualsHashCodeDialogData {

    private final boolean compareReferences;
    private final IInitMultNumbers initMultNumbers;

    private EqualsHashCodeDialogDataImpl(Builder builder) {
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

    public static class Builder extends AbstractFieldDialogData.Builder<Builder> {

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

        public EqualsHashCodeDialogData build() {
            return new EqualsHashCodeDialogDataImpl(getThis());
        }

    }

}
