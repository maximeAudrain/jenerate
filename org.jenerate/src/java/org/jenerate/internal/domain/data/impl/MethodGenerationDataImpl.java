package org.jenerate.internal.domain.data.impl;

import org.jenerate.internal.domain.data.MethodGenerationData;

/**
 * Default implementation of the {@link MethodGenerationData}
 * 
 * @author maudrain
 */
public class MethodGenerationDataImpl extends AbstractMethodGenerationData {

    private MethodGenerationDataImpl(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbstractMethodGenerationData.Builder<Builder> {

        @Override
        public Builder getThis() {
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public MethodGenerationData build() {
            return new MethodGenerationDataImpl(getThis());
        }
    }

}
