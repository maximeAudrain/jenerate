package org.jenerate.internal.domain.data.impl;

import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.hashcode.IInitMultNumbers;

/**
 * Specific implementation of the {@link EqualsHashCodeGenerationData}
 * 
 * @author maudrain
 */
public class EqualsHashCodeGenerationDataImpl extends AbstractMethodGenerationData
        implements EqualsHashCodeGenerationData {

    private final boolean compareReferences;
    private final IInitMultNumbers initMultNumbers;
    private final boolean classComparison;
    private final boolean classCast;

    private EqualsHashCodeGenerationDataImpl(Builder builder) {
        super(builder);
        this.compareReferences = builder.builderCompareReferences;
        this.classComparison = builder.builderClassComparison;
        this.initMultNumbers = builder.builderInitMultNumbers;
        this.classCast = builder.builderClassCast;
    }

    @Override
    public boolean compareReferences() {
        return compareReferences;
    }

    @Override
    public IInitMultNumbers getInitMultNumbers() {
        return initMultNumbers;
    }

    @Override
    public boolean useClassComparison() {
        return classComparison;
    }

    @Override
    public boolean useClassCast() {
        return classCast;
    }

    public static class Builder extends AbstractMethodGenerationData.Builder<Builder> {

        private boolean builderCompareReferences;
        private IInitMultNumbers builderInitMultNumbers;
        private boolean builderClassComparison;
        private boolean builderClassCast;

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

        public Builder withClassComparison(boolean classComparison) {
            this.builderClassComparison = classComparison;
            return getThis();
        }

        public Builder withClassCast(boolean classCast) {
            this.builderClassCast = classCast;
            return getThis();
        }

        @SuppressWarnings("unchecked")
        @Override
        public EqualsHashCodeGenerationData build() {
            return new EqualsHashCodeGenerationDataImpl(getThis());
        }

    }

}
