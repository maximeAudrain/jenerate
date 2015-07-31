package org.jenerate.internal.domain.data.impl;

import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringStyle;

/**
 * Specific implementation of the {@link ToStringGenerationData}
 * 
 * @author maudrain
 */
public class ToStringGenerationDataImpl extends AbstractMethodGenerationData implements ToStringGenerationData {

    private final CommonsLangToStringStyle toStringStyle;

    private ToStringGenerationDataImpl(Builder builder) {
        super(builder);
        this.toStringStyle = builder.builderToStringStyle;
    }

    @Override
    public CommonsLangToStringStyle getToStringStyle() {
        return toStringStyle;
    }

    public static class Builder extends AbstractMethodGenerationData.Builder<Builder> {

        private CommonsLangToStringStyle builderToStringStyle;

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder withToStringStyle(CommonsLangToStringStyle toStringStyle) {
            this.builderToStringStyle = toStringStyle;
            return getThis();
        }

        @SuppressWarnings("unchecked")
        @Override
        public ToStringGenerationData build() {
            return new ToStringGenerationDataImpl(getThis());
        }

    }
}
