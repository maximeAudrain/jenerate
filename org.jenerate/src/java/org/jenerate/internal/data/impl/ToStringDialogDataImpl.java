package org.jenerate.internal.data.impl;

import org.jenerate.internal.data.ToStringDialogData;

public class ToStringDialogDataImpl extends AbstractFieldDialogData implements ToStringDialogData {

    private final String toStringStyle;

    private ToStringDialogDataImpl(Builder builder) {
        super(builder);
        this.toStringStyle = builder.builderToStringStyle;
    }

    @Override
    public String getToStringStyle() {
        return toStringStyle;
    }

    public static class Builder extends AbstractFieldDialogData.Builder<Builder> {

        private String builderToStringStyle;

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder withToStringStyle(String toStringStyle) {
            this.builderToStringStyle = toStringStyle;
            return getThis();
        }

        public ToStringDialogData build() {
            return new ToStringDialogDataImpl(getThis());
        }

    }
}
