package org.jenerate.internal.domain.data.impl;

import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.method.content.tostring.ToStringStyle;

public class ToStringGenerationDataImpl extends AbstractMethodGenerationData implements ToStringGenerationData {

    private final ToStringStyle toStringStyle;

    private ToStringGenerationDataImpl(Builder builder) {
        super(builder);
        this.toStringStyle = builder.builderToStringStyle;
    }

    @Override
    public ToStringStyle getToStringStyle() {
        return toStringStyle;
    }

    public static class Builder extends AbstractMethodGenerationData.Builder<Builder> {

        private ToStringStyle builderToStringStyle;

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder withToStringStyle(ToStringStyle toStringStyle) {
            this.builderToStringStyle = toStringStyle;
            return getThis();
        }

        public ToStringGenerationData build() {
            return new ToStringGenerationDataImpl(getThis());
        }

    }
}
